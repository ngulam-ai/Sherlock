package agency.akcom.mmg.sherlock.ui.server.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONObject;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import agency.akcom.mmg.sherlock.ui.server.avazu.AvazuUtils;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Report.ReportDatum;
import agency.akcom.mmg.sherlock.ui.server.configConnection.SecretIdConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.server.dao.DspDao;
import agency.akcom.mmg.sherlock.ui.server.dao.ImportLogDao;
import agency.akcom.mmg.sherlock.ui.server.options.TaskOptions;
import agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog;
import agency.akcom.mmg.sherlock.ui.shared.enums.ImportStatus;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvazuImportTask extends AbstractTask implements TaskOptions {

	private static final TimeZone TZ = TimeZone.getTimeZone("Europe/Madrid");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	{
		DATE_FORMAT.setTimeZone(TZ);
	}

	@Override
	public void run() {
		log.info("AvazuImportTask runing");
		ImportLog importLog = new ImportLog(Partner.AVAZU);
		
		//Getting credentials for Avazu
		DspDao dspdao = new DspDao();
		ArrayList<ConfigConnection> credentialsList;
		try {
			credentialsList = dspdao.getCredentials(Partner.AVAZU);
			// If got empty list
			if (credentialsList == null) {
				log.warn("Not credentials for Avazu");
				saveImportLog(importLog, true);
				return;
			}
		} catch (NoSuchFieldException ex) {
			log.warn("Not found credentials for Avazu");
			saveImportLog(importLog, true);
			return;
		}

		Publisher publisher = preparePublisher();
		if (publisher == null) {
			log.error("Avazu Publisher was not prepared");
			saveImportLog(importLog, false);
			return;
		}

		String yesterday = getYesterdayFormated();

		//Report data from API for each credential
		for (ConfigConnection config : credentialsList) {
			SecretIdConnection credentials = (SecretIdConnection) config;
			AvazuUtils avazuUtils = new AvazuUtils(credentials);
			if(avazuUtils.checkingValidCredentials() == false) {
				log.warn("AVAZU. Invalid credentials: ClientSecret{" + credentials.getClientSecret() + "}, ClientId{" + credentials.getClientId());
				continue;
			}
			
			List<ReportDatum> reportDatums = avazuUtils.getFullReportDatum("creative", yesterday, yesterday, "site");

			Map<String, String> campaignsBidTypes = avazuUtils.getCampaignsWithBidTypes();

			for (ReportDatum datum : reportDatums) {
				log.info(datum.toString());
				postToPubSub(publisher, datum, campaignsBidTypes);
			}

			// When finished with the publisher, shutdown to free up resources.
			try {
				publisher.shutdown();
			} catch (Exception e) {
				log.error(e.toString());
			}
		}
		saveImportLog(importLog, true);
	}
	
	private static Publisher preparePublisher() {
		TopicName topicName = TopicName.of(Settings.getProjectId(), Settings.getTopicId());
		Publisher publisher = null;
		try {
			publisher = Publisher.newBuilder(topicName).build();
		} catch (IOException e) {
			log.error(e.toString());
		}
		return publisher;
	}

	private static String getYesterdayFormated() {
		Date now = new Date();
		GregorianCalendar gCalendar = new GregorianCalendar(TZ);
		gCalendar.setTime(now);
		gCalendar.add(Calendar.DAY_OF_YEAR, -1); // yesterday
		String yesterday = DATE_FORMAT.format(gCalendar.getTime());
		return yesterday;
	}

	private static void postToPubSub(Publisher publisher, ReportDatum datum, Map<String, String> campaignsBidTypes) {
		// schedule a message to be published, messages are automatically batched
		// convert message to bytes
		ByteString data = ByteString.copyFromUtf8(prepareMessage(datum, campaignsBidTypes));

		PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
		ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);

		ApiFutures.addCallback(messageIdFuture, new ApiFutureCallback<String>() {
			@Override
			public void onSuccess(String messageId) {
				log.info("published with message id: " + messageId);
			}

			@Override
			public void onFailure(Throwable throwable) {
				log.error("failed to publish: " + throwable);
			}
		});
	}

	private static String prepareMessage(ReportDatum datum, Map<String, String> campaignsBidTypes) {
		JSONObject jsonObject = new JSONObject();

		// TODO Add all other required Hit values
		// hitId ?
		// time ?
		// cid ?
		
		// in order to put it in proper day of the costdata_* tables
		jsonObject.put(Keys.getTIME_KEY(),
				LocalDate.parse(datum.getDay()).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
		// new GregorianCalendar(TZ).getTime().getTime());

		jsonObject.put(Keys.getDSP_KEY(), "Avazu MDSP");
		jsonObject.put(Keys.getMODEL_KEY(), campaignsBidTypes.get(datum.getCampaign_id()));
		jsonObject.put(Keys.getSOURCE_KEY(), datum.getSite_id());
		jsonObject.put(Keys.getMEDIUM_KEY(), "Display"); // TODO other possible options?
		jsonObject.put(Keys.getCONTENT_KEY(), datum.getCreative_id());
		/// jsonObject.put(TERM_KEY, null); // not used in these campaigns
		jsonObject.put(Keys.getCAMPAIGN_NAME_KEY(), datum.getCampaign_name());
		jsonObject.put(Keys.getCAMPAIGN_ID_KEY(), datum.getCampaign_id());

		jsonObject.put(Keys.getDATE_KEY(), datum.getDay().replaceAll("-", ""));

		jsonObject.put(Keys.getIMPRESSIONS_KEY(), "" + datum.getImpressions());
		jsonObject.put(Keys.getCLICKS_KEY(), "" + datum.getClicks());
		jsonObject.put(Keys.getCONVERSION_KEY(), "" + datum.getConversions());
		jsonObject.put(Keys.getSPEND_KEY(), "" + datum.getSpend());

		// not for Cost table insertion, just for debug
		jsonObject.put(Keys.getSITE_NAME_KEY(), datum.getSite_name());

		return jsonObject.toString();
	}

	@Override
	protected String getUniqueKey() {
		return "";
	}

}

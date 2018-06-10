package agency.akcom.mmg.sherlock.ui.server.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import agency.akcom.mmg.sherlock.ui.server.dao.ImportLogDao;
import agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog;
import agency.akcom.mmg.sherlock.ui.shared.enums.ImportStatus;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvazuImportTask extends AbsractTask {

	private static final String TOPIC_ID = "sherlock-real-time-ga-hit-data";
	private static final String PROJECT_ID = "sherlock-184721";

	private static final TimeZone TZ = TimeZone.getTimeZone("Europe/Madrid");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	{
		DATE_FORMAT.setTimeZone(TZ);
	}

	private static final String DATE_KEY = "date";
	// based on
	// https://developers.google.com/analytics/devguides/collection/protocol/v1/parameters?hl=en#trafficsources
	private static final String DSP_KEY = "cd12";
	private static final String MODEL_KEY = "cd98";
	private static final String SOURCE_KEY = "cs";
	private static final String MEDIUM_KEY = "cm";
	private static final String CONTENT_KEY = "cc";
	private static final String TERM_KEY = "ck";
	private static final String CAMPAIGN_NAME_KEY = "cn";
	private static final String CAMPAIGN_ID_KEY = "ci";

	private static final String TIME_KEY = "time";

	private static final String IMPRESSIONS_KEY = "_imp";
	private static final String CLICKS_KEY = "_clk";
	private static final String CONVERSION_KEY = "_cnv";
	private static final String SPEND_KEY = "cp.ap";

	private static final String SITE_NAME_KEY = "_sn";

	@Override
	public void run() {
		log.info("AvazuImportTask runing");
		ImportLog importLog = new ImportLog(Partner.AVAZU);
		ImportLogDao importLogDao = new ImportLogDao();
		importLogDao.save(importLog);

		Publisher publisher = preparePublisher();

		if (publisher != null) {

			String yesterday = getYesterdayFormated();
			List<ReportDatum> reportDatums = AvazuUtils.getFullReportDatum("creative", yesterday, yesterday, "site");

			Map<String, String> campaignsBidTypes = AvazuUtils.getCampaignsWithBidTypes();

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

		importLog.setEnd(new Date());
		importLog.setStatus(ImportStatus.SUCCESS);
		importLogDao.save(importLog);
	}

	private static Publisher preparePublisher() {
		TopicName topicName = TopicName.of(PROJECT_ID, TOPIC_ID);
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
		jsonObject.put(TIME_KEY, new GregorianCalendar(TZ).getTime().getTime());

		jsonObject.put(DSP_KEY, "Avazu MDSP");
		jsonObject.put(MODEL_KEY, campaignsBidTypes.get(datum.getCampaign_id()));
		jsonObject.put(SOURCE_KEY, datum.getSite_id());
		jsonObject.put(MEDIUM_KEY, "Display"); // TODO other possible options?
		jsonObject.put(CONTENT_KEY, datum.getCreative_id());
		log.warn(CONTENT_KEY + " " + datum.getCreative_id());
		/// jsonObject.put(TERM_KEY, null); // not used in these campaigns
		jsonObject.put(CAMPAIGN_NAME_KEY, datum.getCampaign_name());
		jsonObject.put(CAMPAIGN_ID_KEY, datum.getCampaign_id());

		jsonObject.put(DATE_KEY, datum.getDay().replaceAll("-", ""));

		jsonObject.put(IMPRESSIONS_KEY, "" + datum.getImpressions());
		jsonObject.put(CLICKS_KEY, "" + datum.getClicks());
		jsonObject.put(CONVERSION_KEY, "" + datum.getConversions());
		jsonObject.put(SPEND_KEY, "" + datum.getSpend());

		// not for Cost table insertion, just for debug
		jsonObject.put(SITE_NAME_KEY, datum.getSite_name());

		return jsonObject.toString();
	}

	@Override
	protected String getUniqueKey() {
		return "";
	}

}

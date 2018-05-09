package agency.akcom.mmg.sherlock.ui.server.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.json.JSONObject;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import agency.akcom.mmg.sherlock.ui.server.avazu.clients.AvazuClient;
import agency.akcom.mmg.sherlock.ui.server.avazu.clients.AvazuClientBuilder;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.Auth;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.AuthRequest;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.Report;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.Report.Datum;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.ReportRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvazuImportTask extends AbsractTask {

	private static final String TOPIC_ID = "sherlock-real-time-ga-hit-data-test";
	private static final String PROJECT_ID = "sherlock-184721";

	private static final TimeZone TZ = TimeZone.getTimeZone("Europe/Madrid");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	{
		DATE_FORMAT.setTimeZone(TZ);
	}

	private static final String DATE_KEY = "date";
	// based on
	// https://developers.google.com/analytics/devguides/collection/protocol/v1/parameters?hl=en#trafficsources
	private static final String SOURCE_KEY = "cs";
	private static final String MEDIUM_KEY = "cm";
	private static final String TIME_KEY = "time";
	private static final String CAMPAIGN_NAME_KEY = "cn";
	private static final String CAMPAIGN_ID_KEY = "ci";
	private static final String CREATIVE_NAME_KEY = null;
	private static final String CREATIVE_ID_KEY = null;
	private static final String SPEND_KEY = "cp.ap";

	@Override
	public void run() {
		log.info("AvazuImportTask runing");

		Publisher publisher = preparePublisher();

		if (publisher != null) {
			Report report = getTodayReport();
			for (Datum datum : report.getData()) {
				log.info(datum.toString());
				postToPubSub(publisher, datum);
			}
		}
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

	private static Report getTodayReport() {

		String yesterday = getYesterdayFormated();

		AvazuClient avazuClient = new AvazuClientBuilder().getAvazuClient();
		Auth auth = avazuClient.getAuth(new AuthRequest());

		ReportRequest reportRequest = new ReportRequest(auth.getAccess_token(), "campaign", yesterday, yesterday);
		Report report = avazuClient.getReport(reportRequest);
		return report;
	}

	private static String getYesterdayFormated() {
		Date now = new Date();
		GregorianCalendar gCalendar = new GregorianCalendar(TZ);
		gCalendar.setTime(now);
		gCalendar.add(Calendar.DAY_OF_YEAR, -1); // yesterday
		String yesterday = DATE_FORMAT.format(gCalendar.getTime());
		return yesterday;
	}

	private static void postToPubSub(Publisher publisher, Datum datum) {
		// schedule a message to be published, messages are automatically batched
		// convert message to bytes
		ByteString data = ByteString.copyFromUtf8(prepareMessage(datum));

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

	private static String prepareMessage(Datum datum) {
		JSONObject jsonObject = new JSONObject();

		// TODO Add all other required Hit values
		// hitId ?
		// time ?
		// cid ?
		jsonObject.put(TIME_KEY, new GregorianCalendar(TZ).getTime().getTime());

		jsonObject.put(SOURCE_KEY, "Avazu DSP");
		jsonObject.put(MEDIUM_KEY, "Display");

		jsonObject.put(DATE_KEY, datum.getDay());
		jsonObject.put(CAMPAIGN_NAME_KEY, datum.getCampaign_name());
		jsonObject.put(CAMPAIGN_ID_KEY, datum.getCampaign_id());

		// jsonObject.put(CREATIVE_NAME_KEY, datum.getCreative_name());
		// jsonObject.put(CREATIVE_ID_KEY, datum.getCreative_id());

		jsonObject.put(SPEND_KEY, "" + datum.getSpend());

		return jsonObject.toString();
	}

	@Override
	protected String getUniqueKey() {
		return "";
	}

}
package agency.akcom.mmg.sherlock.ui.server.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONObject;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import agency.akcom.mmg.sherlock.ui.server.dao.ImportLogDao;
import agency.akcom.mmg.sherlock.ui.server.pocket.PocketUtils;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.ReportDatum;
import agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog;
import agency.akcom.mmg.sherlock.ui.shared.enums.ImportStatus;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PocketMathImportTask extends AbstractTask {
	
	private static final String TOPIC_ID = "sherlock-real-time-ga-hit-data";
	private static final String PROJECT_ID = "sherlock-184721";

	private static final TimeZone TZ = TimeZone.getTimeZone("Europe/Madrid");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	{
		DATE_FORMAT.setTimeZone(TZ);
	}

	private static final String DATE_KEY = "date";

	private static final String DSP_KEY = "cd12"; 			//Pocketmath
	private static final String MODEL_KEY = "cd98"; 		//cpm
	private static final String SOURCE_KEY = "cs"; 			//Publisher name (example: Spotify App)
	private static final String MEDIUM_KEY = "cm"; 			//Display
	private static final String CONTENT_KEY = "cc";			//"name" in order stats ??Why adContent == campaign name??
	private static final String TERM_KEY = "ck"; 			//does not apply to pocketmath because they do not use keywords
	private static final String CAMPAIGN_NAME_KEY = "cn"; 	//"name" in order info by OrderId
	private static final String CAMPAIGN_ID_KEY = "ci";   	//"campaign_id" in order stats

	private static final String TIME_KEY = "time";			//timestamp: 1540252800000

	//in first entry
	private static final String IMPRESSIONS_KEY = "_imp"; 	//"impressions"
	private static final String CLICKS_KEY = "_clk";		//"clicks"
	private static final String CONVERSION_KEY = "_cnv";	//"conversions"
	private static final String SPEND_KEY = "cp.ap";		//"spend"

	private static final String SITE_NAME_KEY = "_sn";
	
	@Override
	public void run() {
		log.info("PocketMath import runing");
		ImportLog importLog = new ImportLog(Partner.POCKETMATH);
		ImportLogDao importLogDao = new ImportLogDao();
		importLogDao.save(importLog);

		Publisher publisher = preparePublisher();

		if (publisher != null) {
			String startDate = getFromDate();
			String endDate = getToDate();
			String yesterday = getYesterday(startDate);

			List<ReportDatum> report = PocketUtils.getReport(startDate, endDate);

			for (ReportDatum rep : report) {
				System.out.println(rep.toString());
				postToPubSub(publisher, rep, yesterday);
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

	@Override
	protected String getUniqueKey() {
		return "";
	}

	//TODO same the logic with Avazu
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
	
	//TODO same the logic with Avazu
	private static void postToPubSub(Publisher publisher, ReportDatum datum, String date) {
		// schedule a message to be published, messages are automatically batched
		// convert message to bytes
		ByteString data = ByteString.copyFromUtf8(prepareMessage(datum, date));

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
	
	public static String prepareMessage(ReportDatum datum, String date) {
		JSONObject jsonObject = new JSONObject();

		// in order to put it in proper day of the costdata_* tables
		
		jsonObject.put(TIME_KEY,
				LocalDate.parse(date).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());

		jsonObject.put(DSP_KEY, "Pocketmath");
		jsonObject.put(MODEL_KEY, "cpm");
//		jsonObject.put(SOURCE_KEY, datum.getReportPublisher().getName());
		jsonObject.put(MEDIUM_KEY, "Display");
		jsonObject.put(CONTENT_KEY, datum.getInfoOrder().getCreative().getName());
		
		jsonObject.put(CAMPAIGN_NAME_KEY, datum.getInfoOrder().getName());
		jsonObject.put(CAMPAIGN_ID_KEY, datum.getInfoOrder().getCampaign_id());

		jsonObject.put(DATE_KEY, date.replaceAll("-", ""));

		jsonObject.put(IMPRESSIONS_KEY, datum.getOrder().getImpressions());
		jsonObject.put(CLICKS_KEY, datum.getOrder().getClicks());
		jsonObject.put(CONVERSION_KEY, datum.getOrder().getConversions());
		jsonObject.put(SPEND_KEY, datum.getOrder().getSpend());

		// not for Cost table insertion, just for debug
		jsonObject.put(SITE_NAME_KEY, "");

		return jsonObject.toString();
	}

	/**
	 * @return Representation yesterday's beginning day as String, formatted
	 *         "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'". Example for 2017-03-26 :
	 *         2017-03-26T00:00:00.000Z
	 */
	public static String getFromDate() {
		ZonedDateTime yesterday = ZonedDateTime.now().with(ChronoField.NANO_OF_DAY, 0).minusDays(1);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String date = dateTimeFormatter.format(yesterday);
		return date;
	}

	/**
	 * @return Representation yesterday's end of the day as String, formatted
	 *         "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'". Example for 2017-03-26 :
	 *         2017-03-26T23:59:59.999Z
	 */
	public static String getToDate() {
		ZonedDateTime today = ZonedDateTime.now().with(ChronoField.NANO_OF_DAY, 0).minusNanos(1);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String date = dateTimeFormatter.format(today);
		return date;
	}
	
	/**
	 * @return Representation the day as String, formatted "yyyy-MM-dd".
	 */
	public static String getYesterday(String startDate) {
		ZonedDateTime yesterday = ZonedDateTime.parse(startDate);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return dateTimeFormatter.format(yesterday);
	}

	// for test by date
	public static String getFromDate(String date) {
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd")
				.parseDefaulting(ChronoField.NANO_OF_DAY, 0).toFormatter().withZone(ZoneId.systemDefault());

		ZonedDateTime yesterday = ZonedDateTime.parse(date, formatter).with(ChronoField.NANO_OF_DAY, 0);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String d = dateTimeFormatter.format(yesterday);
		return d;
	}

	public static String getToDate(String date) {
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd")
				.parseDefaulting(ChronoField.NANO_OF_DAY, 0).toFormatter().withZone(ZoneId.systemDefault());

		ZonedDateTime today = ZonedDateTime.parse(date, formatter).plusDays(1).with(ChronoField.NANO_OF_DAY, 0).minusNanos(1);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String d = dateTimeFormatter.format(today);
		return d;
	}

}

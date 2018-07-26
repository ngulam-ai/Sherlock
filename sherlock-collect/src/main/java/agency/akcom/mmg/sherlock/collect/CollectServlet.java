package agency.akcom.mmg.sherlock.collect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

//@WebServlet(name = "CollectServlet", urlPatterns = { "/collect" })
public class CollectServlet extends HttpServlet {

	private static final String TOPIC_ID = "sherlock-real-time-ga-hit-data";

	private static final String PROJECT_ID = "sherlock-184721"; 

	private static final Map<String, String> COUNTRY_CITY_TIMEZONE_MAP = new HashMap<String, String>(); //key: "country/city" value "timezone"

	TopicName topicName = TopicName.of(PROJECT_ID, TOPIC_ID);
	// Create a publisher instance with default settings bound to the topic
	Publisher publisher = null; // Publisher.newBuilder(topicName).build();

	@Override
	public void init() throws ServletException {
		try {
			publisher = Publisher.newBuilder(topicName).build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		createTimeZoneMap();
		System.out.println("CollectServlet.init() complited");
	}

	@Override
	public void destroy() {
		if (publisher != null) {
			// When finished with the publisher, shutdown to free up resources.
			try {
				publisher.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		super.destroy();

		System.out.println("CollectServlet.destroy() complited");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		// printRequest(req, "GET");

		try {
			postToPubSub(putParamToJSON(req));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// printRequest(req, "POST");

		try {
			postToPubSub(putParamToJSON(req));
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().print("POST collect servlet");
	}

	protected JSONObject putParamToJSON(HttpServletRequest req) {

		JSONObject reqJson = requestParamsToJSON(req);

		// add additional parameters if they not exist
		tryToPutOnce(reqJson, "hitId", UUID.randomUUID().toString()); // Hit identifier represented as UUID (version 4)

		// --- userAgent & IP
		tryToPutOnce(reqJson, "ua", req.getHeader("User-Agent"));
		tryToPutOnce(reqJson, "__uip", req.getRemoteAddr());

		// --- date, time and so on default Europe/Madrid:
		String idTimeZone = "Europe/Madrid"; // TODO Determine and use Analytics account time zone;
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(idTimeZone));
		SimpleDateFormat format = new SimpleDateFormat();
		//TODO consider to implement with several static formats with different patterns - should speed up
		format.setTimeZone(TimeZone.getTimeZone(idTimeZone));
		int offsetTimeZone = TimeZone.getTimeZone(idTimeZone).getOffset(calendar.getTime().getTime());
		tryToPutOnce(reqJson, "time", "" + (calendar.getTime().getTime()+offsetTimeZone));
		// Hit time on the server according to the time zone
		format.applyPattern("yyyyMMdd");
		format.setCalendar(calendar);
		tryToPutOnce(reqJson, "date", "" + format.format(calendar.getTime()));
		// The day of the month, a two-digit number from 01 to 31.
		tryToPutOnce(reqJson, "hour", "" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
		// A two-digit hour of the day ranging from 00-23 in the
		// timezone configured for the account. This value is also
		// corrected for daylight savings time.
		tryToPutOnce(reqJson, "minute", "" + String.format("%02d", calendar.get(Calendar.MINUTE)));
		// Returns the minutes, between 00 and 59, in the hour.

		// Set timeZone use of custom dimensions
		
		idTimeZone = getTimeZoneFromCD(reqJson);

		// TimeZone
		format.setTimeZone(TimeZone.getTimeZone(idTimeZone));
		tryToPutOnce(reqJson, "cd91", "" + calendar.getTimeZone().getID());
		// LocalTime
		format.applyPattern("HH:mm:ss.SSS");
		tryToPutOnce(reqJson, "cd92", "" + format.format(calendar.getTime()));
		// Day
		format.applyPattern("dd");
		tryToPutOnce(reqJson, "cd93", "" + format.format(calendar.getTime()));
		// Weekday
		format = new SimpleDateFormat("EEEE", Locale.ENGLISH);
		tryToPutOnce(reqJson, "cd94", "" + format.format(calendar.getTime()));
		// Month
		format.applyPattern("MM");
		tryToPutOnce(reqJson, "cd95", "" + format.format(calendar.getTime()));
		// Year
		format.applyPattern("yyyy");
		tryToPutOnce(reqJson, "cd96", "" + format.format(calendar.getTime()));
		
		AudienceService.processUIds(reqJson);

		System.out.println("---request JSON---");
		System.out.println(reqJson.toString(4));
		System.out.println("===request JSON===");

		return reqJson;

	}

	private void postToPubSub(JSONObject jsonObj) throws Exception {

		// schedule a message to be published, messages are automatically batched
		// convert message to bytes
//		ByteString data = ByteString.copyFromUtf8(jsonObj.toString());
//
//		PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
//		ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
//
//		ApiFutures.addCallback(messageIdFuture, new ApiFutureCallback<String>() {
//			@Override
//			public void onSuccess(String messageId) {
//				System.out.println("published with message id: " + messageId);
//			}
//
//			@Override
//			public void onFailure(Throwable t) {
//				System.out.println("failed to publish: " + t);
//			}
//		});

	}

	private void tryToPutOnce(JSONObject jsonObj, String key, String value) {
		try {
			jsonObj.putOnce(key, value);
		} catch (JSONException e) {
			System.out.println(String.format("Parameter '%s' already exists, its value '%s', tried to add '%s' value.",
					key, jsonObj.get(key), value));
		}
	}

	public JSONObject requestParamsToJSON(ServletRequest req) {

		JSONObject jsonObj = new JSONObject();

		Map<String, String[]> params = req.getParameterMap();
		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			String v[] = entry.getValue();
			String s = (v.length == 1) ? v[0] : "";
			tryToPutOnce(jsonObj, entry.getKey(), s);
		}

		List<NameValuePair> pairs = null;
		try {
			StringEntity stringEntity = new StringEntity(getBody(req));
			stringEntity.setContentType(URLEncodedUtils.CONTENT_TYPE);
			pairs = URLEncodedUtils.parse(stringEntity);
			Map<String, String> bodyParams = toMap(pairs);
			for (Entry<String, String> entry : bodyParams.entrySet()) {
				String v = entry.getValue();
				tryToPutOnce(jsonObj, entry.getKey(), v);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonObj;
	}

	private static Map<String, String> toMap(List<NameValuePair> pairs) {
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < pairs.size(); i++) {
			NameValuePair pair = pairs.get(i);
			map.put(pair.getName(), pair.getValue());
		}
		return map;
	}

	private void printRequest(HttpServletRequest req, String type) throws IOException {
		System.out.println();
		System.out.println(type + " collect servlet: ");

		System.out.println("---");
		Map<String, String[]> parameterMap = req.getParameterMap();

		for (Entry<String, String[]> entry : parameterMap.entrySet()) {
			System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue()));
		}

		System.out.println("---");
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements())
			System.out.println(headerNames.nextElement());

		// System.out.println("---");
		// String payloadRequest = getBody(req);
		// System.out.println(payloadRequest);
	}

	public static String getBody(ServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();

		return body;
	}

	public static void createTimeZoneMap () {
		URL resourceCities = CollectServlet.class.getClassLoader().getResource("cities15000.txt"); // http://download.geonames.org/export/dump/ description file
		URL resourceCountries = CollectServlet.class.getClassLoader().getResource("country.csv"); //contains "country code" and name "country"
		Reader inCities;
		Reader inCountries;
		CSVParser parserCities;
		CSVParser parserCountries;
		List<CSVRecord> listCities = null;
		List<CSVRecord> listCountries = null;
		try {
			inCities = new FileReader(resourceCities.getFile());
			inCountries = new FileReader(resourceCountries.getFile());
			parserCities = new CSVParser(inCities, CSVFormat.TDF.withQuote(null));
			parserCountries = new CSVParser(inCountries, CSVFormat.DEFAULT);
			listCities = parserCities.getRecords();
			listCountries = parserCountries.getRecords();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, String> countries = new HashMap<String, String>(); // key: "country code", value: "country"
		for (CSVRecord p : listCountries) {
			countries.put(p.get(0), p.get(1));
		}
		for (CSVRecord parser : listCities) {
			String country = countries.get(parser.get(8));
			String country_city = country + "/" + parser.get(2);
			String timeZone = parser.get(17);
			COUNTRY_CITY_TIMEZONE_MAP.put(country_city, timeZone); // For full search "county/city"
			COUNTRY_CITY_TIMEZONE_MAP.put(country, timeZone); // For search only "country"
		}
	}
	
	public String getTimeZoneFromCD(JSONObject json) {
		String timeZone = "Europe/Madrid";
		String countryAdServer = null;
		String cityAdServer = null;

		try {
			countryAdServer = json.get("cd33").toString();
		} catch (JSONException e) {
		}
		try {
			cityAdServer = json.get("cd30").toString();
		} catch (JSONException e) {
		}

		if (COUNTRY_CITY_TIMEZONE_MAP.containsKey(countryAdServer + "/" + cityAdServer)) {
			timeZone = COUNTRY_CITY_TIMEZONE_MAP.get(countryAdServer + "/" + cityAdServer);
		} else if (COUNTRY_CITY_TIMEZONE_MAP.containsKey(countryAdServer)) {
			timeZone = COUNTRY_CITY_TIMEZONE_MAP.get(countryAdServer);
		}
		return timeZone;
	}

}
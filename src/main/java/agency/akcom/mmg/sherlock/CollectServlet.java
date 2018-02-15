package agency.akcom.mmg.sherlock;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.bfsmith.geotimezone.TimeZoneLookup;
import com.github.bfsmith.geotimezone.TimeZoneResult;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

//@WebServlet(name = "CollectServlet", urlPatterns = { "/collect" })
public class CollectServlet extends HttpServlet {

	private static final String TOPIC_ID = "sherlock-real-time-ga-hit-data";
	private static final String PROJECT_ID = "sherlock-184721"; // "mmg-sandbox";
	// ServiceOptions.getDefaultProjectId();
	private static final Map<String, String> MAP_ID_TIMEZONE = new HashMap<String, String>();

	TopicName topicName = TopicName.of(PROJECT_ID, TOPIC_ID);
	// Create a publisher instance with default settings bound to the topic
	Publisher publisher = null; // Publisher.newBuilder(topicName).build();
	LookupService lookupService = null;

	@Override
	public void init() throws ServletException {
		try {
			publisher = Publisher.newBuilder(topicName).build();
			URL resource = CollectServlet.class.getClassLoader().getResource("GeoLiteCity.dat");
			lookupService = new LookupService(new File(resource.getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		createMapTimeZone();
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
		calendar.setTime(new Date());
		tryToPutOnce(reqJson, "time", "" + calendar.getTime().getTime());
		// Hit time on the server according to the time zone
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
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
		String idTimeZoneOnIp = null;
		try {
			String countryAdServer = reqJson.get("cd33").toString();
			String cityAdServer = reqJson.get("cd30").toString();

			if (MAP_ID_TIMEZONE.containsKey(cityAdServer)) {
				idTimeZone = MAP_ID_TIMEZONE.get(cityAdServer);
			} else if (MAP_ID_TIMEZONE.containsKey(countryAdServer)) {
				idTimeZone = MAP_ID_TIMEZONE.get(countryAdServer);
			} else {
				idTimeZoneOnIp = findIDTimeZoneOnIP(reqJson);
			}
		} catch (JSONException e) {
			idTimeZoneOnIp = findIDTimeZoneOnIP(reqJson);
		}

		if (idTimeZoneOnIp != null) {
			idTimeZone = idTimeZoneOnIp;
		}

		// TimeZone
		calendar = Calendar.getInstance(TimeZone.getTimeZone(idTimeZone));
		tryToPutOnce(reqJson, "CD91", "" + calendar.getTimeZone().getID());
		// LocalTime
		format.applyPattern("HH:mm:ss.SSS");
		tryToPutOnce(reqJson, "CD92", "" + format.format(calendar.getTime()));
		// Day
		format.applyPattern("dd");
		tryToPutOnce(reqJson, "CD93", "" + format.format(calendar.getTime()));
		// Weekday
		format = new SimpleDateFormat("EEEE", Locale.ENGLISH);
		tryToPutOnce(reqJson, "CD94", "" + format.format(calendar.getTime()));
		// Month
		format.applyPattern("MM");
		tryToPutOnce(reqJson, "CD95", "" + format.format(calendar.getTime()));
		// Year
		format.applyPattern("yyyy");
		tryToPutOnce(reqJson, "CD96", "" + format.format(calendar.getTime()));

		System.out.println("---request JSON---");
		System.out.println(reqJson.toString(4));
		System.out.println("===request JSON===");

		return reqJson;

	}

	private String findIDTimeZoneOnIP(JSONObject reqJson) {
		try {
			String ip = reqJson.get("__uip").toString();
			Location location = lookupService.getLocation(ip);
			TimeZoneLookup timeZoneLookup = new TimeZoneLookup();
			TimeZoneResult timeZone = timeZoneLookup.getTimeZone(location.latitude, location.longitude);
			return timeZone.getResult();
		} catch (JSONException | NullPointerException e) {
		}
		return null;
	}

	private void postToPubSub(JSONObject jsonObj) throws Exception {

		// schedule a message to be published, messages are automatically batched
		// convert message to bytes
		ByteString data = ByteString.copyFromUtf8(jsonObj.toString());

		PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
		ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);

		ApiFutures.addCallback(messageIdFuture, new ApiFutureCallback<String>() {
			public void onSuccess(String messageId) {
				System.out.println("published with message id: " + messageId);
			}

			public void onFailure(Throwable t) {
				System.out.println("failed to publish: " + t);
			}
		});

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
			// jsonObj.put(entry.getKey(), o);
		}

		List<NameValuePair> pairs = null;
		try {
			StringEntity stringEntity = new StringEntity(getBody(req));
			stringEntity.setContentType(URLEncodedUtils.CONTENT_TYPE);
			pairs = URLEncodedUtils.parse(stringEntity);
			Map<String, String> bodyParams = toMap(pairs);
			for (Entry<String, String> entry : bodyParams.entrySet()) {
				String v = entry.getValue();
				// Object o = v;
				tryToPutOnce(jsonObj, entry.getKey(), v);
				// jsonObj.put(entry.getKey(), o);
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

	public static void createMapTimeZone() {
		String[] list = TimeZone.getAvailableIDs();

		for (int i = 0; i < list.length; i++) {
			String[] id = list[i].split("/");

			for (int j = 0; j < id.length; j++) {
				if (!MAP_ID_TIMEZONE.containsKey(id[j])) {
					MAP_ID_TIMEZONE.put(id[j], list[i]);
				}
			}
		}
	}

}
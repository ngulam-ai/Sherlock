package agency.akcom.mmg.sherlock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
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
	private static final String PROJECT_ID =  "sherlock-184721"; //"mmg-sandbox"; 
															// ServiceOptions.getDefaultProjectId();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		//printRequest(req, "GET");

		try {
			postToPubSub(req);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//printRequest(req, "POST");

		try {
			postToPubSub(req);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().print("POST collect servlet");
	}

	private void postToPubSub(HttpServletRequest req) throws Exception {

		JSONObject reqJson = requestParamsToJSON(req);

		// add additional parameters if they not exist
		tryToPutOnce(reqJson, "hitId", UUID.randomUUID().toString()); // Hit identifier represented as UUID (version 4)
		
		// --- userAgent
		tryToPutOnce(reqJson, "ua", req.getHeader("User-Agent"));
		
		// --- date, time and so on
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));  // TODO Determine and use Analytics account time zone;
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

		// ---------------

		System.out.println("---request JSON---");
		System.out.println(reqJson.toString(4));
		System.out.println("===request JSON===");

		TopicName topicName = TopicName.of(PROJECT_ID, TOPIC_ID);
		Publisher publisher = null;
		try {
			// Create a publisher instance with default settings bound to the topic
			publisher = Publisher.newBuilder(topicName).build();

			// schedule a message to be published, messages are automatically batched
			// convert message to bytes
			ByteString data = ByteString.copyFromUtf8(reqJson.toString());

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

		} finally {
			if (publisher != null) {
				// When finished with the publisher, shutdown to free up resources.
				publisher.shutdown(); 
				
				
			}
		}
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
			//jsonObj.put(entry.getKey(), o);
		}

		List<NameValuePair> pairs = null;
		try {
			StringEntity stringEntity = new StringEntity(getBody(req));
			stringEntity.setContentType(URLEncodedUtils.CONTENT_TYPE);
			System.out.println("content length: " + stringEntity.getContentLength());
			pairs = URLEncodedUtils.parse(stringEntity);
			System.out.println("pairs: " + pairs);
			Map<String, String> bodyParams = toMap(pairs);
			for (Entry<String, String> entry : bodyParams.entrySet()) {
				String v = entry.getValue();
				//Object o = v;
				tryToPutOnce(jsonObj, entry.getKey(), v);
				//jsonObj.put(entry.getKey(), o);
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
		System.out.println("--- body ---");
		System.out.println(body);
		System.out.println("=== body ===");
		return body;
	}

}
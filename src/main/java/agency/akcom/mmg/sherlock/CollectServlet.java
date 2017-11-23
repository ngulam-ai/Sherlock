package agency.akcom.mmg.sherlock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

@WebServlet(name = "CollectServlet", urlPatterns = { "/collect" })
public class CollectServlet extends HttpServlet {

	private static final String TOPIC_ID = "sherlock-real-time-ga-hit-data";
	private static final String PROJECT_ID = "mmg-sandbox"; // ServiceOptions.getDefaultProjectId();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		printRequest(req, "GET");

		try {
			postToPubSub(req);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// resp.setContentType("text/plain");
		// resp.setCharacterEncoding("UTF-8");
		// resp.getWriter().print("GET collect servlet");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		printRequest(req, "POST");

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
		reqJson.putOnce("hitId", UUID.randomUUID().toString()); // Hit identifier represented as UUID (version 4)
		reqJson.putOnce("time", new Date().getTime()); // Hit time on the server according to the time zone
		// TODO Determine and use Analytics account time zone;
		// ---------------

		System.out.println("---request JSON---");
		System.out.println(reqJson.toString(4));
		System.out.println("---request JSON---");

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

	public JSONObject requestParamsToJSON(ServletRequest req) {
		JSONObject jsonObj = new JSONObject();
		Map<String, String[]> params = req.getParameterMap();
		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			String v[] = entry.getValue();
			Object o = (v.length == 1) ? v[0] : v;
			jsonObj.put(entry.getKey(), o);
		}
		return jsonObj;
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

		System.out.println("---");
		String payloadRequest = getBody(req);
		System.out.println(payloadRequest);
	}

	public static String getBody(HttpServletRequest request) throws IOException {

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

}
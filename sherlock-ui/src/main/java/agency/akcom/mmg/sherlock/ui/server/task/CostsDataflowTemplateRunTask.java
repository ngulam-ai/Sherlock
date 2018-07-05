package agency.akcom.mmg.sherlock.ui.server.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.CreateJobFromTemplateRequest;
import com.google.api.services.dataflow.model.Job;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CostsDataflowTemplateRunTask extends AbstractTask {

	private static final String PROJECT = "sherlock-184721";
	private static final String BUCKET = "gs://sherlock-dataflow-template";
	private static final String MY_TEMPLATE_PATH = "/templates/costIncrementsTemplate";
	private static final String MY_TEMPLATE_FULL_PATH = BUCKET + MY_TEMPLATE_PATH;
	private static final String JOB_NAME_PREFIX = "cost-increments-";
	private static final ArrayList<String> scopes = new ArrayList<String>();
	{
		scopes.add("https://www.googleapis.com/auth/cloud-platform");
	}

	@Override
	public void run() {
		Map<String, String> parameters = new HashMap();
		
		// set "dateString" parameter by yesterday date
		parameters.put("dateString", LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));

		Dataflow dataflow = new Dataflow(new UrlFetchTransport(), GsonFactory.getDefaultInstance(), null);

		CreateJobFromTemplateRequest content = new CreateJobFromTemplateRequest();
		content.setJobName(JOB_NAME_PREFIX + UUID.randomUUID().toString());
		content.setGcsPath(MY_TEMPLATE_FULL_PATH);
		content.setParameters(parameters);

		try {
			// TODO determine actual project automatically 
			Job job = dataflow.projects().templates().create(PROJECT, content).setAccessToken(getAccessToken())
					.execute();
			log.info(job.toPrettyString());
		} catch (IOException e) {
			log.error(e.getMessage());
		}

	}

	private String getAccessToken() {
		final AppIdentityService appIdentity = AppIdentityServiceFactory.getAppIdentityService();
		final AppIdentityService.GetAccessTokenResult accessToken = appIdentity.getAccessToken(scopes);
		return accessToken.getAccessToken();
	}

	// @Override
	public void run_() {
		JSONObject jsonObj = null;
		try {
			// JSONObject parameters = new JSONObject()
			// .put("datastoreReadGqlQuery", "SELECT * FROM Entries")
			// .put("datastoreReadProjectId", project)
			// .put("textWritePrefix", bucket + "/output/");
			// JSONObject environment = new JSONObject()
			// .put("tempLocation", bucket + "/temp/")
			// .put("bypassTempDirValidation", false);
			jsonObj = new JSONObject().put("jobName", JOB_NAME_PREFIX + UUID.randomUUID().toString()).put("gcsPath",
					MY_TEMPLATE_FULL_PATH);
			// .put("parameters", parameters)
			// .put("environment", environment);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}

		try {
			URL url = new URL(String.format("https://dataflow.googleapis.com/v1b3/projects/%s/templates", PROJECT));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			jsonObj.write(writer);
			writer.close();

			int respCode = conn.getResponseCode();
			if (respCode == HttpURLConnection.HTTP_OK) {
				String line;
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = reader.readLine()) != null) {
					log.info(line);
				}
				reader.close();

			} else {
				StringWriter w = new StringWriter();
				IOUtils.copy(conn.getErrorStream(), w, "UTF-8");
				log.warn(w.toString());
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}

	}

	@Override
	protected String getUniqueKey() {
		// TODO try to return started Job Id or such
		return "";
	}

}

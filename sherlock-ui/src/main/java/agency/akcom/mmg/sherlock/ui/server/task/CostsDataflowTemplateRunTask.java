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

import com.google.cloud.bigquery.*;
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

import agency.akcom.mmg.sherlock.ui.server.options.TaskOptions.Settings;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CostsDataflowTemplateRunTask extends AbstractTask {

	private static final String PROJECT = Settings.getProjectId();
	private static final String BUCKET = "gs://dmpmm-dataflow-template";
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
		String dateString = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		parameters.put("dateString", dateString);

		if (checkExistTable(dateString)) {
			runDataflow(parameters);
		} else {
			log.info("Not found cost table for " + dateString + " date. Dataflow was not running.");
		}
	}

	public void runDataflow(Map<String, String> parameters) {
		Dataflow dataflow = new Dataflow(new UrlFetchTransport(), GsonFactory.getDefaultInstance(), null);

		CreateJobFromTemplateRequest content = new CreateJobFromTemplateRequest();
		content.setJobName(JOB_NAME_PREFIX + parameters.get("dateString") + "-" + UUID.randomUUID().toString());
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

	private boolean checkExistTable (String dateString) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*)");
		sb.append("FROM `" + PROJECT + ".MMG_Streaming.costdata_" + dateString + "` ");
		sb.append("LIMIT 1");

		BigQuery bigquery = BigQueryOptions.newBuilder().setProjectId(PROJECT).build().getService();
		QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(sb.toString()).setUseLegacySql(false).build();

		// Create a job ID so that we can safely retry.
		JobId jobId = JobId.of(UUID.randomUUID().toString());
		com.google.cloud.bigquery.Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

		try {
			// Wait for the query to complete.
			queryJob = queryJob.waitFor();
			// Check for errors
			if (queryJob == null) {
				throw new RuntimeException("Job no longer exists");
			} else if (queryJob.getStatus().getError() != null) {
				// You can also look at queryJob.getStatus().getExecutionErrors() for all
				// errors, not just the latest one.
				throw new RuntimeException(queryJob.getStatus().getError().toString());
			}
		} catch (Exception e) {
			if (e.getMessage().contains("Not found: Table")) {
				return false;
			} else {
				log.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}
		return true;
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

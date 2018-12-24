package agency.akcom.mmg.sherlock.ui.server.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionCostsUpdateTask extends AbstractTask {

	static final String QUERY_TEMPLATE = "UPDATE `sherlock-184721.MMG_Streaming.sessions_%1$s` AS s "
			+ "SET trafficSource.adCost = IFNULL(trafficSource.adCost, 0) + c.adCostIncrement, "
			+ "trafficSource.attributedAdCost = IFNULL(trafficSource.attributedAdCost, 0) + c.attributedAdCostIncrement "
			+ "FROM ("
			+ "  SELECT sum(adCostIncrement) AS adCostIncrement, sum(attributedAdCostIncrement) AS attributedAdCostIncrement, sessionId, clientId "
			+ "  FROM `sherlock-184721.MMG_Streaming.daily_cost_increments_%1$s`" 
			+ "  GROUP BY sessionId, clientId "
			+ ") AS c "
			+ "WHERE ((s.sessionId is NULL and c.sessionId is NULL) OR (s.sessionId = c.sessionId)) AND ((s.clientId is NULL and c.clientId is NULL) OR (s.clientId = c.clientId))";
			
	@Override 
	public void run() {
		// set "dateString" parameter by yesterday date
		LocalDate dateToProcess = LocalDate.now().minusDays(1);
		String dateString = dateToProcess.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		SessionCostsOneDayUpdateTask task = new SessionCostsOneDayUpdateTask(dateString);
		task.enqueue();
	}

	private void runAllWithinThisTask(String dateString) {

		// Instantiate a client. If you don't specify credentials when constructing a
		// client, the
		// client library will look for credentials in the environment, such as the
		// GOOGLE_APPLICATION_CREDENTIALS environment variable.
		//
		BigQuery bigquery = BigQueryOptions.newBuilder().setProjectId("sherlock-184721").build().getService();

		String query = String.format(QUERY_TEMPLATE, dateString);

		QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).setUseLegacySql(false).build();

		// Create a job ID so that we can safely retry.
		JobId jobId = JobId.of(UUID.randomUUID().toString());
		Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

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

			log.info("Session costs for day '" + dateString + "' successfully updated");
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	@Override
	protected String getUniqueKey() {
		return "";
	}

}

package agency.akcom.mmg.sherlock.ui.server.task;

import java.util.UUID;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionCostsOneDayUpdateTask extends AbstractTask {

	private final String dateString;
	
	public SessionCostsOneDayUpdateTask(String dateString) {
		this.dateString = dateString;
	}


	@Override
	public void run() {
		if (dateString == null || dateString.isEmpty()) {
			log.error("Called with invalid 'dateString' parameter (null or empty) " );
			return;
		}

		BigQuery bigquery = BigQueryOptions.newBuilder().setProjectId("sherlock-184721").build().getService();
		
		String query = " UPDATE `sherlock-184721.MMG_Streaming.sessions_copy_" + dateString + "` AS s";
		query += " SET trafficSource.adCost = adCostTotal, trafficSource.attributedAdCost = attributedAdCostTotal";
		query += " FROM `sherlock-184721.MMG_Streaming.daily_sessions_copy_with_cost_increments` AS c";
		query += " WHERE ((s.sessionId is NULL and c.sessionId is NULL) OR (s.sessionId = c.sessionId)) AND ((s.clientId is NULL and c.clientId is NULL) OR (s.clientId = c.clientId))";
		query += "       AND c.tableDate = '" + dateString + "'"; 

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
		return dateString;
	}

}

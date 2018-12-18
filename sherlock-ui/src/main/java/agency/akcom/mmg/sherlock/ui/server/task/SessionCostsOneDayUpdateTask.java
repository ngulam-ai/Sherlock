package agency.akcom.mmg.sherlock.ui.server.task;

import java.util.UUID;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;

import agency.akcom.mmg.sherlock.ui.server.options.TaskOptions.Settings;
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
			log.error("Called with invalid 'dateString' parameter (null or empty) ");
			return;
		}

		BigQuery bigquery = BigQueryOptions.newBuilder().setProjectId(Settings.getProjectId()).build().getService();

		String query = String.format(SessionCostsUpdateTask.QUERY_TEMPLATE, dateString);

		QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).setUseLegacySql(false).build();

		// Create a job ID so that we can safely retry.
		JobId jobId = JobId.of(UUID.randomUUID().toString());
		Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

		try {
			// Wait for the query to complete.
			queryJob = queryJob.waitFor(); //TODO set more time for job. com.google.apphosting.runtime.HardDeadlineExceededError: This request (00000166a026fc2a) started at 2018/10/23 09:00:01.450 UTC and was still executing at 2018/10/23 09:10:01.859 UTC.

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

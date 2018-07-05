package agency.akcom.mmg.sherlock.ui.server.task;

import java.util.UUID;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionCostsUpdateTask extends AbstractTask {

	@Override
	public void run() {
		// Instantiate a client. If you don't specify credentials when constructing a
		// client, the
		// client library will look for credentials in the environment, such as the
		// GOOGLE_APPLICATION_CREDENTIALS environment variable.
		//
		BigQuery bigquery = BigQueryOptions.newBuilder().setProjectId("sherlock-184721").build().getService();

		String querySET = "SET trafficSource.attributedAdCost = IFNULL (trafficSource.attributedAdCost, 0) + " + cost;
		if (count == UTM_TAGS_SIMPLE_LIST.size()) {
			querySET += " , trafficSource.adCost = IFNULL (trafficSource.adCost, 0) + " + cost;
		}

		String query = "UPDATE `sherlock-184721.MMG_Streaming.sessions_dev_" + iterator.next() + "` " + querySET
				+ " WHERE " + queryWHEREwithTags;
		;
		
//		UPDATE `sherlock-184721.MMG_Streaming.sessions_dev_20180612` AS s
//		SET trafficSource.adCost = adCostTotal,
//		    trafficSource.attributedAdCost  = attributedAdCostTotal
//
//		FROM `sherlock-184721.MMG_Streaming.daily_sessions_with_cost_increments` AS c
//		WHERE ((s.sessionId is NULL and c.sessionId is NULL) OR (s.sessionId = c.sessionId)) AND 
//		      ((s.clientId is NULL and c.clientId is NULL) OR (s.clientId = c.clientId))
//		      
//		-- AND (s._TABLE_SUFFIX BETWEEN '20180604' AND c.tableDate)

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
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	@Override
	public void run_() {

		// Instantiate a client. If you don't specify credentials when constructing a
		// client, the
		// client library will look for credentials in the environment, such as the
		// GOOGLE_APPLICATION_CREDENTIALS environment variable.
		BigQuery bigquery = BigQueryOptions.newBuilder().setProjectId("sherlock-184721").build().getService();

		QueryJobConfiguration queryConfig = QueryJobConfiguration
				.newBuilder("SELECT * FROM `MMG_Streaming.tmpDelta` LIMIT 10")
				// Use standard SQL syntax for queries.
				// See: https://cloud.google.com/bigquery/sql-reference/
				.setUseLegacySql(false).build();

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

			// Get the results.
			// QueryResponse response = bigquery.getQueryResults(jobId);

			TableResult result = queryJob.getQueryResults();

			// Print all pages of the results.
			for (FieldValueList row : result.iterateAll()) {
				FieldValue sessionIdFieldValue = row.get("sessionId");
				String sessionId = sessionIdFieldValue.isNull() ? null : sessionIdFieldValue.getStringValue();

				FieldValue clientIdFieldValue = row.get("clientId");
				String clientId = clientIdFieldValue.isNull() ? null : clientIdFieldValue.getStringValue();

				log.info("{}, {}", sessionId, clientId);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	protected String getUniqueKey() {
		return "";
	}

	/*
	 * Definition for 'daily_sessions_with_cost_increments' - just attempt to
	 * Versioning it
	 * 
	 * SELECT count(*) number, c.tableDate, s.sessionId, s.clientId,
	 * SUM(IFNULL(s.trafficSource.adCost, 0)) adCost, SUM(c.adCostIncrement)
	 * adCostIncrement, SUM(IFNULL(s.trafficSource.adCost, 0)) +
	 * SUM(c.adCostIncrement) adCostTotal,
	 * SUM(IFNULL(s.trafficSource.attributedAdCost, 0)) attributedAdCost,
	 * SUM(c.attributedAdCostIncrement) attributedAdCostIncrement,
	 * SUM(IFNULL(s.trafficSource.attributedAdCost, 0)) +
	 * SUM(c.attributedAdCostIncrement) attributedAdCostTotal
	 * 
	 * FROM `sherlock-184721.MMG_Streaming.sessions_*` AS s JOIN
	 * `sherlock-184721.MMG_Streaming.daily_cost_increments` AS c -- both NULL or
	 * equels ON ((s.sessionId is NULL and c.sessionId is NULL) OR (s.sessionId =
	 * c.sessionId)) AND ((s.clientId is NULL and c.clientId is NULL) OR (s.clientId
	 * = c.clientId)) AND (s._TABLE_SUFFIX = c.tableDate)
	 * 
	 * GROUP BY c.tableDate, s.sessionId, s.clientId
	 */

}

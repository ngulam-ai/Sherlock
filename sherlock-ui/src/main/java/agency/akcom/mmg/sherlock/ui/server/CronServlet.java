package agency.akcom.mmg.sherlock.ui.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import agency.akcom.mmg.sherlock.ui.server.task.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CronServlet extends HttpServlet {

	private static final String TASK_PARAMETER = "task";
	private static final String FORCE_PARAMETER = "force";

	private static final String AVAZU_DAILY_IMPORT = "avazu_daily_import";
	private static final String POCKETMATH_DAILY_IMPORT = "pocketmath_daily_import";
	private static final String PERFECTAUDIENCE_DAILY_IMPORT = "perfectaudience_daily_import";
	private static final String COSTS_DAILY_DF_PIPELINE = "costs_daily_df_pipeline";
	private static final Object SESSION_COSTS_UPDATE = "session_costs_update"; 

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		log.info("Starting cron execution...");

		String taskType = req.getParameter(TASK_PARAMETER);
		boolean force = "true".equals(req.getParameter(FORCE_PARAMETER));

		if (AVAZU_DAILY_IMPORT.equals(taskType)) {
			log.info("Running cron job: " + taskType);
			AbstractTask task = new AvazuImportTask();
			task.enqueue();
		
		} else if (POCKETMATH_DAILY_IMPORT.equals(taskType)) {
			log.info("Running cron job: " + taskType);
			AbstractTask task = new PocketMathImportTask();
			task.enqueue();
			
		} else if (PERFECTAUDIENCE_DAILY_IMPORT.equals(taskType)) {
			log.info("Running cron job: " + taskType);
			AbstractTask task = new PerfectAudienceTask();
			task.enqueue();

		} else if (COSTS_DAILY_DF_PIPELINE.equals(taskType)) {
			log.info("Running cron job: " + taskType);
			AbstractTask task = new CostsDataflowTemplateRunTask();
			task.enqueue();

		} else if (SESSION_COSTS_UPDATE.equals(taskType)) {
			log.info("Running cron job: " + taskType);
			AbstractTask task = new SessionCostsUpdateTask();
			task.enqueue();

			// TODO add more task dispatching here
		}else {
			log.warn(taskType != null ? "No implementation defined for cron task " + taskType
					: "Parameter <" + TASK_PARAMETER + "> missing in cron url");
		}
	}
}

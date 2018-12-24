package agency.akcom.mmg.sherlock.ui.server.task;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import agency.akcom.mmg.sherlock.ui.server.dao.ImportLogDao;
import agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog;
import agency.akcom.mmg.sherlock.ui.shared.enums.ImportStatus;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("serial")
@Slf4j
public abstract class AbstractTask implements DeferredTask, Serializable {
	/**
	 * Returns a particular key for the task name.
	 *
	 * @return
	 */
	protected abstract String getUniqueKey();

	/**
	 * Generate a unique name for the task
	 *
	 * @return
	 */
	private String getUniqueTaskName() {
		return this.getClass().getSimpleName() + "_" + getUniqueKey() + "_" + System.currentTimeMillis();
	}

	/** Add task to the queue */
	public void enqueue() {
		log.info(ModulesServiceFactory.getModulesService().getVersionHostname(null, null));
		log.info(ModulesServiceFactory.getModulesService().getVersionHostname("ui", null));

		
		getQueue().add(TaskOptions.Builder.withPayload(this)
				.header("Host", ModulesServiceFactory.getModulesService().getVersionHostname(null, null))
				.taskName(getUniqueTaskName()));
	}

	/**
	 * Add task to the queue with a delay in milliseconds specified by the countdown
	 * parameter
	 *
	 * @param countdown
	 */
	public void enqueue(long countdown) {
		log.info(ModulesServiceFactory.getModulesService().getVersionHostname(null, null));
		log.info(ModulesServiceFactory.getModulesService().getVersionHostname("ui", null));

		getQueue().add(TaskOptions.Builder.withPayload(this)
				.header("Host", ModulesServiceFactory.getModulesService().getVersionHostname("ui", null))
				.taskName(getUniqueTaskName()).countdownMillis(countdown));
		// TODO review this workaround with time
		// https://issuetracker.google.com/issues/35896906#comment27
		// https://issuetracker.google.com/issues/35901044
	}

	protected Queue getQueue() {
		return QueueFactory.getDefaultQueue();
	}
	
	/**
	 * Save status to log
	 * @param importLog link to importlog
	 * @param status "true" saves with status SUCCESS, "false" saves status like FAILURE
	 */
	public static void saveImportLog(ImportLog importLog, boolean status) {
		ImportLogDao importLogDao = new ImportLogDao();
		importLog.setEnd(new Date());
		if(status) {
			importLog.setStatus(ImportStatus.SUCCESS);
		} else {
			importLog.setStatus(ImportStatus.FAILURE);
		}
		importLogDao.save(importLog);
	}
}

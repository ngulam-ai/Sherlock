package agency.akcom.mmg.sherlock.ui.server.task;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.server.dao.DspDao;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import agency.akcom.mmg.sherlock.ui.server.dao.ImportLogDao;
import agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog;
import agency.akcom.mmg.sherlock.ui.shared.enums.ImportStatus;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.TopicName;
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

	public static ArrayList<ConfigConnection> getCredentials(Partner partner, ImportLog importLog) {
		DspDao dspdao = new DspDao();
		ArrayList<ConfigConnection> credentialsList;
		try {
			credentialsList = dspdao.getCredentials(Partner.PERFECTAUDIENCE);
			// If got empty list
			if (credentialsList == null) {
				log.warn("Not credentials for " + partner.toString());
				saveImportLog(importLog, true);
				return null;
			}
		} catch (NoSuchFieldException ex) {
			log.warn("Not found credentials for " + partner.toString());
			saveImportLog(importLog, true);
			return null;
		}
		return credentialsList;
	}

    protected static Publisher preparePublisher() {
        TopicName topicName = TopicName.of(agency.akcom.mmg.sherlock.ui.server.options.TaskOptions.Settings.getProjectId(), agency.akcom.mmg.sherlock.ui.server.options.TaskOptions.Settings.getTopicId());
        Publisher publisher = null;
        try {
            publisher = Publisher.newBuilder(topicName).build();
        } catch (IOException e) {
            log.error(e.toString());
        }
        return publisher;
    }
}

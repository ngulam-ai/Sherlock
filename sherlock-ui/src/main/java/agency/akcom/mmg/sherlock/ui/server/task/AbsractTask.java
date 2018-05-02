package agency.akcom.mmg.sherlock.ui.server.task;

import java.io.Serializable;

import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@SuppressWarnings("serial")
public abstract class AbsractTask implements DeferredTask, Serializable {
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
		getQueue().add(TaskOptions.Builder.withPayload(this).taskName(getUniqueTaskName()));
	}

	/**
	 * Add task to the queue with a delay in milliseconds specified by the countdown
	 * parameter
	 *
	 * @param countdown
	 */
	public void enqueue(long countdown) {
		getQueue().add(TaskOptions.Builder.withPayload(this)
				.header("Host", ModulesServiceFactory.getModulesService().getVersionHostname("ui", null))
				.taskName(getUniqueTaskName()).countdownMillis(countdown));
		// TODO review tis workaround with time
		// https://issuetracker.google.com/issues/35896906#comment27
		// https://issuetracker.google.com/issues/35901044
	}

	protected Queue getQueue() {
		return QueueFactory.getDefaultQueue();
	}
}

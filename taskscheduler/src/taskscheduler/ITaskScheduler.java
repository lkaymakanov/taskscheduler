package taskscheduler;

import java.util.List;

/**
 * <pre>
 * The main interface of the scheduler that launches tasks!!!!
 * Designed to replace the old UpdateDaemon in LTF project!!!
 * <b>This is aimed to be built on top of java.util.concurrent package!!!</b>
 * </pre>
 * @author lubo
 *
 */
public interface ITaskScheduler<T extends ITask> {

	/***
	 * Runs the task immediately without placing it in the task Queue!!!
	 */
	public void runTask(T task);
	
	/***
	 * Places the task in the common task Queue and prepares it for execution !!!
	 * @param task
	 */
	public void submitTask(T task);
	
	/**
	 * Returns the task info picked up from the task Queue by task id!!!
	 * @param id
	 * @return
	 */
	public TaskInfo getTaskInfoById(long id);
	
	
	/**
	 * Sets the delay of the Scheduler cycle!!!
	 * Shows how often the tasks should be spawned!!!
	 * @param d
	 * @param timeUnit
	 */
	//public void setDelay(long d, TimeUnit timeUnit);
	
	
	/**
	 * Returns a list with the registered running tasks information!!!
	 * @return
	 */
	public List<TaskInfo> getRegisteredTasksInfo();

	
	/***
	 * Stop a Task by id!!!
	 */
	public void stop(long taskId);
	
	
	/***
	 * Forces rough  termination of the thread executing the task!!!
	 */
	public void kill(long taskId);
	
	
	/**
	 * Stop the scheduler!!!
	 */
	public void stop();	
	
	
	/***
	 * Terminates the scheduler by forcing all running threads to terminate!!!
	 */
	public void kill();
}

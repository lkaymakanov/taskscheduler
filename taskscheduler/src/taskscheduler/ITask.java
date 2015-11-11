package taskscheduler;

/***
 * The main task Interface for tasks to be scheduled by the TaskScheduler!!!
 * A Task is considered dead if it will no longer be run by the scheduler under no circumstances!!!
 * @author lubo
 *
 */
public interface ITask {

	/**
	 * Returns true if this task is to be executed by the scheduler or not!!!
	 * 
	 * @return
	 */
	public boolean isSchedulable();
	
	
	/***
	 * <pre>
	 * Reports the progress of the task if available! 
	 * Aimed at modifying the ITaskProgress field of Task !!!
	 * </pre>
	 */
	public void reportProgress();
	
	
	/***
	 * Returns true if task is still executing!!!
	 * @return
	 */
	public boolean isRunning();
	
	
	/***
	 * Set the cancel pending flag to true to politely stop the thread executing the task!!!
	 * Might be supported or not depending on task implementation!!!
	 */
	public void stop();
	
	
	/***
	 * Forces rough  termination of the thread executing the task!!!
	 * Might be supported or not depending on task implementation!!!
	 */
	public void kill();
	
	
	/***
	 * Returns reference to the Thread that is executing this task!!! 
	 * Aimed at manipulating the thread that launched the task!!!
	 */
	public Thread getCallingThread();
	
	
	
	/**
	 * Registers an object to be notified by the task about task progress !!!
	 */
	//public void registerNotifier(ITaskNotifier notifier);
	
	
	/***
	 * Returns info about the task retrieves a copy of the TaskFields!!!
	 * @return
	 */
	public TaskInfo getTaskInfo();
	
	/**
	 * The unique task identifier!!!
	 * @return
	 */
	public long getTaskId();
	
	
	/**
	 * The name of the task if any!!!
	 * @return
	 */
	public String getTaskName();
	
	
}

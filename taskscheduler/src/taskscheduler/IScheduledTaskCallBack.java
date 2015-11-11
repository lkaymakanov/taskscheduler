package taskscheduler;

/**
 * The interface provided to the task when creating the task!!!
 * @author lubo
 *
 */
public interface IScheduledTaskCallBack {
	/**
	 * The activity that must be done by this task!!!
	 * @param taskProgressSetter - used to set the progress task as it advances!!!
	 * @param taskinfoGetter - get info about the task!!!
	 * @param notifier - An object to be notified about task progress!!!
	 */
	 abstract void doWork(ITaskProgressSetter taskProgressSetter, ITaskInfoGetter taskinfoGetter,  ITaskNotifier notifier);
	 
	 /***
	  * Activity to be done when task completes successfully!!!
	  * @param taskinfoGetter - get info about the task!!!
	  * @param notifier  - An object to be notified about task progress!!!
	  */
	 void OnCompleteSuccessfuly(ITaskInfoGetter taskinfoGetter, ITaskNotifier notifier);
	 
	 /**
	  * Activity to be done when task crashes!!!
	  * @param taskinfoGetter - get info about the task!!!
	  * @param notifier - An object to be notified about task progress!!!
	  * @param e - the exception that occurred!!!
	  */
	 void OnCrash(ITaskInfoGetter taskinfoGetter, ITaskNotifier notifier, Exception e);
}

package taskscheduler;

/**
 * Sets the task progress fields from the from the callback interface IScheduledTaskCallBack provided to the task!!!
 * @author lubo
 *
 */
public interface ITaskProgressSetter {
	public void setTaskProgress(PercentageTaskProgress progress);
}

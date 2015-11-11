package taskscheduler;

/**
 * A interface of the object that is to be notified about the task progress!!!
 * @author lubo
 *
 * @param <P>
 */
public interface ITaskNotifier {
	public void notify(TaskInfo progress);
}

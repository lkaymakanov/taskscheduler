package taskscheduler;

import java.util.concurrent.Callable;

/**
 * A summarized interface of ITask & Callable!!!
 * @author lubo
 *
 * @param <T>
 */
public interface ITaskCallable<T> extends ITask, Callable<T>{

}

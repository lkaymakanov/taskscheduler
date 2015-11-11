package taskscheduler;

import java.util.TimeZone;
import java.util.concurrent.ExecutorService;



/***
 * Utilities for creating Schedulers & executing tasks!!!!
 * Serves as function object!!!
 <pre>
 Example usage:
public class Test {

	public static void main(String args []) {
		//create a task scheduler with a thread pool of 10 threads with 20 seconds delay !!!!
		ThreadPoolTaskScheduler sch = TaskUtils.createThreadPoolTaskScheduler(10, new Frequency(20, TimeUnit.SECONDS));
		//sch.removeTaskFromScheduledList(id);
		
		//create time intervals from - to!!!!
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(new TimeInterval(new Hour(8, 20, 0, 0), new Hour(23, 40, 0,0)));   //the task will execute from 8:20 to 17:40 on the days defined by TaskEnums.MONDAY constants!!!
		
		//create & submit 10 tasks
		for(int i=0; i < 10; i++){
			    ScheduledTaskBuilder bd = ScheduledTask.getScheduledTaskBuilder();
			   //the task will wait 30 milliseconds before the next start time!!!
			   bd.setMinTimeOutSincelastRun(TimeUnit.SECONDS.toMillis(60))                
			   .setTaskScheduleDays(TaskEnums.EVERY_DAY)    //the task will execute every day with arbitrary long interval with value between randomMin and randomMax!!!
			   //The task will wait arbitrary long from 100 seconds to 200 seconds!!! The property minTimeOutSinceLastRun will be replaced by the calculated random interval
			  // .setMinRandomTimeOutSincelastRun(100*1000).setMaxRandomTimeOutSincelastRun(200*1000)             
			   //set the intervals in which the task should run!!!!It might be DateTime interval or TimeInterval!If DateTime interval the task is spawned on particular date & time represented by the Interval milliseconds
			   .setIntervals(intervals);                                
			   ScheduledTask task  =  bd.build(new IScheduledTaskCallBack() {
					public void doWork(ITaskProgressSetter taskProgressSetter, ITaskInfoGetter taskinfoGetter, ITaskNotifier notifier) {
						// TODO Auto-generated method stub
					}
					public void OnCrash(ITaskInfoGetter taskinfoGetter, ITaskNotifier notifier, Exception e) {
						// TODO Auto-generated method stub
					}
					public void OnCompleteSuccessfuly(ITaskInfoGetter taskinfoGetter, ITaskNotifier notifier) {
						// TODO Auto-generated method stub
					}
				});
			sch.submitTask(task);
		}
		
		
		//start scheduler as a daemon or not
		sch.startScheduler(false);
		
	}
}
 </pre>
 * @author lubo
 *
 *
 
 */
public class TaskUtils {

	private TaskUtils(){}
	/**
	 * Creates task scheduler with given delay & timeUnit executing specific tasks!!!
	 * @return
	 */
	/**public static <T extends ITask> ITaskScheduler  createTaskScheduler(Class<T> clazz, long delay, TimeUnit timeUnit){
		return null;
	}*/
	
	/**
	 * Creates a new Task Register for the given tasks!!!!
	 * @param clazz
	 * @return
	 */
	/**public static  <T extends ITask> TaskSchedulerRegister<T>  createTaskSchedulerRegister(){
		return TaskSchedulerRegister.getInstance();
	}	
	
	public static boolean isDateIninterval(long dateTime, Interval i){
		return false;
	}
	
	public static boolean isHourMinuteIninterval(long hmin, Interval i){
		return false;
	}
	
/*	
	*//**
	 * Creates a register for BasicRegisteredTask Tasks!!!
	 * @return
	 *//*
    static  <T extends ScheduledTask> TaskSchedulerRegister<T>  createBasicScheduledTaskRegister(){
		return TaskSchedulerRegister.getInstance();
	}*/
	
	/**
	 * Creates a thread pool scheduler with a task register & maximum threads equal to poolSize!!!!
	 * @param poolSize the number of threads in thread pool
	 * @param delay - the delay of the scheduler
	 * @param frequency - the units of and the amount of the delay seconds, milliseconds, minutes so on...
	 * @return
	 */
	public static ThreadPoolTaskScheduler createThreadPoolTaskScheduler(
			int poolSize, 
			Frequency frequency){
		return createThreadPoolTaskScheduler(poolSize, frequency, "Anonymous ThreadPoolTaskScheduler");
	}
	
	
	/**
	 * Creates a thread pool scheduler with a task register & maximum threads equal to poolSize, specifying the name of the Scheduler!!!!
	 * @param poolSize the number of threads in thread pool
	 * @param delay - the delay of the scheduler
	 * @param frequency - the units of and the amount of the delay seconds, milliseconds, minutes so on...
	 * @return
	 */
	public static ThreadPoolTaskScheduler createThreadPoolTaskScheduler(
			int poolSize, 
			Frequency frequency, String name){
		return new ThreadPoolTaskScheduler(poolSize, frequency, name, null);
	}
	
	/**
	 *  Creates a thread pool scheduler with a task register & maximum threads equal to poolSize, specifying the name and Time Zone of the Scheduler!!!!
	 * @param poolSize the number of threads in thread pool
	 * @param delay - the delay of the scheduler
	 * @param frequency - the units of and the amount of the delay seconds, milliseconds, minutes so on...
	 * @return
	 */
	public static ThreadPoolTaskScheduler createThreadPoolTaskScheduler(
			int poolSize, 
			Frequency frequency, String name, TimeZone timezone){
		return new ThreadPoolTaskScheduler(poolSize, frequency, name, timezone);
	}
	
	/**
	 * Creates a thread pool scheduler with a task register specifying the executor service!
	 * @param executorService - the executor service
	 * @param frequency - the units of and the amount of the delay seconds, milliseconds, minutes so on...
	 * @param name - the name of the scheduler!
	 * @param timezone - timeZone
	 * @return
	 */
	public static ThreadPoolTaskScheduler createThreadPoolTaskScheduler(
			ExecutorService executorService, 
			Frequency frequency, String name, TimeZone timezone){
		return new ThreadPoolTaskScheduler(executorService, frequency, name, timezone);
	}
	
	
	/**
	 * Returns a newly created NamingScheduledTaskContext to store ScheduledTasks under name!!!
	 * And later on to retrieve Tasks based on that name!!!
	 * @return
	 */
	public static NamingScheduledTaskContext createNamingScheduledTaskContext(){
		return new NamingScheduledTaskContext();
	}
	
	/***
	 * Prints to the console the configuration of task at the time of creation!!!
	 */
	public static void printTaskConfiguration(ScheduledTask task){
		task.printTaskConfiguration();
	}
	
	/***
	 * Return the configuration of task at the time of creation as a String!!!
	 */
	public static String getTaskConfiguration(ScheduledTask task){
		return task.getTaskConfiguration();
	}
	
	
	
}

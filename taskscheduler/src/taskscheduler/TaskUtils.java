package taskscheduler;

import java.util.TimeZone;
import java.util.concurrent.ExecutorService;

import taskscheduler.TaskEnums.DAY_OF_WEEK;
import taskscheduler.TaskEnums.MONTH_OF_YEAR;



/***
 * Utilities for creating Schedulers & executing tasks!!!!
 * Serves as function object!!!
 <pre>
 Example usage:
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
	
	
	/**
	 * Obtains the task lock!!
	 * @param task
	 * @return
	 */
	public static ScheduledTaskLock getTaskLock(ScheduledTask task){
		return task.lock;
	}
	
	/**
	 * The String representation of Schedule days!!!
	 * @return
	 */
	public static String getTaskScheduleDays(long taskScheduleDays){
		String	taskScheduleDaysSt = "";
		if(taskScheduleDays == -1 || ((taskScheduleDays & DAY_OF_WEEK.EVERY_DAY.getDay()) == DAY_OF_WEEK.EVERY_DAY.getDay())) taskScheduleDaysSt+=" EVERY_DAY";
		else{
			if((taskScheduleDays & DAY_OF_WEEK.MONDAY.getDay()) != 0) taskScheduleDaysSt+=" MONDAY";
		    if((taskScheduleDays & DAY_OF_WEEK.TUESDAY.getDay()) != 0) taskScheduleDaysSt+=" TUESDAY";
			if((taskScheduleDays & DAY_OF_WEEK.WEDNESDAY.getDay()) != 0) taskScheduleDaysSt+=" WEDNESDAY";
			if((taskScheduleDays & DAY_OF_WEEK.THURSDAY.getDay()) != 0) taskScheduleDaysSt+=" THURSDAY";
			if((taskScheduleDays & DAY_OF_WEEK.FRIDAY.getDay()) != 0) taskScheduleDaysSt+=" FRIDAY";
			if((taskScheduleDays & DAY_OF_WEEK.SATURDAY.getDay()) != 0) taskScheduleDaysSt+=" SATURDAY";
			if((taskScheduleDays & DAY_OF_WEEK.SUNDAY.getDay()) != 0) taskScheduleDaysSt+=" SUNDAY";
		}
		return taskScheduleDaysSt;
	}
	
    /**
	 * The String representation of Schedule monts!!!
	 * @return
	 */
	public static String getTaskScheduleMonths(long taskScheduleMonts){
		String	taskScheduleMontsSt = "";
		taskScheduleMontsSt = "";
		if(taskScheduleMonts == -1 || ((taskScheduleMonts & MONTH_OF_YEAR.EVERY_MONTH.getMont()) == MONTH_OF_YEAR.EVERY_MONTH.getMont())) taskScheduleMontsSt+=" EVERY_MONTH";
		else{
			if((taskScheduleMonts & MONTH_OF_YEAR.JANUARY.getMont()) != 0) taskScheduleMontsSt+=" JANUARY";
		    if((taskScheduleMonts & MONTH_OF_YEAR.FEBRUARY.getMont()) != 0) taskScheduleMontsSt+=" FEBRUARY";
			if((taskScheduleMonts & MONTH_OF_YEAR.MARCH.getMont()) != 0) taskScheduleMontsSt+=" MARCH";
			if((taskScheduleMonts & MONTH_OF_YEAR.APRIL.getMont()) != 0) taskScheduleMontsSt+=" APRIL";
			if((taskScheduleMonts & MONTH_OF_YEAR.MAY.getMont()) != 0) taskScheduleMontsSt+=" MAY";
			if((taskScheduleMonts & MONTH_OF_YEAR.JUNE.getMont()) != 0) taskScheduleMontsSt+=" JUNE";
			if((taskScheduleMonts & MONTH_OF_YEAR.JULY.getMont()) != 0) taskScheduleMontsSt+=" JULY";
			if((taskScheduleMonts & MONTH_OF_YEAR.AUGUST.getMont()) != 0) taskScheduleMontsSt+=" AUGUST";
			if((taskScheduleMonts & MONTH_OF_YEAR.SEPTEMBER.getMont()) != 0) taskScheduleMontsSt+=" SEPTEMBER";
			if((taskScheduleMonts & MONTH_OF_YEAR.OCTOBER.getMont()) != 0) taskScheduleMontsSt+=" OCTOBER";
			if((taskScheduleMonts & MONTH_OF_YEAR.NOVEMBER.getMont()) != 0) taskScheduleMontsSt+=" NOVEMBER";
			if((taskScheduleMonts & MONTH_OF_YEAR.DECEMBER.getMont()) != 0) taskScheduleMontsSt+=" DECEMBER";
		}
		return taskScheduleMontsSt;
	}
	
	
}

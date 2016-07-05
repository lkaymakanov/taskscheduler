package taskscheduler;

import java.util.List;

import taskscheduler.TaskEnums.TASK_PRIORITY;

public interface IScheduledTaskBuilder {
	/**
	 * The task name!!!
	 * @param name
	 * @return
	 */
	public IScheduledTaskBuilder setName(String name);

	/**
	 * An object to be notified about the task!!!
	 * @param notifier
	 * @return
	 */
	public IScheduledTaskBuilder setNotifier(ITaskNotifier notifier) ;
	/**
	 * Determines which day the task should run!!! Must be bitwise or of the constants
	 * TaskEnums.SUNDAY, TaskEnums.MONDAY, TaskEnums.TUESDAY... so on!!!
	 * @param taskSchedulability
	 * @return
	 */
	public IScheduledTaskBuilder setTaskScheduleDays(long taskSchedulability);
	
	/**
	 * Determines which month the task should run!!! Must be bitwise or of the constants
	 * TaskEnums.JANUARY, TaskEnums.FEBRUARY, TaskEnums.MARCH... so on!!!
	 * @param taskSchedulability
	 * @return
	 */
	public IScheduledTaskBuilder setTaskScheduleMonths(long taskScheduleMonths);

	/**
	 * Sets the intervals in which the task should run!!! May be DateTimeInterval or TimeInterval!
	 * If TimeInterval the date is ignored and only time is considered useful!!!
	 * If DateTimeInterval the task is spawned between the date & time in interval!!!
	 * 
	 * @param intervals
	 * @return
	 */
	public IScheduledTaskBuilder setIntervals(List<Interval> intervals) ;
	/***
	 * The minimum timeout in milliseconds the task must wait before next round!!!
	 * @param minTimeOutSincelastRun
	 * @return
	 */
	public IScheduledTaskBuilder setMinTimeOutSincelastRun(long minTimeOutSincelastRun);
	/**
	 * Task priority - NORMAL by default!!!
	 * @param priority
	 * @return
	 */
	public IScheduledTaskBuilder setPriority(TASK_PRIORITY priority) ;


	/**
	 * The mininum value of random milliseconds that the task must wait before next spawn!!!
	 * @param randomMin
	 * @return
	 */
	public IScheduledTaskBuilder setMinRandomTimeOutSincelastRun(long minRandomTimeOutSincelastRun);

	
	/**
	 * The maximum value of random milliseconds that the task must wait before next spawn!!!
	 * @param randomMin
	 * @return
	 */
	public IScheduledTaskBuilder setMaxRandomTimeOutSincelastRun(long maxRandomTimeOutSincelastRun);
	
	
	/**
	 * The maximum rounds the task should make -1 is infinity!!!
	 * @param randomMin
	 * @return
	 */
	public IScheduledTaskBuilder setMaxRounds(long maxRounds);
}

package taskscheduler;


import java.util.ArrayList;
import java.util.List;

import taskscheduler.TaskEnums.TASK_PRIORITY;
import taskscheduler.TaskEnums.TASK_STATUS;

/**
 * The information we can get from a task!!!
 * @author lubo
 *
 */
public final class TaskInfo {
	
	 
	 final TASK_PRIORITY priority;
	 final TASK_STATUS status;
	 final PercentageTaskProgress progress;
	 final String lastError;
	 final long currentTaskRounds;
	 /**The id of the Task!!!*/
	 private long id;
	 /**The name of the Task**/
	 private String name;
	 /**shows how many times the task has been run!!!*/
	 /**The maximum rounds the task should make -1 is infinity!!!!*/
	 final long maxRounds;		   		
	 /***The flags that show which days this task is to be executed.This is sum up of flags MONDAY | TUESDAY... of TaskEnums public constants!!!*/
	 final long scheduleDays;
	 /***The flags that show which days this task is to be executed.This is sum up of flags JANUARY | FEBRUARY... of TaskEnums public constants!!!*/
	 final long scheduleMonths;
	 /**The intervals in which the task should run!!!*/
	 private  List<Interval> intervals;
	 /**The amount of time in milliseconds the task must wait before the next spawn!!!*/
	 final long minTimeOutSincelastRun;
	 /**The time at which the Task has run for the last time!!!*/
	 final long timeOfLastRun;
	 /**The lower bound of minTimeOutSincelastRun in milliseconds calculated as random number!!!*/
	 long minRandomTimeOutSincelastRun;
	 /**The upper bound of minTimeOutSincelastRun in milliseconds calculated as random number!!!
	 * The minTimeOutSincelastRun is calculated as random number only if maxRandomTimeOutSincelastRun is more than 0!!!*/
	 long maxRandomTimeOutSincelastRun;
	 /**taskScheduleDays suitable for print format*/
	 private String taskScheduleDays;    //suitable for print format
	 /**taskScheduleMonts suitable for print format*/
	 private String taskScheduleMonts;    //suitable for print format
	 /**taskSheduleIntervals suitable for print format*/
	 private String taskSheduleIntervals;  //
	 /** The stack trace after a crash!!!*/
	 private String stackTrace;
	 
	 private TaskInfo(long id, String name, List<Interval> intervals,  TASK_PRIORITY priority, TASK_STATUS status, 
			 PercentageTaskProgress progress, 
			 String lastError, 
			 long currentTaskRounds,
			 long maxRounds, long scheduleDays,
			 long scheduleMonths,
			 long minTimeOutSincelastRun,
			 long timeOfLastRun,
			 long minRandomTimeOutSincelastRun,
			 long maxRandomTimeOutSincelastRun,
			 String stackTrace
			 ){
		 this.id = id;
		 this.name = name;
		 this.intervals = new  ArrayList<Interval>();
		 //copy intervals 
		 if(intervals!=null) for(Interval i : intervals){ if(i==null) continue;  this.intervals.add(i.copyInterval());}
		 this.priority = priority;
		 this.status = status;
		 this.progress = progress;
		 this.lastError = lastError;
		 this.currentTaskRounds = currentTaskRounds;
		 this.maxRounds = maxRounds;
		 this.scheduleDays = scheduleDays;
		 this.minTimeOutSincelastRun = minTimeOutSincelastRun;
		 this.timeOfLastRun = timeOfLastRun;
		 this.minRandomTimeOutSincelastRun = minRandomTimeOutSincelastRun;
		 this.maxRandomTimeOutSincelastRun = maxRandomTimeOutSincelastRun;
		 this.stackTrace = stackTrace;
		 this.scheduleMonths = scheduleMonths;
	 }
	
	
	/**
	 * Returns the task priority - default is NORMAL!!!
	 * @return
	 */
	public TASK_PRIORITY getPriority() {
		return priority;
	}

	/***
	 * Returns status of task - submitted, running scheduled, etc....!!!
	 * @return
	 */
	public TASK_STATUS getStatus() {
		return status;
	}

	/**The time at which the Task has run for the last time!!!*/
	public String getLastError() {
		return lastError;
	}

	/**shows how many times the task has been run!!!*/
	public long getCurrentTaskRounds() {
		return currentTaskRounds;
	}

	/**The maximum rounds the task should make -1 is infinity!!!!*/
	public long getMaxRounds() {
		return maxRounds;
	}

	/***The flags that show when this task is to be executed.This is sum up of flags MONDAY | TUESDAY... of TaskEnums public constants!!!*/
	public long getTaskScheduleDays() {
		return scheduleDays;
	}
	
	/***The flags that show when this task is to be executed.This is sum up of flags JANUARY | FEBRUARY... of TaskEnums public constants!!!*/
	public long getTaskScheduleMonths() {
		return scheduleMonths;
	}

	 /**The amount of time in milliseconds the task must wait before the next spawn!!!*/
	public long getMinTimeOutSincelastRun() {
		return minTimeOutSincelastRun;
	}

	/**The time at which the Task has run for the last time!!!*/
	public long getTimeOfLastRun() {
		return timeOfLastRun;
	}

	 /**The lower bound of minTimeOutSincelastRun in milliseconds calculated as random number!!!*/
	public long getMinRandomTimeOutSincelastRun() {
		return minRandomTimeOutSincelastRun;
	}

	/**The upper bound of minTimeOutSincelastRun in milliseconds calculated as random number!!!
	 * The minTimeOutSincelastRun is calculated as random number only if maxRandomTimeOutSincelastRun is more than 0!!!*/
	public long getMaxRandomTimeOutSincelastRun() {
		return maxRandomTimeOutSincelastRun;
	}

	/**The intervals in which the task should run!!!*/
	public List<Interval> getIntervals() {
		return intervals;
	}

	/**The task id*/
	public long getId() {
		return id;
	}

	/**The task name*/
	public String getName() {
		return name;
	}
	
	

	/**The stack trace after a crash*/
	public String getStackTrace() {
		return stackTrace;
	}


	/**
	 * Returns copy of the progress Object associated with this task!!!!
	 * This must return immutable copy instance of the progress of task so it will be thread safe!!!
	 * @return
	 */
	 public PercentageTaskProgress getProgress() {
		return new PercentageTaskProgress(progress.getPercentage(), progress.getStartTime(), progress.getFinishTime());
	}

	 
	 public String toString(){
		 if(taskScheduleDays == null) taskScheduleDays = TaskUtils.getTaskScheduleDays(scheduleDays);
		 if(taskScheduleMonts == null) taskScheduleMonts = TaskUtils.getTaskScheduleMonths(scheduleMonths);
		 return  " id " + id +
				 " name " + (name == null || name.equals("") ? "Anonymous" : name ) +
				 " status = " + status + ", priority = " + priority + ",  lastError = " + lastError+
				 " start time =  " + progress.getStartTime() + " finish time = " + progress.getFinishTime() +
				 " percentage =   " + progress.getPercentage() + 
				 " maxRounds " + ((maxRounds < 0)  ? " INFINITY " : maxRounds ) +
				 " taskSchedulability " + scheduleDays +
				 " minTimeOutSincelastRun " + minTimeOutSincelastRun +
				 " timeOfLastRun " + timeOfLastRun +
				 " minRandomTimeOutSincelastRun "  + minRandomTimeOutSincelastRun+
				 " maxRandomTimeOutSincelastRun "  + maxRandomTimeOutSincelastRun + 
				 " Run intervals:\n " + getTaskSheduleIntervals() + 
		 		 "\nRun on Days " +  taskScheduleDays +
		 		 "\nRun on months " + taskScheduleMonts;
	 }
	 
	 
		

		/***
		 * The String representation of Run intervals - suitable for print!!!
		 * @return
		 */
		public  String getTaskSheduleIntervals(){
			if(taskSheduleIntervals!= null) return taskSheduleIntervals;
			StringBuilder sb = new  StringBuilder();
			if(intervals != null){ sb.append("");
			for(Interval i : intervals){if(i == null) continue; sb.append("    " + i.toString() + "\n");}
			}else { sb.append("No run intervals:\n"); }
			taskSheduleIntervals = sb.toString();
			return taskSheduleIntervals;
		}

	/**
	  * Returns task info for a task!
	  * @author lubo
	  *
	  */
	 public static class TaskInfoBuilder {
		
		private  TASK_PRIORITY priority = TASK_PRIORITY.NORMAL;
		private  TASK_STATUS status = TASK_STATUS.CREATED;
		private  String lastError;
		private  PercentageTaskProgress progress;
		private  long currentTaskRounds;
		private  long id;
		private  String name;
		 /**shows how many times the task has been run!!!*/
		 /**The maximum rounds the task should make -1 is infinity!!!!*/
		private long maxRounds = -1;		   		
		 /***The flags that show which days this task is to be executed.This is sum up of flags MONDAY | TUESDAY... of TaskEnums public constants!!!*/
		private  long scheduleDays;
		 /***The flags that show which days this task is to be executed.This is sum up of flags JANUARY | FEBRUARY... of TaskEnums public constants!!!*/
		private long scheduleMonths;
		 /**The intervals in which the task should run!!!*/
		 //private List<Interval> intervals;
		 /**The amount of time in milliseconds the task must wait before the next spawn!!!*/
		private long minTimeOutSincelastRun;
		 /**The time at which the Task has run for the last time!!!*/
		private long timeOfLastRun;
		 /**The lower bound of minTimeOutSincelastRun in milliseconds calculated as random number!!!*/
		private long minRandomTimeOutSincelastRun;
		 /**The upper bound of minTimeOutSincelastRun in milliseconds calculated as random number!!!
		 * The minTimeOutSincelastRun is calculated as random number only if maxRandomTimeOutSincelastRun is more than 0!!!*/
		private long maxRandomTimeOutSincelastRun;
		/**The intervals in which the task should run!!!*/
		private  List<Interval> intervals;
		/** The stack trace after a crash!!!*/
		private String stackTrace;
	
		public TASK_PRIORITY getPriority() {
			return priority;
		}
		public TaskInfoBuilder setPriority(TASK_PRIORITY priority) {
			this.priority = priority;
			return this;
		}
		public TASK_STATUS getStatus() {
			return status;
		}
		public ITaskProgress getProgress() {
			return progress;
		}
		public TaskInfoBuilder setProgress(PercentageTaskProgress progress) {
			this.progress = progress;
			return this;
		}
		
		public TaskInfoBuilder setStatus(TASK_STATUS status) {
			this.status = status;
			return this;
		}
		
		public TaskInfoBuilder setLastError(String lastError) {
			this.lastError = lastError;
			return this;
		}
		
		public TaskInfoBuilder setCurrentTaskRounds(long currentTaskRounds) {
			this.currentTaskRounds = currentTaskRounds;
			return this;
		}
		
		public TaskInfoBuilder setMaxRounds(long maxRounds) {
			this.maxRounds = maxRounds;
			return this;
		}
		public TaskInfoBuilder setTaskScheduleDays(long scheduleDays) {
			this.scheduleDays = scheduleDays;
			return this;
		}
		public TaskInfoBuilder setMinTimeOutSincelastRun(long minTimeOutSincelastRun) {
			this.minTimeOutSincelastRun = minTimeOutSincelastRun;
			return this;
		}
		public TaskInfoBuilder setTimeOfLastRun(long timeOfLastRun) {
			this.timeOfLastRun = timeOfLastRun;
			return this;
		}
		public TaskInfoBuilder setMinRandomTimeOutSincelastRun(long minRandomTimeOutSincelastRun) {
			this.minRandomTimeOutSincelastRun = minRandomTimeOutSincelastRun;
			return this;
		}
		public TaskInfoBuilder setMaxRandomTimeOutSincelastRun(long maxRandomTimeOutSincelastRun) {
			this.maxRandomTimeOutSincelastRun = maxRandomTimeOutSincelastRun;
			return this;
		}
		
		public TaskInfoBuilder setStackTrace(String stackTrace){
			this.stackTrace = stackTrace;
			return this;
		}
		
		public TaskInfoBuilder setIntervals(List<Interval> intervals) {
			this.intervals = intervals;
			return this;
		}
		
		public TaskInfoBuilder setId(long id) {
			this.id = id;
			return this;
		}
		public TaskInfoBuilder setName(String name) {
			this.name = name;
			return this;
		}
		
		public TaskInfoBuilder setTaskScheduleMonths(long scheduleMonths) {
			this.scheduleMonths = scheduleMonths;
			return this;
		}
		public TaskInfo build(){
			return new TaskInfo(id, (name == null || name.equals("") )? "Anonymous" : name, 
					  intervals, priority, status,  progress, 
					  lastError, currentTaskRounds,
					  maxRounds,  scheduleDays, scheduleMonths,
					  minTimeOutSincelastRun,
					  timeOfLastRun,
					  minRandomTimeOutSincelastRun,
					  maxRandomTimeOutSincelastRun, stackTrace);
		}
		
	 }
	
	
}

package taskscheduler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import taskscheduler.TaskEnums.DAY_OF_WEEK;
import taskscheduler.TaskEnums.TASK_PRIORITY;
import taskscheduler.TaskEnums.TASK_STATUS;

/**
 * <pre>
 * A ScheduledTask that is  registered in ThreadPoolTaskScheduler's register when it runs!!!
 * 
 * The task is  placed in ThreadPoolTaskScheduler's list only if it's submitted to the ThreadPoolTaskScheduler's 
 * by calling the ThreadPoolTaskScheduler's submit method!!!
 * 
 * Otherwise calling runTask on ThreadPoolTaskScheduler will delegate the task to 
 * the ThreadPoolTaskScheduler's internal Executor Service without placing it
 * in the ThreadPoolTaskScheduler task list!!! The task will run only once and not be scheduled any more!!!
 * 
 * When finished or crashed task is removed from the ThreadPoolTaskScheduler's register 
 * but still stays in the ThreadPoolTaskScheduler's task list if submitted by the ThreadPoolTaskScheduler's submit method 
 * and will run as many times as specified by its configuration parameters!!!
 * 
 * </pre>
 * @author lubo
 *
 */
public final class ScheduledTask implements ITaskRunnable {
	/**The id of the Task!!!*/
	private long id;
	/**The name of the Task**/
	private String name;
	/**The status of the Task!!!*/
	private volatile TASK_STATUS status;
	/**The object to be notified by the Task!!!*/
	protected ITaskNotifier notifier;
	/**The priority of the Task!!! - */
	private TASK_PRIORITY priority = TASK_PRIORITY.NORMAL;
	/**The error that is set when the task blows!!!*/
	private String lastError;
	@SuppressWarnings("rawtypes")
	/**The register where task registers itself*/
	private TaskSchedulerRegister register;
	/**Dead register*/
	private DeadScheduledTaskRegister deadRegister;
	/**shows how many times the task has been run!!!*/
	private volatile long currentTaskRounds = 0;    		
	/**The maximum rounds the task should make -1 is infinity!!!!*/
	private volatile long maxRounds = -1;		   		
	/***The flags that show when this task is to be executed.This is sum up of flags MONDAY | TUESDAY... of TaskEnums public constants!!!*/
	private long taskSchedulability = DAY_OF_WEEK.EVERY_DAY.getDay();
	/**Callback that provides the methods to be executed when task is running, completes or blows!!! */
	private IScheduledTaskCallBack callBack;
	/**The intervals in which the task should run!!!*/
	private List<Interval> intervals;
	/**The amount of time in milliseconds the task must wait before the next spawn!!!*/
	private volatile long minTimeOutSincelastRun = 0;
	/**The time at which the Task has run for the last time!!!*/
	private volatile long timeOfLastRun;
	/**The lower bound of minTimeOutSincelastRun in milliseconds calculated as random number!!!*/
	private long minRandomTimeOutSincelastRun;
	/**The upper bound of minTimeOutSincelastRun in milliseconds calculated as random number!!!
	 * The minTimeOutSincelastRun is calculated as random number only if maxRandomTimeOutSincelastRun is more than 0!!!*/
	private long maxRandomTimeOutSincelastRun;
	/**The progress Task has made so far!!!*/
	private PercentageTaskProgress progress = new  PercentageTaskProgress(0, 0, 0);
	/**The ThreadPoolTaskScheduler  that executes this Task!!!*/
	private ThreadPoolTaskScheduler  threadPoolTaskScheduler;
	/**The thread object that is executing this Task!!!*/
	private volatile Thread currentThread;
	/**taskScheduleDays suitable for print format*/
	private String taskScheduleDays;    //suitable for print format
	/**taskSheduleIntervals suitable for print format*/
	private String taskSheduleIntervals;  //
	/**The stack trace after a crash!!!*/
	private String stackTrace;
	
	/***
	 * No arg constructor used only to create the inner Builder objects!!!
	 */
	private ScheduledTask(){
		
	}
	
	
	/**Copy task Constructor*/
	private  ScheduledTask(long id,
			String name, 
			ITaskNotifier notifier, 
			TASK_PRIORITY priority, 
			IScheduledTaskCallBack callBack,
			long taskSchedulability,
		    List<Interval> intervals,
		    long minTimeOutSincelastRun,
		    long maxRounds,
		    long randomMin, long randomMax){
		this.id = id;
		this.notifier = notifier;
		this.name = name;
		this.status = TASK_STATUS.CREATED;
		this.priority = priority;
		this.callBack = callBack;
		this.taskSchedulability = taskSchedulability;
		this.intervals = intervals;
		this.minTimeOutSincelastRun = minTimeOutSincelastRun;
		this.maxRounds = maxRounds;
		this.minRandomTimeOutSincelastRun = randomMin;
		this.maxRandomTimeOutSincelastRun = randomMax;
	}
	
	
	/***
	 * Normal Task constructor used in conjunction with ScheduledTaskBuilder!!!
	 * @param name
	 * @param notifier
	 * @param priority
	 * @param callBack
	 * @param taskSchedulability
	 * @param intervals
	 * @param minTimeOutSincelastRun
	 * @param maxRounds
	 * @param randomMin
	 * @param randomMax
	 */
	private  ScheduledTask(String name, 
			ITaskNotifier notifier, 
			TASK_PRIORITY priority, 
			IScheduledTaskCallBack callBack,
			long taskSchedulability,
		    List<Interval> intervals,
		    long minTimeOutSincelastRun,
		    long maxRounds,
		    long randomMin, long randomMax){
		this(TaskIdGenerator.getNextId(), name, 
				 notifier, 
				 priority, 
				 callBack,
				 taskSchedulability,
			     intervals,
			     minTimeOutSincelastRun,
			     maxRounds,
			     randomMin, randomMax);
		
	}

	
	//==================================== schedulability check functions ============================
	public final boolean isSchedulable() {
		// TODO Auto-generated method stub
		if(isRunning()){
			System.out.println("ScheduledTask with id " + id + " is still running");
			return false;                				//still running
		}
		if(status == TASK_STATUS.DEAD){
			System.out.println("ScheduledTask with id " + id + " is dead...");
			return false;
		}
		return shallRun();                         	//decide or not whether I should run
	}

	private final  boolean shallRun(){
		boolean b = true;
		//proverka dali e v intervalite
		if(intervals != null){
			for(Interval i : intervals){
				if(i == null) continue;
				b = isInInterval(i);
				if(b) break;
			}
			if(!b) return b;
		}
		
		
		//proverka dali e izminal minimalniq time out ot poslednoto puskane
		if(threadPoolTaskScheduler.now < (timeOfLastRun + minTimeOutSincelastRun))b = false;
		
		//proverka dali maximuma na zapuskaniqta  e dostignat
		if(maxRounds > 0 && (currentTaskRounds >= maxRounds)) 	b = false;
		
		//proverka za denq na sedmicata
		if((threadPoolTaskScheduler.day & taskSchedulability) == 0) b = false;
		
		
		return b;
	}
	
	/***
	 * Check if the ThreadPoolTaskScheduler  field now is in DateTime interval! If param Interval i is DateTimeInterval!!
	 * Else check if the ThreadPoolTaskScheduler  field hourNowMillis is Time Interval i if param i is TimeInterval!!!
	 * @param i
	 * @return
	 */
	private boolean isInInterval(Interval i){
		if(i instanceof TimeInterval) { return i.isInInterval(threadPoolTaskScheduler.hourNowMillis);}
		else return   i.isInInterval(threadPoolTaskScheduler.now);
	}
	//==================================== end of schedulability check functions ========================

	/***
	 * Retrieves information about the task!!!
	 */
	public TaskInfo getTaskInfo() {
		// TODO Auto-generated method stub
		return new TaskInfo.TaskInfoBuilder().
				setId(id).setName(name).
				setStatus(status).
				setPriority(priority).
				setCurrentTaskRounds(currentTaskRounds).
				setProgress(progress).setMaxRounds(maxRounds).
				setTaskSchedulability(taskSchedulability).
				setMinTimeOutSincelastRun(minTimeOutSincelastRun).
				setMinRandomTimeOutSincelastRun(minRandomTimeOutSincelastRun).
				setTimeOfLastRun(timeOfLastRun).
				setMaxRandomTimeOutSincelastRun(maxRandomTimeOutSincelastRun).
				setIntervals(intervals).
				setLastError(lastError).setStackTrace(stackTrace).build();
	}
	
	
	public long getTaskId(){
		return id;
	}
	
	public String getTaskName(){
		return name;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		currentThread = Thread.currentThread();
		try {
			// TODO Auto-generated method stub
			timeOfLastRun = threadPoolTaskScheduler.now;
			status = TASK_STATUS.RUNNING;
			System.out.println("ScheduledTask with id " + id + " started successfuly at " + new Date() );
			
			//register task
			register.register(id, this);
			
			//the work to be done in thread
			callBack.doWork(new ITaskProgressSetter() {
				@Override
				public void setTaskProgress(PercentageTaskProgress progress) {
					// TODO Auto-generated method stub
					ScheduledTask.this.progress = progress;
				}
			}, new ITaskInfoGetter() {
				@Override
				public TaskInfo getTaskIno() {
					// TODO Auto-generated method stub
					return getTaskInfo();
				}
			}, notifier);
			
			//notify object 
			if(notifier !=null)  notifier.notify(getTaskInfo());
		
			//calculate arbitrary long delay before next round!!!
			if(maxRandomTimeOutSincelastRun > 0) minTimeOutSincelastRun = getRandomIninterval(minRandomTimeOutSincelastRun, maxRandomTimeOutSincelastRun);
			
			status = TASK_STATUS.COMPLETED;
			System.out.println("ScheduledTask  with id " + id + " completed successfuly at " +  new Date()   );
			
			//report successful complete
			callBack.OnCompleteSuccessfuly(new ITaskInfoGetter() {
				@Override
				public TaskInfo getTaskIno() {
					// TODO Auto-generated method stub
					return getTaskInfo();
				}
			}, notifier);
			
			System.out.println(getTaskInfo().toString());
			
		} catch (Exception e) {
			try{
				// TODO Auto-generated catch block
				e.printStackTrace();
				status = TASK_STATUS.CRASHED;
				System.out.println("ScheduledTask  with id " + id + " crashed at " +  new Date()  );
				lastError = e.toString() +  e.getMessage();
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				stackTrace = sw.toString(); 
				//report crash
				callBack.OnCrash(new ITaskInfoGetter() {
					@Override
					public TaskInfo getTaskIno() {
						// TODO Auto-generated method stub
						return getTaskInfo();
					}
				}, notifier, e);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}finally{
			incrementCurrentTaskRounds();
			register.unRegister(id);   //unregister task
			
			if(maxRounds > 0 && (currentTaskRounds >= maxRounds)){
				status = TASK_STATUS.DEAD;
				deadRegister.register(this, this);
			}
			
		}
	}

	/**Calculates the random interval that the task must wait before next round!!! */
	private long getRandomIninterval(long min, long max){
		if(min > max) {
			long l = min;
			min = max;
			max = l;
		}
		long i = ((long)(Math.random()*(max - min)) + min);
		return i;
	}
	


	private void incrementCurrentTaskRounds() {
		this.currentTaskRounds+=1;
	}
	
	public void reportProgress() {
		// TODO Auto-generated method stub
		if(notifier!=null) notifier.notify(getTaskInfo());
	}

	public boolean isRunning() {
		// TODO Auto-generated method stub
		return (register.isRegistered(id) || (status != null && (status == TASK_STATUS.RUNNING)));
	}

	public void stop() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Stop is not suported on ScheduledTask yet...");
	}

	public void kill() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Kill is not suported on ScheduledTask yet...");
	}

	public Thread getCallingThread() {
		// TODO Auto-generated method stub
		return currentThread;
	}
	
	/***
	 * Sets the register to the task so it can register itself before running & unregister itself when finished or crashed!!!
	 * @param register
	 */
	<T extends ITask> void  setRegister(TaskSchedulerRegister<T> register){
		this.register = register;
	}
	
	void  setDeadRegister(DeadScheduledTaskRegister register){
		this.deadRegister = register;
	}
	
	
	void setThreadPoolTaskScheduler(ThreadPoolTaskScheduler threadPoolTaskScheduler){
		this.threadPoolTaskScheduler = threadPoolTaskScheduler;
	}
	
	
	
	private  class ScheduledTaskBuilderCommon implements IScheduledTaskBuilder{
		protected long maxRounds = -1;		   		//Long.MAX_VALUE;    //the maximum rounds the task should make -1 is infinity!!!!
		protected String name;
		protected ITaskNotifier notifier;
		protected long taskSchedulability = -1;   	//the flags that show when this task is to be executed.This is sum up of flags MONDAY | TUESDAY... of TaskEnums public constants!!!
		protected List<Interval> intervals;
		protected long minTimeOutSincelastRun = 0;
		protected TASK_PRIORITY priority = TASK_PRIORITY.NORMAL;
		protected long minRandomTimeOutSincelastRun;
		protected long maxRandomTimeOutSincelastRun;
		protected long id;
		
		/**
		 * The task name!!!
		 * @param name
		 * @return
		 */
		public IScheduledTaskBuilder setName(String name) {
			this.name = name;
			return this;
		}

		/**
		 * An object to be notified about the task!!!
		 * @param notifier
		 * @return
		 */
		public IScheduledTaskBuilder setNotifier(ITaskNotifier notifier) {
			this.notifier = notifier;
			return this;
		}
		/**
		 * Determines when the task should run!!! Must be bitwise or of the constants
		 * TaskEnums.SUNDAY, TaskEnums.MONDAY, TaskEnums.TUESDAY... so on!!!
		 * @param taskSchedulability
		 * @return
		 */
		public IScheduledTaskBuilder setTaskScheduleDays(long taskSchedulability) {
			this.taskSchedulability = taskSchedulability;
			return this;
		}

		/**
		 * Sets the intervals in which the task should run!!! May be DateTimeInterval or TimeInterval!
		 * If TimeInterval the date is ignored and only time is considered useful!!!
		 * If DateTimeInterval the task is spawned between the date & time in interval!!!
		 * 
		 * @param intervals
		 * @return
		 */
		public IScheduledTaskBuilder setIntervals(List<Interval> intervals) {
			this.intervals = intervals;
			return this;
		}
		/***
		 * The minimum timeout in milliseconds the task must wait before next round!!!
		 * @param minTimeOutSincelastRun
		 * @return
		 */
		public IScheduledTaskBuilder setMinTimeOutSincelastRun(long minTimeOutSincelastRun) {
			this.minTimeOutSincelastRun = minTimeOutSincelastRun;
			return this;
		}
		/**
		 * Task priority - NORMAL by default!!!
		 * @param priority
		 * @return
		 */
		public IScheduledTaskBuilder setPriority(TASK_PRIORITY priority) {
			this.priority = priority;
			return this;
		}


		/**
		 * The mininum value of random milliseconds that the task must wait before next spawn!!!
		 * @param randomMin
		 * @return
		 */
		public IScheduledTaskBuilder setMinRandomTimeOutSincelastRun(long minRandomTimeOutSincelastRun) {
			this.minRandomTimeOutSincelastRun = minRandomTimeOutSincelastRun;
			return this;
		}

		
		/**
		 * The maximum value of random milliseconds that the task must wait before next spawn!!!
		 * @param randomMin
		 * @return
		 */
		public IScheduledTaskBuilder setMaxRandomTimeOutSincelastRun(long maxRandomTimeOutSincelastRun) {
			this.maxRandomTimeOutSincelastRun = maxRandomTimeOutSincelastRun;
			return this;
		}
		
		/**
		 * The maximum rounds the task should make -1 is infinity!!!
		 * @param randomMin
		 * @return
		 */
		public IScheduledTaskBuilder setMaxRounds(long maxRounds) {
			this.maxRounds = maxRounds;
			return this;
		}
		
	}
	
	/***
	 * A builder that creates a ScheduledTask based on attributes specified to the builder!!!
	 * @author lubo
	 *
	 */
	public final  class ScheduledTaskBuilder extends ScheduledTaskBuilderCommon  {
		
		private ScheduledTaskBuilder(){}
		
		/**Retrieves a newly created ScheduledTask using the attributes set to  this ScheduledTaskBuilder!!!*/
		public  ScheduledTask  build(IScheduledTaskCallBack taskCallBack) {
			if(taskCallBack == null) throw new RuntimeException("IScheduledTaskCallBack must be provided to build method of ScheduledTaskBuilder....");
			return new ScheduledTask(
					 TaskIdGenerator.getNextId(),
					 name, 
					 notifier, 
					 priority, 
					 taskCallBack,
					 taskSchedulability,
				     intervals,
				     minTimeOutSincelastRun, 
				     maxRounds,
				     minRandomTimeOutSincelastRun,
				     maxRandomTimeOutSincelastRun);
		}
		
	}
	
	
	/**
	 * Builder used to copy task attributes into a new ScheduledTask!!! The new ScheduledTask
	 * will have the same Id as the copied one!!! 
	 * Setting attributes to Builder will reload the newly created ScheduledTask attributes!!! 
	 * Used to reload the settings of the a ScheduledTask making copy of its attributes & 
	 * replacing the ones we want by setting them in CopyScheduledTaskBuilder !!!
	 * @param task
	 * @return
	 */
	public final   class CopyScheduledTaskBuilder extends ScheduledTaskBuilderCommon {
		private CopyScheduledTaskBuilder(){}
		
		CopyScheduledTaskBuilder copyTaskBuilder(ScheduledTask task){
			CopyScheduledTaskBuilder bd = new CopyScheduledTaskBuilder();
			bd.id = task.id;
			bd.notifier = task.notifier;
			bd.name = task.name;
			bd.priority = task.priority;
			bd.taskSchedulability = task.taskSchedulability;
			bd.intervals = task.intervals;
			bd.minTimeOutSincelastRun = task.minTimeOutSincelastRun;
			bd.maxRounds = task.maxRounds;
			bd.minRandomTimeOutSincelastRun = task.minRandomTimeOutSincelastRun;
			bd.maxRandomTimeOutSincelastRun = task.maxRandomTimeOutSincelastRun;
			return bd;
		}
		
		/**
		 * Retrieves a copy of the ScheduledTask attributes set to this CopyScheduledTaskBuilder!!!
		 * @param taskCallBack
		 * @return
		 */
		public  ScheduledTask  copy(IScheduledTaskCallBack taskCallBack){
			if(taskCallBack == null) throw new RuntimeException("IScheduledTaskCallBack must be provided to build method of CopyScheduledTaskBuilder....");
			return new ScheduledTask(
					 id,
					 name, 
					 notifier, 
					 priority, 
					 taskCallBack,
					 taskSchedulability,
				     intervals,
				     minTimeOutSincelastRun, 
				     maxRounds,
				     minRandomTimeOutSincelastRun, 
				     maxRandomTimeOutSincelastRun);
		}
	}
	
	/**
	 * Gets a Builder used to copy task attributes into a new ScheduledTask!!! The new ScheduledTask
	 * will have the same Id as the copied one!!! 
	 * Setting attributes to Builder will reload the newly created ScheduledTask attributes!!! 
	 * Used to reload the settings of the a ScheduledTask making copy of its attributes & 
	 * replacing the ones we want by setting them in CopyScheduledTaskBuilder !!!
	 * @param task
	 * @return
	 */
	public static CopyScheduledTaskBuilder getCopyScheduledTaskBuilder(ScheduledTask task){
		CopyScheduledTaskBuilder bd = new  ScheduledTask().new CopyScheduledTaskBuilder().copyTaskBuilder(task);
		return bd;
	}
	
	/**
	 * Gets a builder that creates a ScheduledTask based on attributes specified to the builder!!!
	 * @return
	 */
	public static ScheduledTaskBuilder getScheduledTaskBuilder(){
		ScheduledTaskBuilder bd = new  ScheduledTask().new ScheduledTaskBuilder();
		return bd;
	}
	
	/***
	 * Prints to the console the configuration of task at the time of creation!!!
	 */
	void printTaskConfiguration(){
		System.out.print(getTaskConfiguration());
	}
	
	/***
	 * Return the configuration of task at the time of creation as a String!!!
	 */
	String getTaskConfiguration(){
		StringBuilder sb = new  StringBuilder();
		sb.append("\n");
		sb.append("+============================================== CONFIG INFO FOR TASK  ========================================================+\n");
		sb.append("ID: " + id + "\n");
		sb.append("Name: " + ((name==null || name.equals("")) ? "Anonymous" : name) + "\n");
		sb.append("Task Priority: " + priority + "\n");
		sb.append("Max Task Rounds: " + (maxRounds == -1 ? "Infinity": maxRounds) + "\n");
		String delay=""+minTimeOutSincelastRun + " milliseconds";
		if(maxRandomTimeOutSincelastRun > 0){delay= " from  " + minRandomTimeOutSincelastRun + " to " + maxRandomTimeOutSincelastRun + " milliseconds"; }
		sb.append("Min Delay Between Runs: " + delay + "\n");
		sb.append(getRunintervals());
		String runDays = "Run on Days: ";
		runDays += getTaskSheduleDays();
		sb.append(runDays + "\n");
		sb.append("+=============================================================================================================================+\n");
		sb.append("\n");
		return sb.toString();
	}
	
	
	/**
	 * The String representation of Schedule days!!!
	 * @return
	 */
	String getTaskSheduleDays(){
		if(taskScheduleDays !=null) return taskScheduleDays;
		taskScheduleDays = "";
		if(taskSchedulability == -1 || ((taskSchedulability & DAY_OF_WEEK.EVERY_DAY.getDay()) == DAY_OF_WEEK.EVERY_DAY.getDay())) taskScheduleDays+=" EVERY_DAY";
		else{
			if((taskSchedulability & DAY_OF_WEEK.MONDAY.getDay()) != 0) taskScheduleDays+=" MONDAY";
		    if((taskSchedulability & DAY_OF_WEEK.TUESDAY.getDay()) != 0) taskScheduleDays+=" TUESDAY";
			if((taskSchedulability & DAY_OF_WEEK.WEDNESDAY.getDay()) != 0) taskScheduleDays+=" WEDNESDAY";
			if((taskSchedulability & DAY_OF_WEEK.THURSDAY.getDay()) != 0) taskScheduleDays+=" THURSDAY";
			if((taskSchedulability & DAY_OF_WEEK.FRIDAY.getDay()) != 0) taskScheduleDays+=" FRIDAY";
			if((taskSchedulability & DAY_OF_WEEK.SATURDAY.getDay()) != 0) taskScheduleDays+=" SATURDAY";
			if((taskSchedulability & DAY_OF_WEEK.SUNDAY.getDay()) != 0) taskScheduleDays+=" SUNDAY";
		}
		return taskScheduleDays;
	}
	
	/***
	 * The String representation of Run intervals - suitable for print!!!
	 * @return
	 */
	String getRunintervals(){
		if(taskSheduleIntervals!= null) return taskSheduleIntervals;
		StringBuilder sb = new  StringBuilder();
		if(intervals != null){ sb.append("Run Intervals: \n");
		for(Interval i : intervals){ sb.append("    " + i.toString() + "\n");}
		}else { sb.append("No run intervals:\n"); }
		taskSheduleIntervals = sb.toString();
		return taskSheduleIntervals;
	}
	
}

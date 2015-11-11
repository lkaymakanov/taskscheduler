package taskscheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import taskscheduler.TaskEnums.TASK_PRIORITY;

/***
 * <pre>
 * Creates a scheduler that utilizes thread pool & registers the running tasks!!!
 * When the task finishes or blows it's removed from the scheduler's register!!!
 * </pre>
 * @author lubo
 *
 */
public final  class ThreadPoolTaskScheduler implements ITaskScheduler<ScheduledTask> {
	/**The thread pool executor service that the scheduler uses to spawn task threads!!!*/
	private ExecutorService executorService;
	/**The register that the Scheduler uses to register the running tasks!!!*/
	private TaskSchedulerRegister<ScheduledTask> register; 
	/**The register with the dead tasks !!!*/
	private DeadScheduledTaskRegister deadTasksRegister = DeadScheduledTaskRegister.getInstance();
	/**The frequency of the scheduler!!!*/
	private Frequency frequency;
	/**The delay of the scheduler in milliseconds!!! Initial value is 1 */
	private long delayInMilliseconds = 1; 				   
	/**The number of cycles that the scheduler has done so far!!!**/
	private AtomicLong currentRounds = new  AtomicLong();   
	/** The list with the tasks!!!*/
	private List<LinkedHashMap<String, ScheduledTask>>  taskList = new ArrayList<LinkedHashMap<String, ScheduledTask>>();
	/** The time stamp captured just before spawning tasks in list...Actually the milliseconds since 01.01.1970*/
	long now;
	/** The hour in format h:mm:ss:milliseconds captured just before spawning tasks in list...*/
	Hour hourNow;
	/** The hour in milliseconds captured just before spawning tasks in list...*/
	long hourNowMillis;
	/**Flag representing the day of week*/
	long day;
	/** The Daemon thread that starts the tasks!!!*/
	private Thread schedulerDaemon = null;
	/**The time the scheduler started!!!*/
	private  long startTime;
	/**The scheduler is stopping*/
	private volatile Boolean stopping = false;
	/**Scheduler name*/
	private String name;
	/***The milliseconds in 24 hours*/
	private final static long $24H_Const = 24*3600*1000;  //the milliseconds in 24 hours
	/**Twice the delay of the scheduler...*/
	private long twoTimesDelay;
	/** Specifies the time zone of Scheduler!!!  Default is Europe/Sofia*/
	private TimeZone timeZone;
	
	
	ThreadPoolTaskScheduler(ExecutorService executorService, Frequency frequency2, String name, TimeZone timeZone){
		this.executorService  = executorService;
		this.register  =   TaskSchedulerRegister.getInstance();
		this.frequency = frequency2;
		this.delayInMilliseconds = frequency.getTimeUnit().toMillis(frequency.getDelay());
		currentRounds.set(1);
		taskList.add(new LinkedHashMap<String, ScheduledTask>());  //high priority list
		taskList.add(new LinkedHashMap<String, ScheduledTask>());  //normal priority list
		this.name = name;
		this.twoTimesDelay = 2*delayInMilliseconds;
		this.timeZone = timeZone;
	}
	
	
	ThreadPoolTaskScheduler(int poolSize, Frequency frequency2, String name, TimeZone timeZone) {
		this(Executors.newFixedThreadPool(poolSize), frequency2, name, timeZone);
	}


	private class  Daemon implements Runnable{
		public void run() {
			// TODO Auto-generated method stub
			rotateDaemon();
		}
	}
	
	
	
	private void rotateDaemon(){
		while(true){
			try{
				if(stopping) break;
				
				//get the time now as milliseconds & 
				now = System.currentTimeMillis();
				if(timeZone == null) timeZone = TimeZone.getTimeZone("Europe/Sofia");
				long offset = now + timeZone.getOffset(now);   // This is offset from GMT!
				
				//System.out.println("Now is  ======== " + now);
				int hours = (int)(((offset)/3600000)%24)%24;  
				int minutes = (int)(offset/60000)%60;
				int seconds = (int)((offset/1000)%60);
				int millis = (int)(offset%1000);
				hourNow = new  Hour(hours, minutes, seconds, millis);
				hourNowMillis = hourNow.toMillis();
				
				//refresh day of week
				if(hourNowMillis + twoTimesDelay >= $24H_Const){
					day =  getDayOfWeek();
					System.out.println("Day of week = " + TaskEnums.DAY_OF_WEEK.toDayOfWeek(day));
				}
				
				//remove dead tasks
				removeDeadTasks();
				
				synchronized (executorService) {
					//execute high priority tasks first
					Iterator<Entry<String, ScheduledTask>> itr = null;
					synchronized (taskList) {
						for(LinkedHashMap<String, ScheduledTask> onePriorityTasks: taskList){
							itr = onePriorityTasks.entrySet().iterator();
							while(itr.hasNext()) {
								ScheduledTask task = itr.next().getValue();
						         if(task.isSchedulable()) executorService.submit(task);
					        }
						}
					}
				}
				//pause before the next round!!!!!!!!
				Thread.sleep(delayInMilliseconds);
			}catch(Exception e){
				System.out.println("Exception occurred in ThreadPoolTaskScheduler  " + e.getMessage());
			}finally{
				currentRounds.set(currentRounds.get()+1);
			}
		}
	}
	
	/**
	 * Creates the Daemon & starts the daemon Thread!!!
	 */
	public void startScheduler(boolean isDaemon){
		synchronized (this) {
			if(schedulerDaemon == null || !schedulerDaemon.isAlive()){
				schedulerDaemon = new Thread(new Daemon());
				schedulerDaemon.setDaemon(isDaemon);
				startTime = new Date().getTime();
				day = getDayOfWeek();
				schedulerDaemon.start();
			}
		}
	}

	
	public void runTask(ScheduledTask task) {
		// TODO Auto-generated method stub
		task.setRegister(register);
		task.setThreadPoolTaskScheduler(this);
		synchronized (executorService) {
			executorService.submit(task);
		}
	}

	
	
	

	public void submitTask(ScheduledTask task) {
		// TODO Auto-generated method stub
		task.setRegister(register);
		task.setDeadRegister(deadTasksRegister);
		task.setThreadPoolTaskScheduler(this);
		synchronized (taskList) {
			if(task.getTaskInfo().getPriority() == null ||task.getTaskInfo().getPriority() == TASK_PRIORITY.NORMAL){
				taskList.get(1).put(String.valueOf(task.getTaskId()), task);     //place in the normal priority list
			}else{
				taskList.get(0).put(String.valueOf(task.getTaskId()), task);     //place in the high priority list
			}
		}
	}
	
	/***
	 * Removes the task from the scheduled task list!!!
	 * @param id
	 */
	public void removeTaskFromScheduledList(long id){
		synchronized (taskList) {
			taskList.get(0).remove(String.valueOf(id));
			taskList.get(1).remove(String.valueOf(id));
		}
	}
	
	/**
	 * The name of the scheduler!!!
	 * @return
	 */
	public String getName() {
		return name;
	}

	/***
	 * Retrieves info about a Task by id!!!
	 */
	public TaskInfo getTaskInfoById(long id) {
		// TODO Auto-generated method stub
		return register.getTaskInfo(id);
	}

	/***
	 * Get all the registered Tasks Info!!!
	 */
	public List<TaskInfo> getRegisteredTasksInfo() {
		// TODO Auto-generated method stub
		return register.getRegisteredTasksInfo();
	}

	/**
	 * Gets info about the scheduled tasks in the schedulers queue!!!
	 * @return
	 */
	public List<TaskInfo>  getScheduledTasksInfo(){
		List<TaskInfo> l = new  ArrayList<TaskInfo>();
		synchronized (taskList) {
			LinkedHashMap<String, ScheduledTask> l0 = taskList.get(0);
			LinkedHashMap<String, ScheduledTask> l1 = taskList.get(1);
			Collection<ScheduledTask> st0 =  l0.values();
			Collection<ScheduledTask> st1 =  l1.values();
			for(ScheduledTask t: st0 ){
				l.add(t.getTaskInfo());
			}
			for(ScheduledTask t1: st1 ){
				l.add(t1.getTaskInfo());
			}
		}
		return l;
	}

	public void stop(long id) {
		// TODO Auto-generated method stub
		register.getTaskById(id).stop();
	}

	public void kill(long id) {
		// TODO Auto-generated method stub
		register.getTaskById(id).kill();
	}

	public Frequency getFrequency() {
		return frequency;
	}

	/***
	 * Retrieve the schedulers cycles!!!
	 * @return
	 */
	public long getCurrentRounds() {
		return currentRounds.get();
	}
	
	/**
	 * Retrieve the scheduler start time!!!!
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * Calculate day of Week to conform to the TaskEnums Day Constants!!!!
	 * @return
	 */
	private long getDayOfWeek(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return 1 << (c.get(Calendar.DAY_OF_WEEK) - 1);  //convert to TaskEnum Constants
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		synchronized (stopping) {
			if(stopping) return;
			System.out.println("Thread with id = " + Thread.currentThread().getId() + "  is stopping the Scheduler...");
			stopping = true;
			executorService.shutdown();
			schedulerDaemon = null;
		}
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		synchronized (stopping) {
			if(stopping) return;
			System.out.println("Thread with id = " + Thread.currentThread().getId() + "  is stopping the Scheduler...");
			stopping = true;
			executorService.shutdownNow();
			schedulerDaemon = null;
		}
	}
	
	private void removeDeadTasks(){
		//remove dead tasks
	    for(ScheduledTask deadTask:	deadTasksRegister.getDeadScheduledTasks()){
	    	System.out.println("Removing dead task with id " + deadTask.getTaskId());
	    	ScheduledTask ref0 =   taskList.get(0).get(String.valueOf(deadTask.getTaskId()));
	    	ScheduledTask ref1 =  taskList.get(1).get(String.valueOf(deadTask.getTaskId()));
	    	if(ref0 != null && ref0 == deadTask) taskList.get(0).remove(String.valueOf(deadTask.getTaskId()));
	    	if(ref1 != null && ref1 == deadTask) taskList.get(1).remove(String.valueOf(deadTask.getTaskId()));
	    }
	    	
	    //clear dead task register
	    deadTasksRegister.clear();
	}

	
	
}

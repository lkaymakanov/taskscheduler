package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;






import taskscheduler.Frequency;
import taskscheduler.Hour;
import taskscheduler.IScheduledTaskCallBack;
import taskscheduler.ITaskInfoGetter;
import taskscheduler.ITaskNotifier;
import taskscheduler.ITaskProgressSetter;
import taskscheduler.Interval;
import taskscheduler.NamingScheduledTaskContext;
import taskscheduler.ScheduledTask;
import taskscheduler.ScheduledTask.CopyScheduledTaskBuilder;
import taskscheduler.ScheduledTask.ScheduledTaskBuilder;
import taskscheduler.TaskEnums.DAY_OF_WEEK;
import taskscheduler.TaskEnums.MONTH_OF_YEAR;
import taskscheduler.TaskUtils;
import taskscheduler.ThreadPoolTaskScheduler;
import taskscheduler.TimeInterval;

public class Test {

	public static void main(String args []) {
		//create a task scheduler with a thread pool of 10 threads with 10 seconds delay !!!!
		ThreadPoolTaskScheduler sch = TaskUtils.createThreadPoolTaskScheduler(10, new Frequency(5, TimeUnit.SECONDS));
		//sch.removeTaskFromScheduledList(id);
		
		TimeZone c = TimeZone.getTimeZone("UTC");
		
		Date d = new Date();
		TimeZone timezone = TimeZone.getTimeZone("Europe/Sofia");
		int offset = timezone.getOffset(d.getTime());
		//int offset = d.getTimezoneOffset();
		System.out.println(d.getTimezoneOffset());
		//create time intervals from - to!!!!
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(new TimeInterval(new Hour(8, 20, 0, 0), new Hour(23, 59, 0,0)));   //the task will execute from 8:20 to 17:40 on the days defined by TaskEnums.MONDAY constants!!!
		intervals.add(new TimeInterval(new Hour(0, 0, 0, 0), new Hour(4, 40, 0,0)));   //the task will execute from 8:20 to 17:40 on the days defined by TaskEnums.MONDAY constants!!!
		
		intervals.add(new TimeInterval(new Hour(5, 20, 0, 0), new Hour(9, 40, 0,0)));   //the task will execute from 8:20 to 17:40 on the days defined by TaskEnums.MONDAY constants!!!
		
		ScheduledTask task = null;
		
		NamingScheduledTaskContext ct =  TaskUtils.createNamingScheduledTaskContext();
		 
		//create & submit 10 tasks
		for(int i=0; i < 1; i++){
			   ScheduledTaskBuilder bd = ScheduledTask.getScheduledTaskBuilder();
			   //the task will wait 30 milliseconds before the next start time!!!
			   bd.setMinTimeOutSincelastRun(TimeUnit.SECONDS.toMillis(120))     .setTaskScheduleMonths(MONTH_OF_YEAR.APRIL.getMont())         
			   .setTaskScheduleDays(DAY_OF_WEEK.MONDAY.getDay() | DAY_OF_WEEK.SATURDAY.getDay() | DAY_OF_WEEK.SUNDAY.getDay())   //the task will execute every day with arbitrary long interval with value between randomMin and randomMax!!!
			   //The task will wait arbitrary long from 100 seconds to 200 seconds!!! The property minTimeOutSinceLastRun will be replaced by the calculated random interval
			   .setMinRandomTimeOutSincelastRun(100*1000).setMaxRandomTimeOutSincelastRun(200*1000)             
			   //set the intervals in which the task should run!!!!It might be DateTime interval or TimeInterval!If DateTime interval the task is spawned on particular date & time represented by the Interval milliseconds
			   .setName("replace").setIntervals(intervals).setMaxRounds(123);                                
			   task  =  bd.build(new IScheduledTaskCallBack() {
					public void doWork(ITaskProgressSetter taskProgressSetter, ITaskInfoGetter taskinfoGetter, ITaskNotifier notifier) {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(5*1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					public void OnCrash(ITaskInfoGetter taskinfoGetter, ITaskNotifier notifier, Exception e) {
						// TODO Auto-generated method stub
					}
					public void OnCompleteSuccessfuly(ITaskInfoGetter taskinfoGetter, ITaskNotifier notifier) {
						// TODO Auto-generated method stub
					}
				});
			TaskUtils.printTaskConfiguration(task);
			System.out.println(task.getTaskInfo());
			//register in context
			ct.register(task.getTaskName(), task);
			sch.submitTask(task);
		}
		
		
		 class KillScheduler implements IScheduledTaskCallBack{
			 
			 private ThreadPoolTaskScheduler sch;
			 
			 KillScheduler(ThreadPoolTaskScheduler  sch){
				 this.sch = sch;
			 }

			public void doWork(ITaskProgressSetter taskProgressSetter,
					ITaskInfoGetter taskinfoGetter, ITaskNotifier notifier) {
				// TODO Auto-generated method stub
				sch.kill();
			}

			public void OnCompleteSuccessfuly(ITaskInfoGetter taskinfoGetter,
					ITaskNotifier notifier) {
				// TODO Auto-generated method stub
				
			}

			public void OnCrash(ITaskInfoGetter taskinfoGetter,
					ITaskNotifier notifier, Exception e) {
				// TODO Auto-generated method stub
				
			}
			
		}
		 
		 
		 
		 
        class ReplaceTaskThread implements IScheduledTaskCallBack{
			 
			 private ThreadPoolTaskScheduler sch;
			 private ScheduledTask task;
			 private NamingScheduledTaskContext ct;
			 
			 ReplaceTaskThread( ThreadPoolTaskScheduler  sch, ScheduledTask task, NamingScheduledTaskContext ct){
				 this.sch = sch;
				 this.task = task;
				 this.ct = ct;
				 
			 }

			public void doWork(ITaskProgressSetter taskProgressSetter,
					ITaskInfoGetter taskinfoGetter, ITaskNotifier notifier) {
				// TODO Auto-generated method stub
				CopyScheduledTaskBuilder bd = ScheduledTask.getCopyScheduledTaskBuilder(task);
						bd.setMinTimeOutSincelastRun(TimeUnit.MINUTES.toMillis(5));
						bd.setMinRandomTimeOutSincelastRun(0).setMaxRandomTimeOutSincelastRun(0);
				ScheduledTask newTask = 		bd.copy(new IScheduledTaskCallBack() {
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
						
				System.out.println("copied task config");
				TaskUtils.printTaskConfiguration(newTask);
				ct.replaceAndSubmit(newTask, sch);
			}

			public void OnCompleteSuccessfuly(ITaskInfoGetter taskinfoGetter,
					ITaskNotifier notifier) {
				// TODO Auto-generated method stub
				
			}

			public void OnCrash(ITaskInfoGetter taskinfoGetter,
					ITaskNotifier notifier, Exception e) {
				// TODO Auto-generated method stub
				
			}
			
		}
        
        
		ScheduledTaskBuilder bd = ScheduledTask.getScheduledTaskBuilder();
		bd.setMaxRounds(1);
		ScheduledTask t =  bd.build(new ReplaceTaskThread(sch, task ,ct));
		sch.submitTask(t);
		
		//start scheduler as a daemon or not
		sch.startScheduler(false);
		
	}
}

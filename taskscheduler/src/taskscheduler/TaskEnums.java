package taskscheduler;

public class TaskEnums {
	/**The task runs every sunday*/
	private  final static long SUNDAY = 1;
	/**The task runs every monday*/
	private final static long MONDAY = 2;
	/**The task runs every tuesday*/
	private final static long TUESDAY = 4;
	/**The task runs every wednesday*/
	private final static long WEDNESDAY = 8;
	/**The task runs every thursday*/
	private final static long THURSDAY = 16;
	/**The task runs every friday*/
	private final static long FRIDAY = 32;
	/**The task runs every saturday*/
	private final static long SATURDAY = 64;
	/**The task runs everyday*/
	private final static long EVERY_DAY = 127;
	
	/**The days of week  in format suitable for Scheduler...*/
	public enum DAY_OF_WEEK{
		/**The task runs every Sunday*/
		SUNDAY(TaskEnums.SUNDAY),
		/**The task runs every Monday*/
		MONDAY(TaskEnums.MONDAY),
		/**The task runs every Tuesday*/
		TUESDAY(TaskEnums.TUESDAY),
		/**The task runs every Wednesday*/
		WEDNESDAY(TaskEnums.WEDNESDAY),
		/**The task runs every Thursday*/
		THURSDAY(TaskEnums.THURSDAY),
		/**The task runs every Friday*/
		FRIDAY(TaskEnums.FRIDAY),
		/**The task runs every Saturday*/
		SATURDAY(TaskEnums.SATURDAY),
		/**The task runs everyday*/
		EVERY_DAY(TaskEnums.EVERY_DAY),
		
		UNKNOWUN(-1);
		private long day;
		
		DAY_OF_WEEK(long day){
			this.day =day;
		}
		public long getDay() {
			return day;
		}
		
		public static DAY_OF_WEEK  toDayOfWeek(long day){
			DAY_OF_WEEK [] d = DAY_OF_WEEK.values();
			for(DAY_OF_WEEK dw : d){
				if(dw.day == day) return dw;
			}
			return UNKNOWUN;
		}
	}

	/***
	 * The priority of task launched by the Scheduler !!!
	 * @author lubo
	 *
	 */
	public enum TASK_PRIORITY {

		/***
		 * The default task priority !!!
		 */
		NORMAL,
		
		/***
		 * First executes the tasks with HIGH priority !!!
		 */
		HIGH
	};
	
	/**
	 * The status of task delegated to Scheduler !!!
	 * @author lubo
	 *
	 */
	public enum TASK_STATUS {

		/***
		 * This flag shows that the task is newly created !!!
		 */
		CREATED,
		
		/**
		 * This flag shows that the task is running !!!
		 */
		RUNNING,
		
		/***
		 * This flag shows that the task is in the task queue !!!
		 */
		SUBMITTED_NOT_RUNNING,
		
		/**
		 * This flag shows that the task has been interrupted !!! 
		 */
		INTERRUPTED,
		
		/***
		 * This flag shows that cancel has been called on this Task !!!
		 */
		CANCEL_PENDING,
		
		/**
		 *  This flag shows that task completed !!!
		 */
		 COMPLETED,
		 
		 /***
		  * Task has crashed !!!
		  */
		 CRASHED,
		 
		 /***
		  * The task will never be scheduled any more !!!
		  */
		 DEAD
	};

}

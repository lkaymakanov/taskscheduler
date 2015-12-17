package taskscheduler;


public class TaskEnums {
	
	private final static  long [] powerOf2 = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};
	
	/**The days of week  in format suitable for Scheduler...*/
	public enum DAY_OF_WEEK{
		/**The task runs every Sunday*/
		SUNDAY(powerOf2[0]),
		/**The task runs every Monday*/
		MONDAY(powerOf2[1]),
		/**The task runs every Tuesday*/
		TUESDAY(powerOf2[2]),
		/**The task runs every Wednesday*/
		WEDNESDAY(powerOf2[3]),
		/**The task runs every Thursday*/
		THURSDAY(powerOf2[4]),
		/**The task runs every Friday*/
		FRIDAY(powerOf2[5]),
		/**The task runs every Saturday*/
		SATURDAY(powerOf2[6]),
		/**The task runs everyday*/
		EVERY_DAY(powerOf2[7] - 1),
		
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
	
	
	/**The months  of year  in format suitable for Scheduler...*/
	public enum MONTH_OF_YEAR{
		/**The task runs JANUARY*/
		JANUARY(powerOf2[0]),
		/**The task runs FEBRUARY*/
		FEBRUARY(powerOf2[1]),
		/**The task runs MARCH*/
		MARCH(powerOf2[2]),
		/**The task runs APRIL*/
		APRIL(powerOf2[3]),
		/**The task runs MAY*/
		MAY(powerOf2[4]),
		/**The task runs JUNE*/
		JUNE(powerOf2[5]),
		/**The task runs JULY*/
		JULY(powerOf2[6]),
		/**The task runs AUGUST*/
		AUGUST(powerOf2[7]),
		/**The task runs SEPTEMBER*/
		SEPTEMBER(powerOf2[8]),
		/**The task runs OCTOBER*/
		OCTOBER(powerOf2[9]),
		/**The task runs NOVEMBER*/
		NOVEMBER(powerOf2[10]),
		/**The task runs DECEMBER*/
		DECEMBER(powerOf2[11]),
		/**The task runs EVERY_MONTH*/
		EVERY_MONTH(powerOf2[11] - 1),
		
		UNKNOWUN(-1);
		private long day;
		
		MONTH_OF_YEAR(long day){
			this.day =day;
		}
		public long getDay() {
			return day;
		}
		
		public static MONTH_OF_YEAR  toMonthOfYear(long day){
			MONTH_OF_YEAR [] d = MONTH_OF_YEAR.values();
			for(MONTH_OF_YEAR dw : d){
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
	
	
	public static void main(String [] args){
		for(long l : powerOf2){
			System.out.println(MONTH_OF_YEAR.toMonthOfYear(l) +  " " + l);
			System.out.println(DAY_OF_WEEK.toDayOfWeek(l) +  " " + l);
		}
	}
}

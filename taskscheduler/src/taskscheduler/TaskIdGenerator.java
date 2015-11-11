package taskscheduler;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The generator of tasks ids....
 * @author lubo
 *
 */
public class TaskIdGenerator {

	/** The id cnt. */
	private static AtomicLong idCnt = new  AtomicLong();

	/**
	 * The next value  of counter!!!
	 *
	 * @return the next id
	 */
	public static  long getNextId(){
		return idCnt.incrementAndGet();
	}
	
	/**
	 * 	The current value of counter!!!
	 *
	 * @return the current id
	 */
	public static  long getCurrentId(){
		return idCnt.get();
	}
}

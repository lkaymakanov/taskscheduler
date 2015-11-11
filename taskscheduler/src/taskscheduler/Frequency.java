package taskscheduler;

import java.util.concurrent.TimeUnit;
/**
 * A frequency that shows how often the scheduler must schedule Tasks!
 * @author lubo
 *
 */
public final class Frequency {
	private long delay = 1000;                                 
	private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
	
	public Frequency(long delay, TimeUnit timeUnit){
		this.delay = delay;
		this.timeUnit = timeUnit;
	}
	
	public long getDelay() {
		return delay;
	}
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	
}

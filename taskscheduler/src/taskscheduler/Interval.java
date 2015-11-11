package taskscheduler;

/**
 * A class that represents an interval of time!!!
 * @author lubo
 *
 */
public abstract class Interval {
	long from;
	long to;
	
	
	public Interval(long from, long to){
		this.from = from;
		this.to = to;
		if(from < 0 || to < 0 || (to < from ) ) throw new IllegalArgumentException();
	}
	
	/***
	 * Check if daytime input param is in this interval of time!!!!
	 * @param daytime
	 * @return
	 */
	public abstract boolean isInInterval(long daytime);
	
	/***
	 * Returns a copy object of this interval!!!
	 * @return
	 */
	public abstract Interval copyInterval();
}

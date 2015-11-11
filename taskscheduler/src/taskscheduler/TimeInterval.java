package taskscheduler;

import java.util.ArrayList;
import java.util.List;


/**
 * An interval that represents time (from to) in the format from ( hh:mm:ss:milliseconds - hh:mm:ss:milliseconds ) (hours minutes, seconds)!!!
 * @author lubo
 *
 */
public final class TimeInterval extends Interval{
	private Hour fromH;
	private Hour toH;
	public TimeInterval(Hour from, Hour to) {
		super(0, 0);
		// TODO Auto-generated constructor stub
		this.from = from.toMillis();
		this.to = to.toMillis();
		this.fromH = from;
		this.toH = to;
	}
	
	TimeInterval(long from, long to){
		super(from, to);
		fromH = millisToHour(from);
		toH = millisToHour(to);
	}

	@Override
	public boolean isInInterval(long daytime) {
		// TODO Auto-generated method stub
		if(from <= daytime && daytime <=to) return true;
		return false;
	}
	
	/**
	 * If the beginning of hour interval is less than the end of Hour interval then the interval is split into 2  intervals!!! 
	 * The first Interval from beginning to midnight and the second from midnight to end of interval!!!
	 * For example the time interval 19:15 - 7:15 will be split into (19:15 - 23:59:59:999) and (23:59:59:999 - 7:15)!!!
	 * @return
	 */
	public static List<TimeInterval>  normalizeInterval(TimeInterval interval){
		List<TimeInterval> l = new  ArrayList<TimeInterval>();
		if(interval.from > interval.to){
			Hour midnight = new Hour(23, 59, 59, 999);
			l.add(new TimeInterval(interval.from, midnight.toMillis()));
			l.add(new TimeInterval(midnight.toMillis(), interval.to));
		}
		return l;
	}
	
	
	/**
	 *The same as normalizeInterval, but storing the normalized interval in outputList !!!
	 * @return void
	 */
	public static void normalizeInterval(TimeInterval interval, List<Interval> outputList){
		if(interval.from > interval.to){
			Hour midnight = new Hour(23, 59, 59, 999);
			outputList.add(new TimeInterval(interval.from, midnight.toMillis()));
			outputList.add(new TimeInterval(midnight.toMillis(), interval.to));
		}else{
			outputList.add(interval);
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return fromH.getHour() +  ":" + fromH.getMinute() + ":" + fromH.getSeconds() + " HH:MM:Sec - "+
		toH.getHour() +  ":" + toH.getMinute() + ":" + toH.getSeconds() + " HH:MM:Sec ";
	}
	
	@Override
	public Interval copyInterval() {
		// TODO Auto-generated method stub
		return new TimeInterval(from, to);
	}
	
	private Hour millisToHour(long milliesOfhour){
		int secs = (int)(milliesOfhour/1000);
		int min = (int)(secs)/60;
		int millis = (int) (milliesOfhour%1000);
		int seconds = (((int)(secs))%60);
		int minutes = ((int)min)%60;
		int hours = (min/60)%24;
		return new Hour(hours, minutes, seconds, millis);
	}

}

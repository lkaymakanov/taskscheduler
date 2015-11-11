package taskscheduler;

/**
 * <pre>
 * A class that represents an hour in the format hh:mm:ss:milliseconds!!!
 * Objects of this class are immutable!!!
 * </pre>
 * @author lubo
 *
 */
public final class Hour {
	
	private final int hour;
	private final int minute;
	private final int seconds;
	private final int millisec;
	
	final static long ONE_SEC = 1000;
	final static long ONE_MIN = 60*ONE_SEC;
	final static long ONE_HOUR = 60*ONE_MIN;

	public Hour(int h, int minute, int sec, int millisec){
		this.hour = h;
		this.minute = minute;
		this.seconds = sec;
		this.millisec = millisec;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getSeconds() {
		return seconds;
	}
	
	public int getMillisec() {
		return millisec;
	}

	/**
	 * Converts the hour to milliseconds!!!
	 * @return
	 */
	public long toMillis(){
		return hour * ONE_HOUR + minute * ONE_MIN + seconds * ONE_SEC + millisec;
	}
}

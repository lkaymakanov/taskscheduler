package taskscheduler;

import java.util.Date;

public class DateTimeInterval extends Interval{

	public DateTimeInterval(long from, long to) {
		super(from, to);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isInInterval(long daytime) {
		// TODO Auto-generated method stub
		if(from <= daytime && daytime <=to) return true;
		return false;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "" + new Date(from) + " - " + new Date(to);
	}

	@Override
	public Interval copyInterval() {
		// TODO Auto-generated method stub
		return new DateTimeInterval(from, to);
	}

	
}

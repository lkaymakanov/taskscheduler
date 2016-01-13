package taskscheduler;

/**
 * The message that is set when lock has been locked!!!
 * @author lubo
 *
 */
public class ScheduledTaskLockMessage {

	String value;

	public ScheduledTaskLockMessage(String value){
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

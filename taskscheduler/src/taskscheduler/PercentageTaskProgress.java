package taskscheduler;

/***
 * An immutable object that indicates the percentage the task has completed!!!
 * @author lubo
 *
 */
public final class PercentageTaskProgress implements ITaskProgress {

	final long percentage;
	final long startTime; 
	final long finishTime;
	
	public PercentageTaskProgress(long percentage, long startTime, long finishTime){
		this.percentage = percentage;
		this.startTime = startTime;
		this.finishTime = finishTime;
	}
	
	public long getPercentage(){
		return percentage;
	}

	@Override
	public long getStartTime() {
		// TODO Auto-generated method stub
		return startTime;
	}

	@Override
	public long getFinishTime() {
		// TODO Auto-generated method stub
		return finishTime;
	}
	
}

package taskscheduler;

import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * <pre>
 * Simple guarding resource lock! 
 * The only difference between this lock and using synchronized region idiom is that 
 * it doesn't block the calling thread when checking the lock 
 * but simply skips the critical section if lock is acquired by other thread!!!
 * 
 * Usage 
 * <code>
 * SimpleLock l = new SimpleLock();
 * 
 * if(!l.lock()){ //acquire the lock
 *     try {
 * 
 * 		//perform time critical task
 *     }
 *     finally{
 *      //release the lock
 * 		l.unlock();
 *     }
 * }
 * </code>
 * </pre>
 * @author lubo
 *
 */

class ScheduledTaskLock {
	//flag
	/** The locked. */
	private boolean locked = false;
	
	/** The thread lock id. */
	private long threadLockId = -1;
	
	/** The reentrant. */
	private boolean reentrant = true;
	
	/***
	 * The message set when lock has been locked!!!
	 */
	private ScheduledTaskLockMessage message;
	
	/** The id. */
	private long id  = UUID.randomUUID().getLeastSignificantBits();
	
	/** The Constant enablbeLog. */
	private final static boolean enablbeLog = true;
	
	
	/**
	 * Lock counter - the number of locks performed by the same thread after the lock has bean acquired by it.
	 */
	private int cnt = 0;   
	
	/**
	 * Instantiates a new simple lock.
	 */
	public ScheduledTaskLock(){
		this(true);
	}
	
	
	/**
	 * Instantiates a new simple lock.
	 *
	 * @param reentrant the reentrant
	 */
	public ScheduledTaskLock(boolean reentrant){
		this.reentrant = true; 
	}
	/**
	 * <pre>
	 * Call lock on entering critical section. 
	 * Performs check if the lock is acquired and locks the lock if it is not acquired.
	 * If this lock has been acquired by another thread than the calling thread  it returns TRUE. 
	 * If this lock has been acquired by the calling thread the returned value is FALSE.
	 * @return Return value is the previous state of lock
	 * </pre>
	 */
	public synchronized boolean lock(){
		boolean prevState = locked;
		long currentThreadId = Thread.currentThread().getId();
		
		if(reentrant){//the lock is reentrant
			//WE HAVE REENTRANCE - the lock has been locked by the calling thread -  increase the counter & return false.
			if(prevState && currentThreadId == threadLockId){
				cnt++;
				log("THREAD WITH ID " + currentThreadId + " REENTERED  THE THE LOCK WITH ID " +id + "  "  + cnt  + " times...");
				return  false;
			}
		}
		if(!locked){
			threadLockId = Thread.currentThread().getId();
			this.locked = true;
			cnt++;
			log("THREAD WITH ID " + currentThreadId + " ACQUIRED THE THE LOCK WITH ID " +id);
		}else{
			log("THREAD WITH ID " + currentThreadId + " FAILED TO ACQIURE THE THE LOCK WITH ID " +id + " BECAUSE IT IS LOCKED BY THREAD WITH ID " + threadLockId);
		}
		
		return prevState;
	}
	
	/**
	 * The same as lock but also sets a message - the reason for lock!
	 * @param message
	 * @return
	 */
	public synchronized boolean lock(ScheduledTaskLockMessage message){
		boolean b = lock();
		if(!b) {this.message = message; log("Message is " +  this.message);}
		return b;
	}
	
	/**<pre>
	 * Call unLock on exiting critical section.
	 * 
	 * </pre>
	 */
	public synchronized void unLock(){
		long currentThreadId = Thread.currentThread().getId();
		if(threadLockId == currentThreadId)cnt--;
		if(cnt == 0){
			log("THREAD WITH ID " + threadLockId  + " RELEASED THE LOCK WITH ID " +  id);
			this.threadLockId = -1;
			this.locked = false;
			message = null;   //clear the message when releasing lock!!!
		}
	}
	
	/**
	 * <pre>
	 * Call isLocked  to check lock state.
	 * 
	 * If this lock has been acquired by another thread than the calling thread  it returns TRUE.
	 * If this lock has been acquired by the calling thread the returned value is FALSE.
	 * </pre>
	 *
	 * @return true, if is locked
	 */
	public  synchronized boolean isLocked(){
		long currentThreadId = Thread.currentThread().getId();
		//if has been acquired by the calling thread return false.
		if(reentrant && locked && (currentThreadId == threadLockId))  {
			return false;
		}
		return locked;
	}
	
	/**
	 * Call getThreadLockId  to get id of the thread that locked this object.
	 *
	 * @return the thread lock id
	 */
	public  synchronized long getThreadLockId(){
		return threadLockId;
	}
	

	/**
	 * test.
	 *
	 * @param args the arguments
	 */
	
	public static void main(String [] args){
		new  ScheduledTaskLock().new TestLock(new ScheduledTaskLock()).useLock();
	}
	
	
	/**
	 * The Class TestLock.
	 */
	class TestLock{
		
		/** The l. */
		ScheduledTaskLock l;
		
		/**
		 * Instantiates a new test lock.
		 *
		 * @param l the l
		 */
		TestLock(ScheduledTaskLock l){
			this.l = l;
		}
		
		/**
		 * Use lock.
		 */
		public void useLock(){
			if(!l.lock()){
				try{
					reenterLock();
				}finally{
					l.unLock();
				}
			}
		}
		
		/**
		 * Reenter lock.
		 */
		public void reenterLock(){
			if(!l.lock()){
				try{
					
					
				}finally{
					l.unLock();
				}
			}
		}
	}

	
	/**
	 * Logs a message if log is enabled.
	 *
	 * @param message the message
	 */
	private void log(String message){
		if(enablbeLog)	System.out.println(message);
	}
	
	public ScheduledTaskLockMessage getMessage() {
		return message;
	}
	

}

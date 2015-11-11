package taskscheduler;

import java.util.HashMap;
import java.util.Map;


/**
 * A naming context that binds a name & a ScheduledTask!!!
 * Used to retrieve a reference to 
 * the task based on the name under which the Task is stored in the naming context!!!
 * @author lubo
 *
 */
public class NamingScheduledTaskContext implements ITaskSchedulerRegister<String, ScheduledTask> {
	
	private Map<String, ScheduledTask> registerMap = new HashMap<String, ScheduledTask>();

	@Override
	public boolean register(String key, ScheduledTask val) {
		// TODO Auto-generated method stub
		synchronized (this) {
			//if no such key in map the object is not registered
			if(!registerMap.containsKey(key)){
				registerMap.put(key, val);
				return false;
			}
			//object is registered
			return true;
		}
	}

	@Override
	public void unRegister(String key) {
		// TODO Auto-generated method stub
		synchronized (this) {
			//if object is registered - remove it from register map
			if(registerMap.containsKey(key)){
				registerMap.remove(key);
			}
		}
	}

	@Override
	public boolean isRegistered(String key) {
		// TODO Auto-generated method stub
		synchronized (this) {
			if(registerMap.containsKey(key)) return true;
			return false;
		}
	}
	
	public ScheduledTask getScheduledTask(String key){
		synchronized (this) {
			return	registerMap.get(key);
		}
	}
	
	public void replaceAndSubmit(ScheduledTask newTask, ThreadPoolTaskScheduler sch){
		synchronized (this) {
			//replace
			registerMap.put(newTask.getTaskName(), newTask);
			
			//submit
			sch.submitTask(newTask);
		}
	}
	
}

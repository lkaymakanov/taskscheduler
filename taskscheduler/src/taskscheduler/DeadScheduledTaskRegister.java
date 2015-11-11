package taskscheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/***
 * A class that holds the Dead Tasks!!! Used to register a dead task & lately remove it from Task Queue!!!
 * The key under which task is register is it's reference!!!
 * @author lubo
 *
 */
class DeadScheduledTaskRegister  implements ITaskSchedulerRegister<ScheduledTask, ScheduledTask> {
	private DeadScheduledTaskRegister(){}
	private Map<ScheduledTask, ScheduledTask> registerMap = new HashMap<ScheduledTask, ScheduledTask>();

	@Override
	public boolean register(ScheduledTask key, ScheduledTask val) {
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
	public void unRegister(ScheduledTask key) {
		// TODO Auto-generated method stub
		synchronized (this) {
			//if object is registered - remove it from register map
			if(registerMap.containsKey(key)){
				registerMap.remove(key);
			}
		}
	}

	@Override
	public boolean isRegistered(ScheduledTask key) {
		// TODO Auto-generated method stub
		synchronized (this) {
			if(registerMap.containsKey(key)) return true;
			return false;
		}
	}

	public List<ScheduledTask> getDeadScheduledTasks(){
		synchronized (this){
			List<ScheduledTask> l = new  ArrayList<ScheduledTask>();
			Collection<ScheduledTask> c = registerMap.values();
			Iterator<ScheduledTask> i =  c.iterator();
			while (i.hasNext()) { l.add(i.next()); }
			return l;
		}
	}
	
	static DeadScheduledTaskRegister  getInstance(){
		return new DeadScheduledTaskRegister();
	}

	void clear() {
		// TODO Auto-generated method stub
		synchronized (this){
			registerMap.clear();
		}
	}

	
	
}

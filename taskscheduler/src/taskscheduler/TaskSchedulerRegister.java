package taskscheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




/***
 * The Register with the running tasks!!!!
 * @author lubo
 *
 */
public class TaskSchedulerRegister<T extends ITask> implements ITaskSchedulerRegister<Long, T> {

	private TaskSchedulerRegister(){}
	private Map<Long, T> registerMap = new HashMap<Long, T>();

	
	public  boolean register(Long key, T val) {
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

	/**
	 * Retrieves Task by Id!!!
	 * @param id
	 * @return
	 */
	T getTaskById(long id){
		synchronized (this) {
			return registerMap.get(id);
		}
	}
	
	
	public void unRegister(Long key) {
		// TODO Auto-generated method stub
		synchronized (this) {
			//if object is registered - remove it from register map
			if(registerMap.containsKey(key)){
				registerMap.remove(key);
			}
		}
	}


	
	public  boolean isRegistered(Long key) {
		// TODO Auto-generated method stub
		synchronized (this) {
			if(registerMap.containsKey(key)) return true;
			return false;
		}
	}
	
	/**
	 * Retrieves the List with the registered & running  tasks information !!!
	 * @return
	 */
	public List<TaskInfo> getRegisteredTasksInfo(){
		synchronized (this) {
			Collection<T> tasks = registerMap.values();
			List<TaskInfo> infoList= new ArrayList<TaskInfo>();
			Iterator<T> it = tasks.iterator();
			while(it.hasNext()){
				ITask  task =	 it.next();
				infoList.add(task.getTaskInfo());
			}
			return infoList;
		}
	}
	
	static <T extends ITask> TaskSchedulerRegister<T> getInstance(){
		return new TaskSchedulerRegister<T>();
	}



	public TaskInfo getTaskInfo(long id) {
		// TODO Auto-generated method stub
		synchronized (this){
			ITask t = registerMap.get(id);
			if(t==null) return null;
			return t.getTaskInfo();
		}
	}
	
}

package taskscheduler;

public interface ITaskSchedulerRegister<K,V> {

		/**
		 * Registers an Object of Type V under Key of type K. If an object is registered under key K returns true.
		 * Otherwise the object is registered under key K & returns false.
		 * @param key
		 * @param val
		 * @return
		 */
		public boolean register(K key, V val);
		
		/**
		 * 
		 * If object has been registered - remove it from register map.
		 */
		public void unRegister(K key);
		
		/***
		 * Checks if object is registered under key K.
		 * @param key
		 * @return
		 */
		public boolean isRegistered(K key);

}

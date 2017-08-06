package lib;

import java.util.HashMap;
import java.util.Map;

public class Cache {

	private static Cache instance;
	private static final long EXPIRATION = 3600000; // 1 hour in milliseconds;
	
	private Map<String, Entry> cache;
	private long expiration;
	
	public static Cache getInstance() {
		if (instance == null) {
			instance = new Cache();
		}
		return instance;
	}
	
	private Cache() {
		this.cache = new HashMap<>();
		this.expiration = EXPIRATION;
	}
	
	public Object get(String key) {
		if (isValid(key)) {
			return cache.get(key).getObject();	
		}
		return null;
	}
	
	public void put(String key, Object object) {
		cache.put(key, new Entry(object));
	}
	
	private boolean isValid(String key) {
		if (!cache.containsKey(key)) {
			return false;
		}
		Entry entry = cache.get(key);
		
		return System.currentTimeMillis() - entry.getTimestamp() < expiration;
	}
	
	private static final class Entry {
		private long timestamp;
		private Object object;
		
		public Entry(Object object) {
			this.timestamp = System.currentTimeMillis();
			this.object = object;
		}
		
		public long getTimestamp() {
			return timestamp;
		}
		
		public Object getObject() {
			return object;
		}
	}
}

package talecraft.script;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import talecraft.script.wrappers.IObjectWrapper;

public class WrapperClassCache {

	private Map<IObjectWrapper, Cache> cache = new HashMap<IObjectWrapper, Cache>();
	
	/**
	 * Gets the cache for the wrapper and if one is not cached
	 * it will create it.
	 */
	public Cache getCache(IObjectWrapper wrapper){
		if(cache.containsKey(wrapper)) return cache.get(wrapper);
		Cache cache = new Cache(wrapper);
		this.cache.put(wrapper, cache);
		return cache;
	}
	
	
	class Cache{
		
		private final List<String> props;
		
		private Cache(IObjectWrapper wrapper){
			Class<?> clazz = wrapper.getClass();

			Field[] fields = clazz.getDeclaredFields();
			Method[] methods = clazz.getDeclaredMethods();

			String[] props = new String[fields.length+methods.length];
			int ix = 0;

			for(Field field : fields) {
				props[ix++] = field.getName();
			}

			for(Method method : methods) {
				props[ix++] = method.getName();
			}

			this.props = Lists.newArrayList(props);
		}
		
		public List<String> getProps(){
			return props;
		}
	}
	
}

package collectivesim.util;


import java.util.HashMap;
import java.util.Map;

/**
 * Implements methods to set and get typed values (long,double, boolean and String).
 * 
 * @version 1.0  January 30,2006
 * @author Pablo Chacin
 *
 */
@SuppressWarnings("serial")
public class TypedMap extends HashMap<String,Object> {

	/**
	 * Constructor based on an existing Map.
	 */
	public TypedMap(Map<String, Object> map){
		super(map);
	}
	
	/**
	 * Deafult constructor. Invoques construnctor from super class. 
	 *
	 */
	public TypedMap() {
     super();
	}

	/** 
	 * Returns the value of a long parameter from this Map..
	 * 
	 * @param key identification of the argument.
	 * @return a long with the value of the element under the given key.
	 * @throws NumberFormatException if the argument's value does not conform to the 
	 *         requested format or sucthe key is not associates with any element.
	 */
	public long getLong(String key){
		String value = (String)get(key);
			return Long.valueOf(value).longValue();
	}
	
	/** 
	 * Returns the value of a boolean parameter from this Map..
	 * 
	 * @param key identification of the argument.
	 * @return a boolean with the value of the element under the given key or false if such 
	 *         key is not associates with any element.
	 */
	public boolean getBoolean(String key){
		String value = (String)get(key);
			return Boolean.valueOf(value).booleanValue();
	}

	
	/** 
	 * Returns the value of a Double parameter from this Map.
	 * 
	 * @param key identification of the argument.
	 * @return a double with the value of the element under the given key
	 * @throws NumberFormatException if the argument's value does not conform to the 
	 *         requested format or the key is not associates with any element.
	 */
	public double getDouble(String key){
		String value = (String)get(key);
		return Double.valueOf(value).doubleValue();
	}	
	
	
	/** 
	 * Returns the value of an Object parameter from this Map.
	 * 
	 * @param key identification of the argument.
	 * @return an Object with the value of the element under the given key or such key is 
	 *         not associates with any element.
	 */
	public Object getObject(String key){
		return get(key);
	}	
	
	
	/** 
	 * Returns the value of a Long parameter from this Map .
	 * 
	 * @param key identification of the .
	 * @return a String with the value of the element under the given key or such key is 
	 *         not associates with any element..
	 */
	public String getString(String key){
		return (String)get(key);
	}	
	
	
	public String[] getStringArray(String key){
		return getStringArray(key,",");
	}
	
	
	public String[] getStringArray(String key,String separator){
		String value = (String)get(key);
		if(value != null){
			return value.split(separator);
		}else{
			return null;
		}
	}	
	
	/** 
	 * Returns the value of a Integer parameter from this Map.
	 * 
	 * @param key identification of the argument.
	 * @return a Integer with the value of the element under the given key
	 * @throws NumberFormatException if the argument's value does not conform to the 
	 *         requested format or the key is not associates with any element.
	 */
	public Integer getInteger(String key){
		String value = (String)get(key);
		return Integer.valueOf(value);
	}
	/**
	 * Sets a long value in the Map under a given key of type String
	 * 
	 * @param key identification of the value.
	 * @param value long with the value to be set.	 
	 */
	public void putLong(String key,long value){
		
		this.put(key,String.valueOf(value));
	}
	
	/**
	 * Sets a long value in the Map under a given key of type String
	 * 
	 * @param key identification of the value.
	 * @param value long with the value to be set.	 
	 */
	public void putInteger(String key,int value){
		
		this.put(key,String.valueOf(value));
	}
	
	/**
	 * Sets a long value in the Map under a given key of type String
	 * 
	 * @param key identification of the value
	 * @param value double with the value to be set.
	 */
	public void putDouble(String key,double value){
		this.put(key,String.valueOf(value));
	}
	
	
	/**
	 * Sets a long value in the Map under a given key of type String
	 * 
	 * @param key identification of the value
	 * @param value boolean with the value to be set.
	 */
	public void putBoolean(String key,boolean value){
		this.put(key,String.valueOf(value));
	}
	
	/**
	 * Sets a long value in the Map under a given key of type String
	 * 
	 * @param key identification of the value
	 * @param value String with the value to be set.
	 */
	public void putString(String key,String value){
		this.put(key,value);
	}
	
	/**
	 * Sets an Object value in the Map under a given key of type String
	 * 
	 * @param key identification of the value
	 * @param value String with the value to be set.
	 */
	public void putObject(String key,Object value){
		this.put(key,value);
	}
	
	public void putDoubleArray(String key,Double ...doubles){
		put(key,doubles);
	}


	
}

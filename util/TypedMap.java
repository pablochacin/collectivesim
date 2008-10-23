/*
 * P2PAgent.java
 * 
 * Created January 30, 2006
 * 
 * (c) UPC
 */
package edu.upc.cnds.collectivesim.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements methods to set and get typed values (long,double, boolean and String).
 * 
 * @version 1.0  January 30,2006
 * @author Pablo Chacin
 *
 */
public class TypedMap extends HashMap {

	/**
	 * Constructor based on an existing Map.
	 */
	public TypedMap(Map map){
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
	public long getLong(Object key){
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
	public boolean getBoolean(Object key){
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
	public double getDouble(Object key){
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
	public Object getObject(Object key){
		return get(key);
	}	
	
	
	/** 
	 * Returns the value of a Long parameter from this Map .
	 * 
	 * @param key identification of the .
	 * @return a String with the value of the element under the given key or such key is 
	 *         not associates with any element..
	 */
	public String getString(Object key){
		return (String)get(key);
	}	
	
	/**
	 * Sets a long value in the Map under a given key of type String
	 * 
	 * @param key identification of the value.
	 * @param value long with the value to be set.	 
	 */
	public void putLong(Object key,long value){
		
		this.put(key,String.valueOf(value));
	}
	
	/**
	 * Sets a long value in the Map under a given key of type String
	 * 
	 * @param key identification of the value
	 * @param value double with the value to be set.
	 */
	public void putDouble(Object key,double value){
		this.put(key,String.valueOf(value));
	}
	
	
	/**
	 * Sets a long value in the Map under a given key of type String
	 * 
	 * @param key identification of the value
	 * @param value boolean with the value to be set.
	 */
	public void putBoolean(Object key,boolean value){
		this.put(key,String.valueOf(value));
	}
	
	/**
	 * Sets a long value in the Map under a given key of type String
	 * 
	 * @param key identification of the value
	 * @param value String with the value to be set.
	 */
	public void putString(Object key,String value){
		this.put(key,value);
	}
	
}

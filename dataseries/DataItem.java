package edu.upc.cnds.collectivesim.dataseries;

import java.util.Map;

/**
 * Represents an item in a Series. The item has a numerical value and is associated
 * with a (optional) series of attributes. 
 * 
 * @author Pablo Chacin
 *
 */
public interface DataItem {
	
	/**
	 * 
	 * @return A (possibly empty) Map of attributes that describe this DataItem
	 */
	public Map getAttributes();
	
	
	/**
	 * 
	 * @return the actual value of the DataItem
	 */
	public Double getValue();
	
}

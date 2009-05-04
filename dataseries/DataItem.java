package edu.upc.cnds.collectivesim.dataseries;

import java.util.Map;

/**
 * Represents an item in a DataSeries. The item has a set of attributes
 * of type String, Double (numeric) or Boolean. 
 * 
 * @author Pablo Chacin
 *
 */
public interface DataItem {
	
	/**
	 * 
	 * @return A map of attributes that describe this DataItem and their values 
	 * as Strings
	 */
	public Map<String,String> getAttributes();
	

	/**
	 * Returns the value of a data as a Double. If the attribute can't be
	 * casted to double, returns the value NaN defined in the double class
	 * 
	 * @return the value of the attribute as a Double
	 */
	public Double getDouble(String attribute);
	
	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public Boolean getBoolean(String attribute);
	
	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public String getString(String attribute);
	
	
	/**
	 * @return the sequence of the item in the series
	 */
	public int getSequence();
			
}

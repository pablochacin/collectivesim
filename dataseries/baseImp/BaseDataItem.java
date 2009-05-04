package edu.upc.cnds.collectivesim.dataseries.baseImp;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;

/**
 * Implements a DataItem using a Map of attributes. Each attribute is stored as a String
 * 
 * @author Pablo Chacin
 *
 */
public class BaseDataItem implements DataItem {

	private Map<String,Object> attributes;
	
	DataSeries dataSeries;
	
	private int sequence;
		
	/**
	 * Creates a DataItem from a map of Objects. Only elements of the map that has a 
	 * numeric value (Double, Integer, Long), String or Boolean are added.
	 * 
	 * @param series
	 * @param sequence
	 * @param attributes
	 */
	BaseDataItem(DataSeries series, int sequence, Map<String,Object> attributes) {
		super();
		this.sequence = sequence;
		this.attributes = new HashMap<String,Object>();
		
		for(String s: attributes.keySet()){
			Object value = attributes.get(s);
			if((Number.class.isInstance(value))   || 
				(String.class.isInstance(value))  ||
				(Boolean.class.isInstance(value))){
				this.attributes.put(s, value);
			}
		}
	}


	/**
	 * Convenience constructor with a single attribute of type String
	 * @param series
	 * @param sequence
	 * @param attribute
	 * @param value
	 */
	BaseDataItem(DataSeries series,int sequence, String attribute, String value){
		this.dataSeries = series;
		this.sequence = sequence;
		this.attributes = new HashMap<String,Object>();
		this.attributes.put(attribute, value);
		
	}

	/**
	 * Convenience constructor with a single attribute of type Double, which is converted
	 * to String.
	 * @param series
	 * @param sequence
	 * @param attribute
	 * @param value
	 */
	BaseDataItem(DataSeries series, int sequence, String attribute, Double value) {
		this(series,sequence,attribute,value.toString());
	}
	
	/**
	 * Convenience constructor with a single attribute of type Boolean, which is converted
	 * to String.
	 * 
	 * @param series
	 * @param sequence
	 * @param attribute
	 * @param value
	 */
	BaseDataItem(DataSeries series,int sequence, String attribute, Boolean value){
		this(series,sequence,attribute,value.toString());
		
	}
	public Map getAttributes() {
		return new HashMap<String,Object>(attributes);
	}

	public Double getDouble(String name) {
		try{
			return Double.valueOf(attributes.get(name).toString());
		}
		catch(NumberFormatException e){
				return Double.NaN;
		}
	}
	
	public String getString(String name){
		return attributes.get(name).toString();
	}
	
	
	public Boolean getBoolean(String name){
		return Boolean.valueOf(attributes.get(name).toString());
	}
	public int getSequence(){
		return sequence;
	}

}

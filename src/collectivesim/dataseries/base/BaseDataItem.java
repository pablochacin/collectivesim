package collectivesim.dataseries.base;

import java.util.HashMap;
import java.util.Map;

import collectivesim.dataseries.DataItem;
import collectivesim.dataseries.DataSeries;

/**
 * Implements a DataItem using a Map of attributes. Each attribute is stored as a String
 * 
 * @author Pablo Chacin
 *
 */
public class BaseDataItem implements DataItem {

	private Map<String,Object> attributes;
	
	private int sequence;
		
	/**
	 * Creates a DataItem from a map of Objects. Only elements of the map that has a 
	 * numeric value (Double, Integer, Long), String or Boolean are added.
	 * 
	 * @param series
	 * @param sequence
	 * @param attributes
	 */
	public BaseDataItem(int sequence, Map<String,Object> attributes) {
		super();
		this.sequence = sequence;
		this.attributes = new HashMap<String,Object>();
		this.attributes.put("sequence", sequence);
		
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
	public BaseDataItem(int sequence, String attribute, String value){
		this.sequence = sequence;
		this.attributes = new HashMap<String,Object>();
		this.attributes.put(attribute, value);
		this.attributes.put("sequence", sequence);
		
	}

	/**
	 * Convenience constructor with a single attribute of type Double, which is converted
	 * to String.
	 * @param series
	 * @param sequence
	 * @param attribute
	 * @param value
	 */
	public BaseDataItem(int sequence, String attribute, Double value) {
		this(sequence,attribute,value.toString());
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
	public BaseDataItem(int sequence, String attribute, Boolean value){
		this(sequence,attribute,value.toString());
		
	}
	


	public Map getAttributes() {
		return new HashMap<String,Object>(attributes);
	}

	public Double getDouble(String name) {
		try{
			return Double.valueOf(attributes.get(name).toString());
		}
		catch(Exception e){
				return Double.NaN;
		}
	}
	
	public String getString(String name){
		
		Object value = attributes.get(name);
		
		if (value == null){
			return "";
		}
		else{
			return value.toString();
		}
	}
	
	
	public Boolean getBoolean(String name){
		return Boolean.valueOf(attributes.get(name).toString());
	}
	public int getSequence(){
		return sequence;
	}

}

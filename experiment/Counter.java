package edu.upc.cnds.collectivesim.experiment;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.dataseries.DataItem;
import edu.upc.cnds.collectives.dataseries.baseImp.BaseDataItem;

/**
 * Maintains a counter of experiment related events, which are updated by
 * models. 
 * 
 * It extends DataItem to facilitate reading it periodically and putting it on a DataSeries.
 * 
 * @author Pablo Chacin 
 *
 */
public class Counter implements DataItem {

	private Map attributes;
	
	private Double value;
	
	private String name;
	
	/**
	 * Constructor with full attributes
	 * @param attributes
	 * @param value
	 */
	public Counter(String name, Map attributes) {
		this.name = name;
		this.attributes = attributes;
		this.value = 0.0;
	}

	/**
	 * Convenience constructor without attributes
	 * @param name
	 */
	public Counter(String name){
		this(name,new HashMap());
	}
	@Override
	public Map getAttributes() {
		return attributes;
	}

	@Override
	public Double getValue() {
		return new Double(value);
	}

	public synchronized void  increment(Double increment){
		value=+ increment;
	}
	
	public synchronized void increment(){
		value++;
	}

	/**
	 * Returns the value of the Counter as a DataItem
	 * @return
	 */
	public DataItem getAsDataItem() {
	
		return new BaseDataItem(this.getValue(),new HashMap(getAttributes()));
	}
}

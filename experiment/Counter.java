package edu.upc.cnds.collectivesim.experiment;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.baseImp.BaseDataItem;

/**
 * Maintains a counter of experiment related events, which are updated by
 * models. 
 * 
 * It extends DataItem to facilitate reading it periodically and putting it on a DataSeries.
 * 
 * @author Pablo Chacin 
 *
 */
public class Counter {


	private Double value;
	
	private String name;
	
	/**
	 * Constructor with full attributes
	 * @param attributes
	 * @param value
	 */
	public Counter(String name) {
		this.name = name;
		this.value = 0.0;
	}


	public Double getValue() {
		return new Double(value);
	}

	public String getName(){
		return name;
	}
	public synchronized void  increment(Double increment){
		value=+ increment;
	}
	
	public synchronized void increment(){
		value++;
	}

}

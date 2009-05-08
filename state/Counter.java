package edu.upc.cnds.collectivesim.state;


/**
 * Maintains a counter of experiment related events, which are updated by
 * models. 
 *  
 * @author Pablo Chacin 
 *
 */
public class Counter implements StateValue {


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
	
	/**
	 * sets the counter to its initialization value
	 */
	public void reset(){
		value = 0.0;
	}

}

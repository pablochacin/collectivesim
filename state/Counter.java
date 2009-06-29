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
	
	Counter parent;
	
	/**
	 * Constructor with full attributes
	 * @param attributes
	 * @param value
	 */
	public Counter(String name) {
		this.name = name;
		this.value = 0.0;
	}

	
	private Counter(String name,Counter parent){
		this.name = name;
		this.value = 0.0;
		this.parent = parent;
	}

	public Double getValue() {
		return new Double(value);
	}

	public String getName(){
		return name;
	}
	public synchronized void  increment(Double increment){
		value += increment;
		if(parent!= null){
			parent.increment(increment);
		}
	}
	
	public synchronized void increment(){
		
		increment(1.0);

	}
	
	/**
	 * sets the counter to its initialization value
	 */
	public void reset(){
		value = 0.0;
	}
	
	public Counter getChild(){
		return new Counter(name,this);
	}

}

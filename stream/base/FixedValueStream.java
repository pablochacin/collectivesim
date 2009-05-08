package edu.upc.cnds.collectivesim.stream.base;

import edu.upc.cnds.collectivesim.stream.Stream;


/**
 * A simple Stream that return always the same value
 * 
 * @author Pablo Chacin
 * @param <T>
 *
 */
public class FixedValueStream<T> implements Stream<T> {

	private String name;
	
	private T value;
	
	public FixedValueStream(String name, T value) {
		this.name = name;
		this.value = value;		
	}
	
	
	public String getName() {
		return name;
	}

	public T getValue() {
	
		return value;
	}


	@Override
	public void reset() {
		//DO nothing.
		
	}

	@Override
	public void open() {
		// Do nothing.
		
	}
	
	public String toString(){
		return "Fixed value " + value.toString();
	}
}

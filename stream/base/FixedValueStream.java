package edu.upc.cnds.collectivesim.stream.base;

import edu.upc.cnds.collectivesim.factory.Factory;
import edu.upc.cnds.collectivesim.stream.Stream;


/**
 * A simple Stream that return always the same value
 * 
 * @author Pablo Chacin
 * @param <T>
 *
 */
public class FixedValueStream<T> implements Stream<T>{

	
	private T value;
	
	public FixedValueStream( T value) {
		this.value = value;		
	}
	
	

	public T nextElement() {
	
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
	
	@Override
	public boolean hasMoreElements(){
		return true;
	}
}

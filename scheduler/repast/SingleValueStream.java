package edu.upc.cnds.collectivesim.scheduler.repast;

import edu.upc.cnds.collectivesim.scheduler.Stream;

/**
 * A simple Stream that return always the same value
 * 
 * @author Pablo Chacin
 * @param <T>
 *
 */
public class SingleValueStream<T> implements Stream<T> {

	private String name;
	
	private T value;
	
	public SingleValueStream(String name, T value) {
		this.name = name;
		this.value = value;		
	}
	
	
	public String getName() {
		return name;
	}

	public T getValue() {
	
		return value;
	}

	
}

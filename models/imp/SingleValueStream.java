package edu.upc.cnds.collectivesim.models.imp;

import edu.upc.cnds.collectivesim.models.Stream;

/**
 * A simple Stream that return always the same value
 * 
 * @author Pablo Chacin
 *
 */
public class SingleValueStream implements Stream {

	private String name;
	
	private Double value;
	
	public SingleValueStream(String name, Double value) {
		this.name = name;
		this.value = value;		
	}
	
	
	public String getName() {
		return name;
	}

	public Object getValue() {
	
		return value;
	}

	
}

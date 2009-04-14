package edu.upc.cnds.collectivesim.model;

import cern.jet.random.AbstractDistribution;


/**
 * Generates a random stream 
 * 
 * @author Pablo Chacin
 *
 */
public class RandomStream implements Stream<Double>{

	private String name;
	
	private AbstractDistribution distribution;
	
	public RandomStream(String name,AbstractDistribution distribution){
		this.name = name;
		this.distribution = distribution;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Double getValue() {
		return distribution.nextDouble();
	}

	@Override
	public void reset() {
		//Do nothing
	}

	@Override
	public void open() {
		// Do nothing.
		
	}
}

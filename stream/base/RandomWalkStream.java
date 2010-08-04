package edu.upc.cnds.collectivesim.stream.base;

import java.util.Random;

import edu.upc.cnds.collectives.Collectives;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.stream.StreamException;

public class RandomWalkStream implements Stream<Double> {
	
	private String name; 
	
	private Double minLoad;
	
	private Double maxLoad;
	
	private Double variation;
	
	private Double drift;
	
	private Double trend;

	private Double value;
	
	private Random rand;
	
	public RandomWalkStream(String name, Double minLoad, Double maxLoad, Double variation, Double drift, Double trend){

		this.name = name; 
		
		this.minLoad = minLoad;
		
		this.maxLoad = maxLoad;
		
		this.variation = variation;
		
		this.drift = drift;
		
		this.trend = trend;

		this.rand = Collectives.getExperiment().getRandomGenerator();
		
		this.value = minLoad + (maxLoad-minLoad)*rand.nextDouble();

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean hasMoreElements() {
		return true;
	}

	@Override
	public Double nextElement() {
		
		Double nextValue = value + variation*rand.nextDouble() + drift*trend;
		
		if(nextValue > 1.0){
			nextValue = 1.0;
		}
		else if(nextValue < 0.0){
			nextValue = 0.0;
		}
		
		value = nextValue;
		
		return value;
	}

	@Override
	public void open() throws StreamException {
				
	}

	@Override
	public void reset() {
		
	}

}

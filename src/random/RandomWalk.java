package collectivesim.random;


import java.util.Random;

import collectives.Collectives;
import collectivesim.CollectiveSim;
import collectivesim.stream.Stream;
import collectivesim.stream.StreamException;

public class RandomWalk  {
		
	private Double minLoad;
	
	private Double maxLoad;
	
	private Double variation;
	
	private Double drift;
	
	private Double trend;

	private Double value;
	
	private Random rand;
	
	public RandomWalk(Double minLoad, Double maxLoad, Double variation, Double drift, Double trend){
		
		this.minLoad = minLoad;
		
		this.maxLoad = maxLoad;
		
		this.variation = variation;
		
		this.drift = drift;
		
		this.trend = trend;

		this.rand = CollectiveSim.getExperiment().getRandomGenerator();
		
		this.value = minLoad + (maxLoad-minLoad)*rand.nextDouble();

	}



	public Double nextValue() {
		
		Double nextValue = value + variation*(-1+2*rand.nextDouble()) + drift*trend;
		
		if(nextValue > maxLoad){
			nextValue = maxLoad;
		}
		else if(nextValue < minLoad){
			nextValue = minLoad;
		}
		
		value = nextValue;
				
		return value;
		
	}



}

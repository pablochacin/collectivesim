package edu.upc.cnds.collectivesim.stream.base;

import java.util.Random;

import edu.upc.cnds.collectives.Collectives;
import edu.upc.cnds.collectivesim.CollectiveSim;
import edu.upc.cnds.collectivesim.random.RandomWalk;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.stream.StreamException;

public class RandomWalkStream extends RandomWalk implements Stream<Double> {
	
	

	public RandomWalkStream(Double minLoad, Double maxLoad, Double variation,
			Double drift, Double trend) {
		super(minLoad, maxLoad, variation, drift, trend);
	}

	

	@Override
	public boolean hasMoreElements() {
		return true;
	}

	@Override
	public Double nextElement() {
		
		return nextValue();
		
	}

	@Override
	public void open() throws StreamException {
				
	}

	@Override
	public void reset() {
		
	}

}

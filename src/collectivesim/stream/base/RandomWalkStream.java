package collectivesim.stream.base;

import java.util.Random;


import collectivesim.core.CollectiveSim;
import collectivesim.random.RandomWalk;
import collectivesim.stream.Stream;
import collectivesim.stream.StreamException;

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
	public void open() throws StreamException {
				
	}

	@Override
	public void reset() {
		
	}

}

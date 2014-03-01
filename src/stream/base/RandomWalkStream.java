package collectivesim.stream.base;

import java.util.Random;

import collectives.Collectives;
import collectivesim.CollectiveSim;
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

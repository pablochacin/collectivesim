package edu.upc.cnds.collectivesim.overlay.utility;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.random.RandomWalk;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * Returns a random utility value
 * 
 * @author 
 *
 */
public class RandomUtilityFunction extends RandomWalk implements UtilityFunction {


	public RandomUtilityFunction(Double minLoad, Double maxLoad,
			Double variation, Double drift, Double trend) {
		super(minLoad, maxLoad, variation, drift, trend);
		
	}

	

	@Override
	public Double getUtility(Node n) {
		
		return nextValue();	
	
	}

}

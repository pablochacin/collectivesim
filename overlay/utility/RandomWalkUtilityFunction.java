package edu.upc.cnds.collectivesim.overlay.utility;

import java.util.Random;

import edu.upc.cnds.collectives.Collectives;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.random.MersenneRandom;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * Returns a random utility value
 * 
 * @author 
 *
 */
public class RandomWalkUtilityFunction implements UtilityFunction {


	
	protected Stream<Double> variation;
	
	protected Stream<Double> trend;
	
	protected Double direction;
	
	/**
	 * Initial utility
	 */
	protected Double utility;

	/**
	 * Drift of the random walk
	 */
	protected Double drift;
	
	Random random;
	
	/**
	 * Create a random walk with an initial value and a direction
	 * @param direction
	 * @param utility
	 */
	public RandomWalkUtilityFunction(Double utility, Double initialTrend,Stream<Double> variation, Double drift,Stream<Double> trend) {
		super();
		this.utility = utility;
		this.drift = drift;
		this.variation = variation;
		this.trend = trend;
		this.random = Collectives.getExperiment().getRandomGenerator();
		this.direction = initialTrend;
		
		
	}



	@Override
	public Double getUtility(Node n) {
		
		direction = direction*trend.nextElement();
		
		Double newUtility = utility + variation.nextElement()+direction*drift;
		
		//ensure that 0 <= utility+variation `<= 1
		if(newUtility < 0)
			utility = 0.0;
		else if(newUtility > 1.0) {
			utility = 1.0;
		}
		else {
			utility = newUtility;
		}
									
		return newUtility;
	}
	
	

}

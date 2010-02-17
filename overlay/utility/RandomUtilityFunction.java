package edu.upc.cnds.collectivesim.overlay.utility;

import java.util.Random;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.random.MersenneRandom;

/**
 * Returns a random utility value
 * 
 * @author 
 *
 */
public class RandomUtilityFunction implements UtilityFunction {


	
	protected Double variation;
	
	protected Double trendProbability;
	
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
	public RandomUtilityFunction(Double utility, Double variation, Double drift,Double trendProbability) {
		super();
		this.utility = utility;
		this.drift = drift;
		this.variation = variation;
		this.trendProbability = trendProbability;
		this.random = new MersenneRandom((int)System.currentTimeMillis());
		this.direction = getInitialTrend();
		
		
	}



	@Override
	public Double getUtility(Node n) {
		
		direction = direction*getTrend();
		
		Double newUtility = utility + getVariation() +direction*drift;
		
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

	private Double getVariation() {
		return variation*(random.nextDouble()*2.0-1.0);
	}
	
	private Double getTrend() {
		if(random.nextDouble() < trendProbability) {
			return 1.0;
		}
		else {
			return -1.0;
		}
	}
	
	private Double getInitialTrend() {
		
		if(random.nextGaussian() < 0) 
			return -1.0;
		else
			return 1.0;
		
	}

}

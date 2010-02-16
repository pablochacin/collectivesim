package edu.upc.cnds.collectivesim.overlay.utility;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * Returns a random utility value
 * 
 * @author 
 *
 */
public class RandomUtilityFunction implements UtilityFunction {

	protected Stream<Double> variation;
	
	protected Stream<Double> trend;
	
	protected Double direction;
	
	protected Double utility;

	protected Double drift;
	/**
	 * Create a random walk with an initial value and a direction
	 * @param direction
	 * @param utility
	 */
	public RandomUtilityFunction(Double utility, Double drift,Double direction,
			                     Stream<Double> variation,Stream<Double>trend) {
		super();
		this.direction = direction;
		this.utility = utility;
		this.drift = drift;
		this.variation = variation;
		this.trend = trend;
	}



	@Override
	public Double getUtility(Node n) {
		
		direction = direction*trend.nextElement();
		
		Double newUtility = utility + variation.nextElement() +direction*drift;
		
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

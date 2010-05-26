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
public class WhiteNoiseUtilityFunction implements UtilityFunction {


	
	protected Stream<Double> variation;
		
	
	/**
	 * Initial utility
	 */
	protected Double base;
	
	Random random;
	
	/**
	 * Create a random walk with an initial value and a direction
	 * @param direction
	 * @param utility
	 */
	public WhiteNoiseUtilityFunction(Double base,Stream<Double> variation) {
		super();
		this.base = base;
		this.variation = variation;
		this.random = Collectives.getExperiment().getRandomGenerator();		
		
	}



	@Override
	public Double getUtility(Node n) {
		
		
		Double var = variation.nextElement();
		
		//ensure that 0 <= utility+variation `<= 1
		if(var < 0)
			return Math.max(base+var,0.0);
		else 
			return Math.min(base+var, 1.0);
		}
									

}

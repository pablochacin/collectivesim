package collectivesim.stream.base;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Beta;
import cern.jet.random.Gamma;
import cern.jet.random.Normal;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.MersenneTwister64;
import collectivesim.stream.Stream;


/**
 * Generates a random stream 
 * 
 * @author Pablo Chacin
 *
 */
public class RandomStream implements Stream<Double>{

	
	private AbstractDistribution distribution;
	
	public RandomStream(AbstractDistribution distribution){
		this.distribution = distribution;
	}
	

	@Override
	public Double nextElement() {
		return distribution.nextDouble();
	}

	@Override
	public void reset() {
		//Do nothing
	}

	@Override
	public void open() {
		// Do nothing.
		
	}
	
	public String toString(){
		return "Random distribution class " + this.distribution.getClass().getName(); 
	}
	
	@Override
	public boolean hasMoreElements(){
		return true;
	}
	
	
	
}

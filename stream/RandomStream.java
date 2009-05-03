package edu.upc.cnds.collectivesim.stream;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Beta;
import cern.jet.random.Gamma;
import cern.jet.random.Normal;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.MersenneTwister64;


/**
 * Generates a random stream 
 * 
 * @author Pablo Chacin
 *
 */
public class RandomStream implements Stream<Double>{

	private String name;
	
	private AbstractDistribution distribution;
	
	public RandomStream(String name,AbstractDistribution distribution){
		this.name = name;
		this.distribution = distribution;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Double getValue() {
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
	
	public static void main(String[] args){
		double mean = 0.3;
		double stdev = 0.1;
		double alpha = mean*mean/stdev;
		double beta = mean/stdev;
		
		RandomStream stream = new RandomStream("",new Gamma(alpha,beta,new MersenneTwister64()));
		
		Double value = 0.0;
		for(int i = 0;i < 1000;i++){
			value = stream.getValue();
			System.out.println(value);
		}
	}
}

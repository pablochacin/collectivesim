package edu.upc.cnds.collectivesim.model.samplers;

import java.util.ArrayList;
import java.util.List;

import cern.jet.random.engine.MersenneTwister64;

import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.ModelAgent;

/**
 * Makes a sample of (approximately) k agents following a Poisson process.
 * When this process is repeated, the average number of agents selected is
 * k, but on each invocation, it may vary (with a variace of sqrt(k).
 * 
 * If there are n agents, each agent will have a probability of k/n of being selected
 * in the sample.
 * 
 * @author Pablo Chacin
 *
 */
public class PoissonSampler implements AgentSampler {

	protected Double n;

	protected MersenneTwister64 rand;
	
	public PoissonSampler(Double n, MersenneTwister64 rand){
		this.n = n;
		this.rand = rand;
	}
	
	@Override
	public List<ModelAgent> sample(List<ModelAgent> agents) {
		List<ModelAgent> sample = new ArrayList<ModelAgent>();
		
		Double p = n/(double)agents.size();
		for(ModelAgent n: agents){
			if(rand.nextDouble() < p){
				sample.add(n);
			}
		}
		
		return sample;
		
	}

}

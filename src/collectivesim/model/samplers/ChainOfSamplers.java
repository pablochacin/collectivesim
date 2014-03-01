package collectivesim.model.samplers;

import java.util.List;

import collectivesim.model.AgentSampler;
import collectivesim.model.ModelAgent;

/**
 * Applies a series of {@link AgentSampler}s to a list of agents
 * in a chain and returns the resulting sample.
 * 
 * Assumes each sampler returns a new list of agents even if it doesn't
 * modify the input list.
 *  
 * @author PablO Chacin
 *
 */
public class ChainOfSamplers implements AgentSampler {

	protected AgentSampler[] samplers;
	
	public ChainOfSamplers(AgentSampler ...samplers){
		this.samplers = samplers;
	}
	@Override
	public List<ModelAgent> sample(List<ModelAgent> agents) {
	 List<ModelAgent> sample = agents;
	 for(AgentSampler sampler: samplers){

		 sample = sampler.sample(sample);
	 }
	
	 return sample;
		
	}

}

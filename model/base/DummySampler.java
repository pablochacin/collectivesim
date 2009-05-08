package edu.upc.cnds.collectivesim.model.base;

import java.util.List;

import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.ModelAgent;

/**
 * Simple AgentSampler that returns all the agents.
 * 
 * @author Pablo Chacin
 *
 */
public class DummySampler implements AgentSampler{

	@Override
	public List<ModelAgent> sample(List<ModelAgent> agents) {
		return agents;
	}
	
}

package collectivesim.model.base;

import java.util.List;

import collectivesim.model.AgentSampler;
import collectivesim.model.ModelAgent;

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

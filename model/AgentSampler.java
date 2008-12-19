package edu.upc.cnds.collectivesim.model;

import java.util.List;

/**
 * Samples a list of Agents and returns a sublist 
 * 
 * @author pchacin
 *
 */
public interface AgentSampler {
	
	/**
	 * Retuns a (potentially empty, but not null) sublist of the agents
	 * according to certain sampling criteria
	 * 
	 * @param agents list of agents
	 * 
	 * @return a sublist
	 */
	public List<ModelAgent> sample(List<ModelAgent> agents); 

}

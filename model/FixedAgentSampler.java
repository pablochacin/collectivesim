package edu.upc.cnds.collectivesim.model;

import java.util.ArrayList;
import java.util.List;

/**
 * AgentSampler that returns always the same Agent, which is given in its constructor
 * 
 * Notice that current implementation doesn't check that the agent still is in the
 * model.
 * 
 * @author Pablo Chacin
 *
 */
public class FixedAgentSampler implements AgentSampler {

	private ModelAgent agent;
	
	public FixedAgentSampler(ModelAgent agent){
		this.agent = agent;
	}
	
	@Override
	public List<ModelAgent> sample(List<ModelAgent> agents) {
	  List<ModelAgent> agentList = new ArrayList<ModelAgent>();
	  agentList.add(agent);
	  return agentList;
	}

}

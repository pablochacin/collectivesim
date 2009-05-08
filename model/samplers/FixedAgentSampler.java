package edu.upc.cnds.collectivesim.model.samplers;

import java.util.ArrayList;
import java.util.List;

import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.ModelAgent;

/**
 * AgentSampler that returns the agent with the given name.
 * 
 * 
 * TODO: Current implementation look for an agent  on each iteration,but this is highly 
 * inefficient. The Model offers the getAgent(Sting name=) method, but currently the sampler
 * doesn't have access to the model.
 * 
 * @author Pablo Chacin
 *
 */
public class FixedAgentSampler implements AgentSampler {

	private String name;
	
	public FixedAgentSampler(String name){
		this.name = name;
	}
	
	@Override
	public List<ModelAgent> sample(List<ModelAgent> agents) {
	  List<ModelAgent> agentList = new ArrayList<ModelAgent>();
	  
	  for(ModelAgent a: agents){
		 if(a.getName().equals(name)){
	       agentList.add(a);
	       break;
	     }
	  }
	  
	  return agentList;
	}

}

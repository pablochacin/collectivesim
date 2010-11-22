package edu.upc.cnds.collectivesim.adaptation;

import edu.upc.cnds.collectives.adaptation.AdaptationEnvironment;
import edu.upc.cnds.collectives.adaptation.action.AdaptationAction;
import edu.upc.cnds.collectivesim.model.ModelAgent;

public class AgentAdaptationAction implements AdaptationAction {

	protected ModelAgent agent;
	
	protected String method;
	
	@Override
	public boolean trigger(AdaptationEnvironment environment) throws Exception {
		agent.execute(method);

		return true;
	}

}

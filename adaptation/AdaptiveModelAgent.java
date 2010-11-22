package edu.upc.cnds.collectivesim.adaptation;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.adaptation.AdaptationEnvironment;
import edu.upc.cnds.collectives.adaptation.AdaptationManager;
import edu.upc.cnds.collectives.adaptation.action.AdaptationAction;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.base.ReflexionModelAgent;

public class AdaptiveModelAgent extends ReflexionModelAgent implements
		AdaptationManager, AdaptationEnvironment{

	protected Map<String,AdaptationAction> actions = new HashMap<String, AdaptationAction>(); 
	
	public AdaptiveModelAgent(Object target) {
		super(target);
	}

	public AdaptiveModelAgent(String name, Object target) {
		super(name, target);
	}

	public AdaptiveModelAgent(String name) {
		super(name);
	}

	public AdaptiveModelAgent() {
		super();
	}



	@Override
	public void addAction(String name, AdaptationAction action) {
		actions.put(name,action);
		
	}


	@Override
	public String[] getActionNames() {
		throw new UnsupportedOperationException();
	}


	@Override
	public boolean trigger(String name) throws Exception {
		return actions.get(name).trigger(this);
	}

	@Override
	public String[] getAttributeNames(){
		return super.getAttributeNames();		
	}

	@Override
	public Map<String, Object> getAttributes() {
	 
		return inquire(getAttributeNames());
	}


	public Map<String, Object> getAttributes(String[] attributes) {
		 
		return inquire(attributes);
	}
	
	@Override
	public Object getAttribute(String attribute) {
		try {
			return inquire(attribute);
		} catch (ModelException e) {
			return null;
		}
	}

	
}

package edu.upc.cnds.collectivesim.collective.imp;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.collective.Collective;
import edu.upc.cnds.collectives.collective.CollectiveAction;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.agents.Agent;
import edu.upc.cnds.collectivesim.agents.AgentException;
import edu.upc.cnds.collectivesim.collective.CollectiveException;
import edu.upc.cnds.collectivesim.overlay.Overlay;

/**
 * Representes the local interface to the Collective for agents in a Node
 *  
 *  *  
 * @author Pablo Chacin
 *
 */
public class CollectiveAgent implements Collective, Agent {

	/**
	 * Maps actions to objects that execute it
	 */
	private Map<String,CollectiveAction> actions;

	
	/**
	 * Maps attributes to objects that return it
	 */
	private Map<String,Object> attributes;
	
	private Collective collective;
	
	private UnderlayNode node;
	
	private Overlay overlay;
	
	
	public CollectiveAgent(Collective collective,UnderlayNode node,Overlay overlay) {
		this.collective = collective;
		this.node = node;
		this.overlay = overlay;
		this.actions = new HashMap<String,CollectiveAction>();
		this.attributes = new HashMap<String,Object>();
	}



	public void visit(String actionName,Object[] arguments) throws CollectiveException{
		
		CollectiveAction action = actions.get(actionName);
		if(action == null) {
			throw new CollectiveException("Unknown action "+actionName);
		}
		
	}


	public Map inquire(String attribute) {
		throw new UnsupportedOperationException();
	}

	
	
	public Object handleInquire(String attribute) throws AgentException {
	
		Object target = actions.get(attribute);
		if(target == null) {
		 throw new AgentException("Attributed not registered: "+attribute);	
		}
		
		try {

			Method method = target.getClass().getMethod("get"+attribute, (Class[])(null));
			Object result = method.invoke(target, (Object[])(null));
			return result;

		} catch (Exception e) {
			throw new AgentException("Exception accessing attribute "+attribute,e);
		} 
	}


	public UnderlayNode getNode() {
		return node;
	}


	public void registerActionTarget(String action, Object  target) {
		throw new UnsupportedOperationException();
		
	}

	public void registerAttributeTarget(String attribute, Object target) {
		throw new UnsupportedOperationException();
	}



	public void handleVisit(String action, Object[] args) throws AgentException {
		throw new UnsupportedOperationException();
		
	}


}

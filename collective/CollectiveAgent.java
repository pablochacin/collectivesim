package edu.upc.cnds.collectivesim.collective;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.collective.Collective;
import edu.upc.cnds.collectives.collective.CollectiveAction;
import edu.upc.cnds.collectives.collective.imp.CollectiveActionImp;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;

/**
 * Represents the local interface to the Collective for agents in a Node
 *  
 *  *  
 * @author Pablo Chacin
 *
 */
public class CollectiveAgent implements Collective, ModelAgent {

	/**
	 * Maps actions to objects that execute it
	 */
	private Map<String,CollectiveAction> actions;

		
	private UnderlayNode node;
	
	private Overlay overlay;
	
	private String type;
	
	public CollectiveAgent(String type, Object target, UnderlayNode node,Overlay overlay) {
		this.type = type;
		this.node = node;
		this.overlay = overlay;
		this.actions = new HashMap<String,CollectiveAction>();

	}



	public void visit(String actionName,Object[] arguments) throws ModelException{
		
		CollectiveAction action = actions.get(actionName);
		if(action == null) {
			throw new ModelException("Unknown action "+actionName);
		}
		
	}


	public Map inquire(String attribute) {
		throw new UnsupportedOperationException();
	}

	
	
	public UnderlayNode getNode() {
		return node;
	}


	public void registerActionTarget(String name, Object  target,String method) {
		
		CollectiveAction action = new CollectiveActionImp(name,target,method,null);
		actions.put(name,action);
		
	}

	public void registerAttributeTarget(String name, Object target,String attribute) {

		CollectiveAction action = new CollectiveActionImp(name,target,"get"+attribute,null);
		actions.put(name,action);

	}

	public void execute(String name, Object[] args) throws ModelException {
		
		CollectiveAction action = actions.get(name);
		if(action == null) {
			 throw new ModelException("Action not registered: "+name);				
		}
		
		try {

			action.execute(args);
			
		} catch (Exception e) {
			throw new ModelException("Exception executing action "+name,e);
		} 
	}


	public Object getAttribute(String attribute) throws ModelException {
		
		CollectiveAction action = actions.get(attribute);
		if(attribute == null) {
		 throw new ModelException("Attribute not registered: "+attribute);	
		}
		
		try {

			Object result = action.execute();
			
			return result;

		} catch (Exception e) {
			throw new ModelException("Exception accessing attribute "+attribute,e);
		} 
	}



	public String getName() {
		return type;
	}
}

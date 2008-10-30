package edu.upc.cnds.collectivesim.collective.imp;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeAccessException;
import edu.upc.cnds.collectivesim.agents.Agent;
import edu.upc.cnds.collectivesim.agents.AgentException;
import edu.upc.cnds.collectivesim.collective.Collective;
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
	private Map<String,Object> actions;

	
	/**
	 * Maps attributes to objects that return it
	 */
	private Map<String,Object> attributes;
	
	private Collective collective;
	
	private Node node;
	
	private Overlay overlay;
	
	
	public CollectiveAgent(Collective collective,Node node,Overlay overlay) {
		this.collective = collective;
		this.node = node;
		this.overlay = overlay;
		this.actions = new HashMap<String,Object>();
		this.attributes = new HashMap<String,Object>();
	}



	public void visit(String method,Object[] arguments) {
		
		for(Node n: overlay.getNeighbors(node)) {
			try {
				n.visit(method, arguments);
			} catch (NodeAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}


	public Map inquire(String attribute) {
		throw new UnsupportedOperationException();
	}

	
	/**
	 * Allows the Collective to visit the agent in this node
	 * 
	 * @param action name of the action. The target registered with this action
	 *        must implement a method of the same name
	 * @param args an array of objects to be passed to the method
	 * 
	 * @throws AgentException if the method couldn't be executed or it thrown an exception 
	 */
	public void handleVisit(String action,Object[] args) throws AgentException{
	
		Object target = actions.get(action);
		if(target == null) {
		 throw new AgentException("Method not registered: "+action);	
		}
		
		try {
			Class[] classes = new Class[args.length];
			for(int i=0;i <args.length;i++){
				classes[i] = args[i].getClass();
			}
			
			Method method = target.getClass().getMethod(action, classes);

			method.invoke(target, args);

		} catch (Exception e) {
			throw new AgentException("Exception accessing executing method " + action,e);
		} 

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


	public Node getNode() {
		return node;
	}


	public void registerAction(String action, Object target) {
		actions.put(action, target);
		
	}

	public void registerAttribute(String attribute, Object target) {
		attributes.put(attribute, target);
	}


}

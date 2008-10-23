package edu.upc.cnds.collectivesim.agents;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.collective.Collective;
import edu.upc.cnds.collectivesim.models.Model;

public interface Agent {

	
	/**
	 * Returns the type of the agent. Use for filtering agents 
	 * in behavors and observers.
	 * 
	 * @return
	 */
	public String getAgentType();
	
	
	/**
	 * @param attribute a String with the name of the attribute
	 * 
	 * @return the value of the given attribute
	 * 
	 * @throws AgentException if the attribute is not exposed by the agent
	 */
	public Object getAttribute(String attribute) throws AgentException;
	
	/**
	 * Executes the given method in the context of one AgentContext
     * 
     * 
	 * @param collective the collective on which the action occures
	 * @param method
	 * 
	 * @return an Object with the method's result, or null if none.
	 * 
	 * @throws  AgentException if the method can't be executed
	 */
	public Object execute(Collective collective,String method) throws AgentException;
 
	/**
	 * Executes the given method in the context of one AgentContext
	 * passing an array of arguments
     * 
	 * @param collective the collective on which the action occures
	 * @param method name of the method to be executed
	 * @param arguments an array (potentially null) of parameters to be passed to the method
	 * 
	 * @return an Object with the method's result, or null if none.
	 * 
	 * @throws  AgentException if the method can't be executed
	 */
	public Object execute(Collective collective,String method,Object[] arguments) throws AgentException;

	/**
	 * Returns an unique identifier for this agent
	 * 
	 * @return
	 */
	public Identifier getId();
	
	
	/**
	 * @return the node the agent resides in
	 */
	public Node getNode();
	
	
	/**
	 * Sets the node on which the agent resides
	 * 
	 * @param node Node on which the agent resides
	 */
	public void setNode(Node node);
	
	
	/**
	 * Sets the Model from which the agent can take simulation parameters
	 * 
	 * @param model
	 */
	public void setModel(Model model);
	
	
	/**
	 * 
	 * @return the Model used by the agent to access simulation information
	 */
	public Model getModel();
	
}

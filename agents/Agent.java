package edu.upc.cnds.collectivesim.agents;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.collective.Collective;

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
	public Object handelInquire(String attribute) throws AgentException;
	
	/**
	 * Executes the given method in the context of one AgentContext
     * 
     * 
	 * @param collective the collective on which the action occures
	 * @param method
	 * 
	 * @throws  AgentException if the method can't be executed
	 */
	public void handleVisit(Collective collective,String method) throws AgentException;
 
	/**
	 * Executes the given method in the context of one AgentContext
	 * passing an array of arguments
     * 
	 * @param collective the collective on which the action occures
	 * @param method name of the method to be executed
	 * @param arguments an array (potentially null) of parameters to be passed to the method
	 * 
	 * @throws  AgentException if the method can't be executed
	 */
	public void handleVisit(Collective collective,String method,Object[] arguments) throws AgentException;

	
	/**
	 * 
	 * @return returns the Identifier of this agent, which must be unique.
	 */
	public Identifier getId();
}

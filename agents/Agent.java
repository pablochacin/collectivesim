package edu.upc.cnds.collectivesim.agents;

/**
 * Represents the application that handles the events in a node
 * 
 * @author Pablo Chacin
 *
 */
public interface Agent {

	
	
	/**
	 * 
	 * Handle the inquire for an attribute
	 * 
	 * @param attribute a String with the name of the attribute
	 * 
	 * @return the value of the given attribute
	 * 
	 * @throws AgentException if the attribute is not exposed by the agent
	 */
	public Object handleInquire(String attribute) throws AgentException;
	
	
	/**
	 * Handle the invocation of an action
	 * @param methodName
	 * @param args
	 * @throws AgentException
	 */
	public void handleVisit(String action,Object[] args) throws AgentException;
	
	
}

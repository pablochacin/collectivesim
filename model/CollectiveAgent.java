package edu.upc.cnds.collectivesim.model;

/**
 * Represent the interface to the collective agent running in node used by the
 * simulation of the collective to execute actions and access attributes
 * 
 * @author Pablo Chacin
 *
 */
public interface CollectiveAgent {

	
	
	/**
	 * 
	 * Inquire an attribute
	 * 
	 * @param attribute a String with the name of the attribute
	 * 
	 * @return the value of the given attribute
	 * 
	 * @throws AgentException if the attribute is not exposed by the agent
	 */
	public Object inquireAttribute(String attribute) throws CollectiveException;
	
	
	/**
	 * Handle the invocation of an action
	 * @param methodName
	 * @param args
	 * @throws AgentException
	 */
	public void executeAction(String action,Object[] args) throws CollectiveException;
	
	
}

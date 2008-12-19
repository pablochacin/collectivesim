package edu.upc.cnds.collectivesim.model;

/**
 * Represent the interface to the model agent: an entity that exposes attributes
 * and methods to the simulation model.
 * 
 * @author Pablo Chacin
 *
 */
public interface ModelAgent {

	
	
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
	public Object inquireAttribute(String attribute) throws ModelException;
	
	
	/**
	 * Handle the invocation of an action
	 * 
	 * @param methodName
	 * @param args
	 * @throws AgentException
	 */
	public void executeAction(String action,Object[] args) throws ModelException;
	
	
}

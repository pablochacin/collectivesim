package edu.upc.cnds.collectivesim.model;

import java.util.Map;

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
	 * @return the type of the agent.
	 */
	public String getName();
	
	
	/**
	 * 
	 * @return an array with the name of the attributes exposed by the agent.
	 */
	public String[] getAttributeNames();
	
	/**
	 * 
	 * Inquire an attribute of the agent.
	 * 
	 * @param attribute a String with the name of the attribute
	 * 
	 * @return the value of the given attribute
	 * 
	 * @throws AgentException if the attribute is not exposed by the agent
	 */
	public Object inquire(String attribute) throws ModelException;
	
	
	/**
	 * Returns all the attributes exposed by the agent
	 * @return
	 */
	public Map<String,Object>inquire();
	
	/**
	 * Handle the invocation of a method in the agent
	 * 
	 * @param methodName
	 * @param args
	 * @throws AgentException if the method is not exposed by the agent or
	 *         there is an exception executing it.
	 */
	public void execute(String action,Object[] args) throws ModelException;
	
	/**
	 * Convenience method, for invocations without arguments
	 * @param action
	 * @throws ModelException
	 */
	public void execute(String action) throws ModelException;
	
	
	/**
	 * Initializes the Agent. Called before when the model is ready to start and before
	 * any behavior is executed. 
	 * 
	 */
	public void init();
	
	
	/**
	 * Finalize the Agent. Called after the Model has been stoped. 
	 */
	public void finish();

	
	/**
	 * Convenience method, retrieves the values for a given list of attribute Names.
	 * 
	 * @param attributeNames
	 * @return
	 */
	Map<String, Object> inquire(String[] attributeNames);


	/**
	 * Returns the model on which this agent lives
	 * 
	 * @return
	 */
	public Model getModel();
	
	
}

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
	 * @return the type of the agent.
	 */
	public String getName();
	
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
	public Object getAttribute(String attribute) throws ModelException;
	
	
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
	
	
}

package edu.upc.cnds.collectivesim.model;

/**
 * A function that operates on an agent and returns a value
 * 
 * @author Pablo Chacin
 *
 */
public interface AgentFunction {
	
	/**
	 * Resets the function for a new iteration
	 */
	public void reset();
	
	/**
	 * Calculates a value based on the agent's attributes
	 * TODO: how avoid that the function invokes method in the agent?
	 * 
	 * @param agent
	 * 
	 * @return a single value
	 */
	public Object process(ModelAgent agent);
	
}

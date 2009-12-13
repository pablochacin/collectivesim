package edu.upc.cnds.collectivesim.model;

import java.util.Map;


/**
 * 
 * A factory to create ModelAgents from a set of arguments
 * 
 * @author Pablo Chacin
 *
 */
public interface AgentFactory <M extends Model<? extends A>,A extends ModelAgent> {

	/**
	 * Creates a new {@link ModelAgent} ready to be inserted in a model.
	 *
	 * @param model Model on which the agent will be created
	 * @param args a variable set of arguments to be used in the construction of the agent
	 * 
	 * @return a new instance of a ModelAgent
	 * @throws ModelException 
	 */
	public A createAgent(M model, Map<String,Object> args) throws ModelException;
}

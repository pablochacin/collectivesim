package edu.upc.cnds.collectivesim.model;

import java.util.List;

import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelObserver;

public interface Model {

	/**
	 * Adds a behavior to agents in the collective, defined as the execution of a method. 
	 *
	 * @param name a String that identifies the behavior
	 * @param method a Strings with the name of the method
	 * @param sampler an AgentSampler used to select the agents on which the behavior will be applied
	 * @param streams an array with the Streams to feed the method invocation
	 * @param active a boolean that indicates if the behavior must be inserted active
	 *         or will be inactive until the model activates it.
	 * @param iterations the maximum of executions of the behavior         
	 * @param frequency a Stream of Long that specifies the frequency of execution of the behaviors
	 * @param delay the time the behavior will begin to execute. 0 means start running immediately.
	 * @param endTime the maximum time the executions will be scheduled
	 * @param args a variable number of Streams of that are used to retrieve the arguments for the
	 *        behavior's method on each iteration
	 */
	public abstract void addBehavior(String name, String method,AgentSampler sampler,
			boolean active, int iterations,Stream<Long> frequency, long delay, long endTime,
			Stream<? extends Object> ... args);

	/**
	 * Convenience method to specify the frequency as fixed value
	 * 
	 * @param name a String that identifies the behavior
	 * @param method a Strings with the name of the method
	 * @param sampler an AgentSampler used to select the agents on which the behavior will be applied
	 * @param streams an array with the Streams to feed the method invocation
	 * @param active a boolean that indicates if the behavior must be inserted active
	 *         or will be inactive until the model activates it.
	 * @param iterations the maximum of executions of the behavior         
	 * @param frequency a long that specifies the frequency of execution of the behavior
	 * @param delay the time the behavior will begin to execute. 0 means start running immediately.
	 * @param endTime the maximum time the executions will be scheduled
	 * @param args a variable number of Streams of that are used to retrieve the arguments for the
	 *        behavior's method on each iteration
	 */
	public abstract void addBehavior(String name, String method,AgentSampler sampler,
			boolean active, int iterations,long frequency, long delay, long endTime,
			Stream<? extends Object> ... args);

	/**
	 * Convenience method, to add a behavior that starts immediately, has neither end time nor
	 * maximum number of iterations and don't use an AgentSampler to select agents.
	 * 
	 * @param name
	 * @param method
	 * @param active
	 * @param frequency
	 * @param args
	 */
	public abstract void addBehavior(String name, String method, long frequency, Stream<? extends Object>... args);

	
	/**
	 * Convenience method, to add a behavior that starts immediately, has neither end time nor
	 * maximum number of iterations and don't use an AgentSampler to select agents.
	 * 
	 * @param name
	 * @param method
	 * @param active
     * @param iterations
	 * @param frequency
	 * @param args
	 */
	public abstract void addBehavior(String name, String method, int iterations,long frequency, Stream<? extends Object>... args);


	/**
	 * Convenience method, to add a behavior that starts immediately, has neither end time nor
	 * maximum number of iterations and don't use an AgentSampler to select agents.
	 * 
	 * @param name
	 * @param method
     * @param iterations
	 * @param frequency
	 * @param args
	 */
	public abstract void addBehavior(String name, String method,
			long frequency, long delay,long endTime,Stream<? extends Object>... args);


	/**
	 * Adds an observer to calculate an attribute over the agents of the collective and generate 
	 * a DataSeries with the resulting values
	 * 
	 * @param name a String with the name of the observer
	 * @param observer a ModelObserver that will receive the value of the attribute
	 * @param sampler an AgentSampler used to select the agents from which the attributes will be picked
	 * @param attribute the name of the attribute
	 * @param frequency the frequency of update
	 */
	public abstract void addObserver(String name, ModelObserver observer,AgentSampler sampler,
			String attribute, boolean active, long frequency);

	
	
	/**
	 * 
	 * @return a list of the agents in the model
	 */
	public abstract List<ModelAgent> getAgents();

	/**
	 * pause the execution of the model
	 */
	public abstract void pause();

	/**
	 * starts the execution of the model
	 * @throws ModelException 
	 */

	public abstract void start() throws ModelException;

	/**
	 * Returns the current simulation time for the model
	 * @return
	 */
	public long getCurrentTime();
	
	/**
	 * Returns the experiment this model lives in
	 * @return
	 */
	public Experiment getExperiment();
	
	/**
	 * 
	 * @return the name of the model
	 */
	public String getName();
	
	
	/**
	 * Returns a reference to the given agent
	 * @param name
	 * @return
	 */
	public ModelAgent getAgent(String name);
}
package collectivesim.model;

import java.util.List;

import collectives.factory.Factory;
import collectivesim.dataseries.DataSeries;
import collectivesim.dataseries.SeriesFunction;
import collectivesim.experiment.Experiment;
import collectivesim.model.ModelAgent;
import collectivesim.stream.Stream;

public interface Model <T extends ModelAgent> {

	
	public static int EXECUTION_ORDER_BEFORE = Integer.MIN_VALUE;
	
	public static int EXECUTION_ORDER_RANDOM = 0;
	
	public static int EXECUTION_ORDER_AFTER  = Integer.MAX_VALUE;
	
	
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
	 * @param priority order of execution of the behavior during the cycle
	 * @param args a variable number of Streams of that are used to retrieve the arguments for the
	 *        behavior's method on each iteration
	 */
	public abstract void addBehavior(String name, String method,AgentSampler sampler,
			boolean active, int iterations,long frequency, long delay, long endTime,int priority,
			Stream<? extends Object> ... args);

	public abstract void addBehavior(String name, String method,
			AgentSampler sampler, boolean active, int iterations, long frequency, long delay, long endTime,Stream<? extends Object> ... args);

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

	

		
	public abstract void addCompositeBehavior(String name, CompositeBehavior behavior,
			boolean active, int iterations,long frequency, long delay, long endTime);
	
	/**
	 * Convenience method to generate a series from a single attribute
	 * @param name
	 * @param sampler
	 * @param attribute
	 * @param values
	 * @param reset
	 * @param frequency
	 */
	public abstract void addObserver(String name,AgentSampler sampler,
			                      String attribute, DataSeries values,boolean reset,long delay,long frequency);

	/**
	 * 
	 * @param name
	 * @param sampler
	 * @param attribute
	 * @param values
	 * @param function
	 * @param reset
	 * @param delay
	 * @param frequency
	 */
	public abstract void addObserver(String name,AgentSampler sampler,
            String attribute, DataSeries values,SeriesFunction function,boolean reset,long delay,long frequency);

	
	
	/**
	 * Creates an agent from a factory. 
	 */
	public ModelAgent createAgent(Factory<T> factory) throws ModelException;

	

	/**
	 * 
	 * Creates a stream of new agents that are created in the model periodically, 
	 * 
	 * @param delay
	 * @param endTime
	 * @param frequency
	 * @param rate
	 * @param factory
	 * @param args
	 */
	public void addAgentStream(String name,long delay,long endTime,Stream<Long>frequency,Stream<Integer> rate,Factory<T> factory);


	public void addAgentStream(String name,long delay,long endTime,Stream<Long>frequency,Stream<Integer> rate);

	
	
	/**
	 * 
	 * @return a list of the agents in the model
	 */
	public abstract List<T> getAgents();

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
	 * Resets the model's state and prepares it for a new run.
	 * This method is invoked after the execution of the model
	 * (it is not invoked before the first execution)
	 */
	public void reset();
	
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
	public T getAgent(String name);

	/**
	 * Resume execution of Model (behaviors and observers)
	 */
	public void resume();
	
	/**
	 * Add a submodel for the model
	 * 
	 * @param m
	 */
	public void addSubModel(Model<? extends T> model);
	
	
	/**
	 * indicates if the model is being debugged
	 * 
	 * @return
	 */
	public boolean isDebugging();
		
	/**
	 * Indicates if the actions of the model must be debugged (true) or not (false)
	 * @param debug
	 */
	public void setDebug(boolean debug);

}
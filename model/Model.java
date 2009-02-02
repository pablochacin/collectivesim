package edu.upc.cnds.collectivesim.model;

import java.util.List;

import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelObserver;
import edu.upc.cnds.collectivesim.scheduler.Stream;

public interface Model {

	/**
	 * Adds a behavior to agents in the collective, defined as the execution of a method. 
	 *
	 * @param name a String that identifies the behavior
	 * @param method a Strings with the name of the method
	 * @param streams an array with the Streams to feed the method invocation
	 * @param active a boolean that indicates if the behavior must be inserted active
	 *         or will be inactive until the model activates it.
	 * @param frequency a double that specifies the frequency of execution of the behaviors
	 */
	public abstract void addBehavior(String name, String method,
			boolean active, Stream<Long> frequency, int iterations, long endTime,
			Stream<Object> ... args);

	/**
	 * Convenience method. Receives the frequency as a long and converts it to a SingleValueStream.
	 * 
	 * @param name
	 * @param method
	 * @param active
	 * @param frequency
	 * @param iterations
	 * @param endTime
	 * @param args
	 */
	public abstract void addBehavior(String name, String method,
			boolean active, long frequency, int iterations, long endTime,
			Stream<Object>... args);

	/**
	 * Convenience method, adds a behavior without endtime nor maximun iterations
	 * 
	 * @param name
	 * @param method
	 * @param active
	 * @param frequency
	 * @param args
	 */
	public abstract void addBehavior(String name, String method,
			boolean active, long frequency, Stream<Object>... args);

	/**
	 * Convenience method, defines a behavior with a maximum of iterations
	 * @param name
	 * @param method
	 * @param active
	 * @param frequency
	 * @param iterations
	 * @param args
	 */
	public abstract void addBehavior(String name, String method,
			boolean active, long frequency, int iterations, Stream<Object> ... args);

	/**
	 * Convenience method, defined a behavior with a end time
	 * 
	 * @param name
	 * @param method
	 * @param active
	 * @param frequency
	 * @param endTime
	 * @param args
	 */
	public abstract void addBehavior(String name, String method,
			boolean active, long frequency, long endTime, Stream<Object>... args);

	/**
	 * Adds an observer to calculate an attribute over the agents of the collective and generate 
	 * a DataSeries with the resulting values
	 * 
	 * @param name a String with the name of the observer
	 * @param operator the Operator used to calculate the attribute
	 * @param attribute the name of the attribute
	 * @param frequency the frequency of update
	 */
	public abstract void addObserver(String name, ModelObserver observer,
			String attribute, boolean active, long frequency);

	public abstract List<ModelAgent> getAgents();

	/**
	 * pause the execution of the model
	 */
	public abstract void pause();

	/**
	 * starts the execution of the model
	 */

	public abstract void start();

	/**
	 * Returns the current simulation time for the model
	 * @return
	 */
	public long getCurrentTime();
}
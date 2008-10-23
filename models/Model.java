package edu.upc.cnds.collectivesim.models;

import java.util.Map;

import edu.upc.cnds.collectivesim.metrics.Metric;


public interface Model {

	/**
	 * Get the parameter indicating the number of agents currently actvive in this model
	 * @return the number of agents in the model
	 */
	public abstract int getNumActiveAgents();

	/**
	 * Returns a random real value
	 */
	public abstract Double getRandomDouble();

	public abstract Boolean getBooleanProperty(String property);

	public abstract Double getDoubleProperty(String property);

	public abstract Object getObjectProperty(String property);

	/**
	 * Reports a metric with a Metric object that describes its name, current value 
	 * and attributes. 
	 * 
	 * The format of the report depends on the configuration of the P2pAgentHosting.
	 * 
	 * @param metric a Metrics object describing the metric
	 */
	public abstract void reportMetric(Metric metric);

	/**
	 * Reports a metric. The reported metric will have as default attributes the current time stamp
	 * and the agent that issued the metric.
	 * @param name of the metric
	 * @param value of the metric
	 * @param attributes a Map with a set of (key,value) pairs of attributes
	 *        that qualifies the metric
	 */
	public abstract void reportMetric(String name, String value, Map attributes);

	/**
	 * A convenience method to report a metric without attributes
	 * 
	 * @param name a String with the identification of the metric
	 * @param value a String with the value of the metric
	 */
	public abstract void reportMetric(String name, String value);

	/**
	 * Returns the current simulation time
	 * @return
	 */
	public abstract Double getTime();

	/**
	 * Schedules an action
	 *
	 */
	public abstract void scheduleAction(Action action);
    
    /**
     * Returns the next value of a random stream
     * 
     * @return a Double with the next random number in the random stream
     * 
     * @throws an InvalidStateException if the random stream has not been initialized
     */
    Double getStreamValue(String stream);
}
package collectivesim.events;

import java.io.Serializable;
import java.util.Map;

import collectivesim.model.Model;
import collectivesim.model.ModelAgent;
import collectivesim.util.TypedMap;


public interface Event extends Comparable,Serializable{

	
	/**
	 * 
	 * @return the time at which this event was reported
	 */
	public long getTimestamp();

	/**
	 * 
	 * @return the event type
	 */
	public String getType();

	/**
	 * 
	 * @return the attributes of this event
	 */
	public TypedMap getAttributes();


	/**
	 * return the name of the associated agent
	 * @return
	 */
	public ModelAgent getAgent();
	
	/**
	 * Returns the name of the model on which the event occurred
	 */
	public Model getModel();
}
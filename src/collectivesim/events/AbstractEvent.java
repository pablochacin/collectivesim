package collectivesim.events;

import java.util.HashMap;
import java.util.Map;

import collectivesim.events.Event;
import collectivesim.model.Model;
import collectivesim.model.ModelAgent;
import collectivesim.util.TypedMap;


/**
 * Represents an event associated with an ModelAgent.
 * 
 * @author Pablo Chacin
 *
 */
public abstract class AbstractEvent implements Event{

	/**
	 * Model on which the event hapenned
	 */
	private Model model;
	
	
	/**
	 * Agent related to the event
	 */
	private ModelAgent agent; 

	/**
	 * time when this event was fired (as reported)
	 */
	private long timestamp;

	/**
	 * Type of event
	 */
	private String type;


	/**
	 * A set  <key,value> pairs that describes the event
	 */
	private TypedMap attributes;


	/**
	 * Full constructor
	 * 
	 * @param Model model on which the event happened
	 * @param ModelAgent agent related to the event
	 * @param timeStamp the time at which the event occurred
	 * @param type a String that identifies the type of the event
	 * @param attributes a Map with attributes that describe the event
	 */
	public AbstractEvent(Model model, ModelAgent agent,long timeStamp,String type,Map attributes) {

		this.type = type;
		this.model = model;
		this.agent = agent;
		this.timestamp = timeStamp;
		this.attributes = new TypedMap();
		if(attributes != null){
			this.attributes.putAll(attributes);
		}

	}

	
	public AbstractEvent (Model model, ModelAgent agent,long timeStamp,String type) {
		this(model,agent,timeStamp,type,new HashMap<String,String>());
	}

	
	public long getTimestamp() {
		return timestamp;
	}


	public String getType() {
		return type;
	}


	public Model getModel() {
		return model;
	}


	public ModelAgent getAgent() {
		return agent;
	}


	/**
	 * Allows ordering events with respect of the timeStamp, Model, ModelAgent and type (in that order)
	 * 
	 */
	public int compareTo(Object arg0) {

		Event other = (Event)arg0;

		if(timestamp != other.getTimestamp()) {
			return getTimestamp() > other.getTimestamp()?1:-1;
		}

		if(model.getName() != other.getModel().getName()) {
			return model.getName().compareTo(other.getModel().getName());
		}

		if(agent.getName()!= other.getAgent().getName()) {
			return agent.getName().compareTo(other.getAgent().getName());
		}

		
		if(!type.equals(other.getType())){
			return type.compareTo(other.getType());		    
		}

		return 0;
	}


	
	public TypedMap getAttributes() {
		return attributes;
	}
	
	
	/**
	 * Sets the timestamp of the event
	 *
	 */
	protected void setTimeStamp(long timestamp) {
	  this.timestamp = timestamp;
		
	}
	
	
	/**
	 * Sets the given timestamp to the event
	 * @param attributes
	 */
	protected void setAttributes(Map attributes){
		this.attributes.putAll(attributes);
	}
	
	
	
}

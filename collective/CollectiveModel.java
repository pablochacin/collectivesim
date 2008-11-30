package edu.upc.cnds.collectivesim.collective;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectivesim.collective.imp.Behavior;
import edu.upc.cnds.collectivesim.collective.imp.Event;
import edu.upc.cnds.collectivesim.models.SimulationModel;
import edu.upc.cnds.collectivesim.models.Stream;
import edu.upc.cnds.collectivesim.models.imp.SingleValueStream;


/**
 * Manages the simulation of a Collective by means of Behaviors that occur on the agent
 * periodically and Events that are trigered following a certain probability distribution. 
 * 
 * The CollectiveManager allows also to observe the attributes of the agents in the collective.
 *  
 * The CollectiveManager 
 * @author Pablo Chacin
 *
 */

public class CollectiveModel implements CollectiveConfig {



	/**
	 * List of active agents
	 */
	private List<CollectiveAgent> agents;

	/**
	 * behaviors currently registered in the realm
	 */
	private HashMap<String,Behavior> behaviors;

	/**
	 * Model on which this realm inhabits
	 */
	protected SimulationModel model;

	/**
	 * list of active observers 
	 */
	private Map<String,CollectiveObserver> observers;

	/**
	 * Constructor
	 */
	public CollectiveModel(SimulationModel model){
		this.model = model;
		this.agents = new ArrayList<CollectiveAgent>();
		this.behaviors = new HashMap<String,Behavior>();
		this.observers = new HashMap<String, CollectiveObserver>();
	}


	/**
	 * Adds a behavior to agents in the realm, defined as the execution of a series of
	 * methods. 
	 * The way the behavior is executed along the agents is defined by the 
	 * parameter order. This execution can be by agent (all methods are executed to each agent,
	 * agent by agent) or by method (each method is executed in all agents, method by method)
	 *
	 * @param name a String that identifies the behavior
	 * @param method a Strings with the name of the method
	 * @param streams an array with the Streams to feed the method invocation
	 * @param active a boolean that indicates if the behavior must be inserted active
	 *         or will be deactivated until the realm activates it.
	 * @param frequency a double that specifies the frequency of execution of the behaviors
	 */
	public void addBehavior(String name,String method,Stream[] streams,boolean active, long frequency){
		Behavior behavior = new Behavior(name,this,method, streams,active,frequency);
		behaviors.put(name,behavior);
		
		model.scheduleAction(behavior,new SingleValueStream(name,new Double(frequency)));	
	}

	/**
	 * Adds an event that will be triggered at a certain time 
	 *
	 * @param agent CollectiveAgent that must execute the action
	 * @param time a long with the time at which the event will be triggered
	 * @param action a String with the name of the actions to be executed
	 * @param args an array of Objects to be passed to the action
     *
	 */
	public void addEvent(String name,AgentSampler sampler, Stream distribution, boolean active,String action,Stream...args){
		Event event = new Event(name,this,sampler,active,action,args);
		model.scheduleAction(event, distribution);

	}



	/**
	 * Adds an observer to calculate an attribute over the agents of the collective
	 * 
	 * @param name a String with the name of the observer
	 * @param operator the Operator used to calculate the attribute
	 * @param attribute the name of the attribute
	 * @param frequency the frequency of update
	 */
	public void addObserver(String name, Operator operator, String attribute,long frequency) {

		CollectiveObserver observer = new CollectiveObserver(this,name, operator, attribute);
		
		observers.put(name,observer);

		//schedule observer at a fixed interval using a Stream with a fixed value
		model.scheduleAction(observer,new SingleValueStream(name,new Double(frequency)));
	}


	public List<CollectiveAgent> getAgents() {

		return agents;
	}


	/**
	 * pause the execution of the realm
	 */
	 public void pause() {

		 //pause behaviors
		 for(int i = 0;i<behaviors.size();i++){
			 ((Behavior)behaviors.get(i)).pause();
		 }

	 }


	 /**
	  * starts the execution of the realm
	  */

	 public void start() {
		 //start behaviors
		 for(int i = 0;i<behaviors.size();i++){
			 ((Behavior)behaviors.get(i)).pause();
		 }

	 }




	 public boolean addAgent(CollectiveAgent agent) {
		 // TODO Auto-generated method stub
		 return false;
	 }


	public void registerAction(String action, Protocol protocol) {
		// TODO Auto-generated method stub
		
	}


	public void registerAttribute(String attribute, Protocol protocol) {
		// TODO Auto-generated method stub
		
	}


}

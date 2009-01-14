package edu.upc.cnds.collectivesim.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.cnds.collectives.dataseries.DataSequence;
import edu.upc.cnds.collectivesim.model.imp.BehaviorVisitor;
import edu.upc.cnds.collectivesim.model.imp.Event;
import edu.upc.cnds.collectivesim.model.imp.ModelObserverVisitor;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.scheduler.Stream;
import edu.upc.cnds.collectivesim.scheduler.repast.SingleValueStream;


/**
 * Manages the simulation of a set of related ModelAgents by means of Behaviors that occur on the agent
 * periodically and Events that are triggered following a certain probability distribution. 
 * 
 * The Model allows also to observe the attributes of the agents.
 *  
 * The CollectiveManager 
 * @author Pablo Chacin
 *
 */

public class Model{



	/**
	 * List of active agents
	 */
	private List<ModelAgent> agents;

	/**
	 * behaviors currently registered in the model
	 */
	private HashMap<String,BehaviorVisitor> behaviors;

	/**
	 * Scheduler used to control simulation
	 */
	protected Scheduler scheduler;

	/**
	 * list of active observers 
	 */
	private Map<String,ModelObserverVisitor> observers;

	/**
	 * Constructor
	 */
	public Model(Scheduler scheduler){
		this.scheduler = scheduler;
		this.agents = new ArrayList<ModelAgent>();
		this.behaviors = new HashMap<String,BehaviorVisitor>();
		this.observers = new HashMap<String, ModelObserverVisitor>();
	}


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
	public void addBehavior(String name,String method,boolean active, long frequency,int iterations,long endTime,Stream[] streams){
	
		BehaviorVisitor behavior = new BehaviorVisitor(name,this,method, streams,active,frequency);
		behaviors.put(name,behavior);
		
		scheduler.scheduleRepetitiveAction(behavior,new SingleValueStream(name,new Double(frequency)),iterations,endTime);	
	}
	

	public void addBehavior(String name,String method,boolean active, long frequency,Stream[] streams){
		addBehavior(name, method, active, frequency,0,0, streams);
	}

	public void addBehavior(String name,String method,boolean active, long frequency,int iterations,Stream[] streams){
		addBehavior(name, method, active, frequency,iterations,0, streams);
		
	}


	
	public void addBehavior(String name,String method,boolean active, long frequency,long endTime,Stream[] streams){
		addBehavior(name, method, active, frequency,0,endTime, streams);
	
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
		scheduler.scheduleRepetitiveAction(event, distribution);

	}



	/**
	 * Adds an observer to calculate an attribute over the agents of the collective and generate 
	 * a DataSeries with the resulting values
	 * 
	 * @param name a String with the name of the observer
	 * @param operator the Operator used to calculate the attribute
	 * @param attribute the name of the attribute
	 * @param frequency the frequency of update
	 */
	public void addObserver(String name, ModelObserver observer, String attribute,boolean active,long frequency) {

		ModelObserverVisitor action = new ModelObserverVisitor(this,name,observer,attribute,active,frequency);
		
		observers.put(name,action);

		//schedule observer at a fixed interval using a Stream with a fixed value
		scheduler.scheduleRepetitiveAction(action,new SingleValueStream(name,new Double(frequency)));
		
	}


	public List<ModelAgent> getAgents() {

		return agents;
	}


	/**
	 * pause the execution of the model
	 */
	 public void pause() {

		 //pause behaviors
		 for(int i = 0;i<behaviors.size();i++){
			 ((BehaviorVisitor)behaviors.get(i)).pause();
		 }

	 }


	 /**
	  * starts the execution of the model
	  */

	 public void start() {
		 //start behaviors
		 for(int i = 0;i<behaviors.size();i++){
			 ((BehaviorVisitor)behaviors.get(i)).pause();
		 }

	 }




	 public void addAgent(ModelAgent agent) {
		 	agents.add(agent);
	 }


	 /**
	  * Convenience method to add a batch of agents
	  * @param agents
	  */
	 public void addAgents(List<ModelAgent>agents) {
		for(ModelAgent a: agents) {
			addAgent(a);
		}
	 }

}

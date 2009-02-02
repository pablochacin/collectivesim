package edu.upc.cnds.collectivesim.model.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelObserver;
import edu.upc.cnds.collectivesim.model.imp.BehaviorVisitor;
import edu.upc.cnds.collectivesim.model.imp.DummySampler;
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
 * Models usually are factories for their own agents (but this is not mandatory), which the delegates in the
 * model some actions. This class offers methods to allow subclasses to insert those agents to the model, but 
 * doesn't offer any default method to create new agents. 
 *  
 * TODO: Add AgentSampler as an optional parameter to adBbehavior and addObserver methods
 * TODO: Allows a Stream as frequency (not only fixed values) to addBehavior method
 * 
 * @author Pablo Chacin
 *
 */

public abstract class AbstractModel implements Model{



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
	public AbstractModel(Scheduler scheduler){
		this.scheduler = scheduler;
		this.agents = new ArrayList<ModelAgent>();
		this.behaviors = new HashMap<String,BehaviorVisitor>();
		this.observers = new HashMap<String, ModelObserverVisitor>();
	}


	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#addBehavior(java.lang.String, java.lang.String, boolean, edu.upc.cnds.collectivesim.scheduler.Stream, int, long, edu.upc.cnds.collectivesim.scheduler.Stream[])
	 */
	public void addBehavior(String name,String method,boolean active, Stream<Long> frequency,int iterations,long endTime,Stream<Object>... args){
	
		BehaviorVisitor behavior = new BehaviorVisitor(this,name,new DummySampler(),method, active,args);
		behaviors.put(name,behavior);
		
		scheduler.scheduleRepetitiveAction(behavior,frequency,iterations,endTime);	
	}
	
	
	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#addBehavior(java.lang.String, java.lang.String, boolean, long, int, long, edu.upc.cnds.collectivesim.scheduler.Stream[])
	 */
	public void addBehavior(String name,String method,boolean active, long frequency,int iterations,long endTime,Stream<Object> ... args){
		addBehavior(name, method, active, new SingleValueStream<Long>(name,new Long(frequency)),0,0, args);		
	}

	
	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#addBehavior(java.lang.String, java.lang.String, boolean, long, edu.upc.cnds.collectivesim.scheduler.Stream[])
	 */
	public void addBehavior(String name,String method,boolean active, long frequency,Stream<Object> ...args){
		addBehavior(name, method, active, frequency,0,0, args);
	}

	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#addBehavior(java.lang.String, java.lang.String, boolean, long, int, edu.upc.cnds.collectivesim.scheduler.Stream[])
	 */
	public void addBehavior(String name,String method,boolean active, long frequency,int iterations,Stream<Object> ... args){
		addBehavior(name, method, active, frequency,iterations,0, args);
		
	}


	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#addBehavior(java.lang.String, java.lang.String, boolean, long, long, edu.upc.cnds.collectivesim.scheduler.Stream[])
	 */
	public void addBehavior(String name,String method,boolean active, long frequency,long endTime,Stream<Object> ... args){
		addBehavior(name, method, active, frequency,0,endTime, args);
	
	}

	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#addObserver(java.lang.String, edu.upc.cnds.collectivesim.model.ModelObserver, java.lang.String, boolean, long)
	 */
	public void addObserver(String name, ModelObserver observer, String attribute,boolean active,long frequency) {

		ModelObserverVisitor action = new ModelObserverVisitor(this,name,new DummySampler(),observer,attribute,active);
		
		observers.put(name,action);

		//schedule observer at a fixed interval using a Stream with a fixed value
		scheduler.scheduleRepetitiveAction(action,new SingleValueStream<Long>(name,new Long(frequency)));
		
	}


	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#getAgents()
	 */
	public List<ModelAgent> getAgents() {

		return agents;
	}


	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#pause()
	 */
	 public void pause() {

		 //pause behaviors
		 for(BehaviorVisitor b: behaviors.values()){
			 b.pause();
		 }
	 }


	 /* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#start()
	 */

	 public void start() {
		 //start behaviors
		 for(BehaviorVisitor b: behaviors.values()){
			 b.start();
		 }
	 }



	 /**
	  * inserts an agent to the model. This method can be invoked by the subclasses to add agents 
	  * they created to the model. Subclasses can also offer methods to insert externally created
	  * agents to the model. 
	  * 
	  * @param agent
	  */
	 protected void addAgent(ModelAgent agent) {
		 	agents.add(agent);
	 }

	 protected void addAgent(Object target){
		 addAgent(new ReflexionModelAgent(target));
	 }

	 /**
	  * Convenience method to insert a batch of agents
	  * @param agents
	  */
	 protected void addAgents(List<ModelAgent>agents) {
		for(ModelAgent a: agents) {
			addAgent(a);
		}
	 }

	public long getCurrentTime() {
	 return scheduler.getTime().longValue();		
	}
	
	/**
	 * Schedules the execution of an event on an specific agent. This method is protected because
	 * it receives an agent as a parameter and it is not safe to allow external components to
	 * specify the agent, as it might not be part of the model. Subclasses should provide some
	 * safe model-specific method to trigger events on an particular agent. 
	 * 
	 * @param agent
	 * @param delay
	 * @param method
	 * @param args
	 */
	protected void addEvent(ModelAgent agent,long delay,String method,Object ... args){
		scheduler.scheduleAction(new EventAction(agent,method,args), delay);
	}
}

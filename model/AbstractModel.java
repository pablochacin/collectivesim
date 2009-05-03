package edu.upc.cnds.collectivesim.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.ModelObserver;
import edu.upc.cnds.collectivesim.model.imp.BehaviorVisitor;
import edu.upc.cnds.collectivesim.model.imp.DummySampler;
import edu.upc.cnds.collectivesim.model.imp.ModelEvent;
import edu.upc.cnds.collectivesim.model.imp.ObserverVisitor;
import edu.upc.cnds.collectivesim.model.imp.ReflexionModelAgent;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.stream.FixedValueStream;
import edu.upc.cnds.collectivesim.stream.Stream;



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
	 * Experiment that defines the model's context
	 */
	protected Experiment experiment;
	
	/**
	 * Name of the model
	 */
	protected String name;

	/**
	 * list of active observers 
	 */
	private Map<String,ObserverVisitor> observers;

	/**
	 * Constructor
	 * @param name 
	 */
	public AbstractModel(String name, Experiment experiment){
		this.experiment = experiment;
		this.name = name;
		this.scheduler = experiment.getScheduler();
		this.agents = new ArrayList<ModelAgent>();
		this.behaviors = new HashMap<String,BehaviorVisitor>();
		this.observers = new HashMap<String, ObserverVisitor>();
	}

	@Override
	public Experiment getExperiment(){
		return  experiment;
	}

	@Override
	public String getName(){
		return name;
	}
	
	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#addBehavior(java.lang.String, java.lang.String, boolean, edu.upc.cnds.collectivesim.scheduler.Stream, int, long, edu.upc.cnds.collectivesim.scheduler.Stream[])
	 */
	public  void addBehavior(String name, String method,AgentSampler sampler,
			boolean active, int iterations,Stream<Long> frequency, long delay, long endTime,
			Stream<? extends Object> ... args){
	
		//the sampler is optional, if none specified, use a dummy one, 
		//to assure there is always one
		if(sampler == null){
			sampler = new DummySampler();
		}
						
		BehaviorVisitor behavior = new BehaviorVisitor(this,name,sampler,method, active,iterations,frequency,delay,endTime,args);
		
		behaviors.put(name,behavior);
		

	}
	
		
	public  void addBehavior(String name, String method,AgentSampler sampler,
			boolean active, int iterations,long frequency, long delay, long endTime,
			Stream<? extends Object> ... args){
		
		addBehavior(name, method,sampler, active, iterations,new FixedValueStream<Long>("",frequency), 
				delay, endTime,args);
		
	}
	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#addBehavior(java.lang.String, java.lang.String, boolean, long, edu.upc.cnds.collectivesim.scheduler.Stream[])
	 */
	public void addBehavior(String name,String method,int iterations,long frequency,Stream<? extends Object> ...args){
		addBehavior(name, method,new DummySampler(),true, iterations,new FixedValueStream<Long>(name,frequency),0,0, args);
	}


	public void addBehavior(String name, String method,	long frequency, long delay,long endTime,Stream<? extends Object>... args){
		addBehavior(name, method,new DummySampler(),true, 0,new FixedValueStream<Long>(name,frequency),delay,endTime, args);
		
	}
	
	
	@Override
	public void addBehavior(String name, String method, long frequency, Stream<? extends Object>... args) {
		addBehavior(name, method,new DummySampler(),true, 0,new FixedValueStream<Long>(name,frequency),0,0, args);
		
	}


	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#addObserver(java.lang.String, edu.upc.cnds.collectivesim.model.ModelObserver, java.lang.String, boolean, long)
	 */
	public void addObserver(String name, ModelObserver observer, AgentSampler sampler,String attribute,boolean active,long frequency,long delay) {

		//the sampler is optional, if none specified, use a dummy one, 
		//to assure there is always one
		if(sampler == null){
			sampler = new DummySampler();
		}
		
		ObserverVisitor action = new ObserverVisitor(this,name,sampler,observer,attribute,active,0,new FixedValueStream<Long>("",frequency),delay,0);
		
		observers.put(name,action);

		
	}


	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#getAgents()
	 */
	public List<ModelAgent> getAgents() {

		return new ArrayList<ModelAgent>(agents);
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

	 @Override
	public void reset() {
		//by default, do nothing on reset	
	}

	public void start() throws ModelException {

		 //TODO: this is rather dangerous because if a subclass doesn't call super().start()
		 //       Behaviors will not start. Consider to add an abstract method to be
		 //       extended by subclasses and call it from the abstract model's start method.
		 for(BehaviorVisitor b: behaviors.values()){
				scheduler.scheduleRepetitiveAction((Runnable) b,b.getIterations(),b.getFrequency(),b.getDelay(),b.getEndTime());
		 }

		for(ObserverVisitor o: observers.values()){
			scheduler.scheduleRepetitiveAction((Runnable) o,o.getIterations(),o.getFrequency(),o.getDelay(),o.getEndTime());
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
	 return scheduler.getTime();		
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
		scheduler.scheduleAction(new ModelEvent(agent,method,args), delay);
	}



}

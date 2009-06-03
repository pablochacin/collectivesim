package edu.upc.cnds.collectivesim.model.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.base.BehaviorVisitor;
import edu.upc.cnds.collectivesim.model.base.DummySampler;
import edu.upc.cnds.collectivesim.model.base.ObserverVisitor;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.stream.base.FixedValueStream;



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
 *  Current implementation maintains a separate Map for Observers and Behaviors to allow runtime 
 *  inspection (for example, from a GUI) to discover and manipulate them (for example, pause/resume a
 *  behavior)
 * 
 * @author Pablo Chacin
 *
 */

public abstract class AbstractModel implements Model{

	
	private static Logger log = Logger.getLogger("collectivesim.model");

	/**
	 * List of active agents
	 */
	private List<ModelAgent> agents;

	/**
	 * Map of agents by name
	 */
	private Map<String,ModelAgent>agentMap;
	
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
	 * Actions to be scheduled by this model (observers and behaviors)
	 */
	private List<ModelAction>actions;
	
	/**
	 * Stream of attributes used to initialize agents
	 */
	protected Stream[] attributeStreams;
	
	/**
	 * Constructor
	 * @param name 
	 */
	public AbstractModel(String name, Experiment experiment,Stream...attributeStreams){
		this.experiment = experiment;
		this.name = name;
		this.attributeStreams = attributeStreams;
		this.scheduler = experiment.getScheduler();
		this.agents = new ArrayList<ModelAgent>();
		this.agentMap = new HashMap<String,ModelAgent>();
		this.actions = new ArrayList<ModelAction>();
		this.behaviors = new HashMap<String,BehaviorVisitor>();
		this.observers = new HashMap<String, ObserverVisitor>();
	}

	@Override
	public Experiment getExperiment(){
		return  experiment;
	}

	
	/**
	 * Builds a Map of attributes that can be passed to agent constructors
	 * @return
	 */
	protected Map getAttributes(){
		Map attributes = new HashMap();
		for(Stream s: attributeStreams){
			attributes.put(s.getName(),s.getValue());
		}
		
		return attributes;
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#addBehavior(java.lang.String, java.lang.String, boolean, edu.upc.cnds.collectivesim.scheduler.Stream, int, long, edu.upc.cnds.collectivesim.scheduler.Stream[])
	 */
	public final void addBehavior(String name, String method,AgentSampler sampler,
			boolean active, int iterations,Stream<Long> frequency, long delay, long endTime,
			Stream<? extends Object> ... args){
	
		//the sampler is optional, if none specified, use a dummy one, 
		//to assure there is always one
		if(sampler == null){
			sampler = new DummySampler();
		}
		
		BehaviorVisitor behavior = new BehaviorVisitor(this,name,sampler,method, active,iterations,frequency,delay,endTime,args);
		behaviors.put(name,behavior);
		actions.add(behavior);
		
	}
	
		
	public final void addBehavior(String name, String method,AgentSampler sampler,
			boolean active, int iterations,long frequency, long delay, long endTime,
			Stream<? extends Object> ... args){
		
		addBehavior(name, method,sampler, active, iterations,new FixedValueStream<Long>("",frequency), 
				delay, endTime,args);
		
	}
	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#addBehavior(java.lang.String, java.lang.String, boolean, long, edu.upc.cnds.collectivesim.scheduler.Stream[])
	 */
	public final void addBehavior(String name,String method,int iterations,long frequency,Stream<? extends Object> ...args){
		addBehavior(name, method,new DummySampler(),true, iterations,new FixedValueStream<Long>(name,frequency),0,0, args);
	}


	public final void addBehavior(String name, String method,	long frequency, long delay,long endTime,Stream<? extends Object>... args){
		addBehavior(name, method,new DummySampler(),true, 0,new FixedValueStream<Long>(name,frequency),delay,endTime, args);
		
	}
	
	
	@Override
	public final void addBehavior(String name, String method, long frequency, Stream<? extends Object>... args) {
		addBehavior(name, method,new DummySampler(),true, 0,new FixedValueStream<Long>(name,frequency),0,0, args);
		
	}


	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#addObserver(java.lang.String, edu.upc.cnds.collectivesim.model.ModelObserver, java.lang.String, boolean, long)
	 */
	public final void addObserver(String name, AgentSampler sampler,String[] attributes,DataSeries values,boolean reset,long frequency,long delay) {

		//the sampler is optional, if none specified, use a dummy one, 
		//to assure there is always one
		if(sampler == null){
			sampler = new DummySampler();
		}
		
		ObserverVisitor visitor = new ObserverVisitor(this,name,sampler,attributes,values,reset,0,new FixedValueStream<Long>("",frequency),delay,0);
		
		observers.put(name,visitor);
		actions.add(visitor);
		
	}


	public final void addObserver(String name, AgentSampler sampler,String attribute,DataSeries values,boolean reset,long frequency,long delay) {
	
		String[] attributes = {attribute};
		addObserver(name,sampler, attributes,values,reset,frequency,delay);
	}
	
	public final void addObserver(String name, AgentSampler sampler,String[] attributes,DataSeries values,SeriesFunction function,boolean append,long frequency,long delay) {
		//the sampler is optional, if none specified, use a dummy one, 
		//to assure there is always one
		if(sampler == null){
			sampler = new DummySampler();
		}
		
		ObserverVisitor visitor = new CalculatingObserverVisitor(this,name,sampler,attributes,values,function,append,0,new FixedValueStream<Long>("",frequency),delay,0);
		
		observers.put(name,visitor);
		actions.add(visitor);
	}
	
	public final void addObserver(String name, AgentSampler sampler,String attribute,DataSeries values,SeriesFunction function, boolean append,long frequency,long delay) {
		String[] attributes = {attribute};
		addObserver(name,sampler, attributes,values,function,append,frequency,delay);
	}
	
	/* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#getAgents()
	 */
	public final List<ModelAgent> getAgents() {
		
		//return a new List to avoid concurrency problems
		return new ArrayList<ModelAgent>(agents);
	}

	@Override
	public final void reset(){
		
		//be sure that all agents are eliminated from the model
		agents.clear();
		
		//call subclass to clean up its resources
		terminate();
	}

	@Override
	 public final void pause() {

		 //pause behaviors and observers
		 for(ModelAction a: actions){
			 a.pause();
		 }
		 

	 }

	 @Override
	 public final void resume(){
		 //resume behaviors and observers
		 for(ModelAction a: actions){
			 a.resume();
		 }
	 }

	 
	 /* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.model.imp.ModelInterface#start()
	 */

	 public final void start() throws ModelException {
		 
		 //add agents to model
		 populate();
		 if(agents.size() == 0){
			 log.warning("Model" + name + " has no agents");
		 }
		 
		 //start behaviors
		 //TODO: this is rather dangerous because if a subclass doesn't call super().start()
		 //       Behaviors will not start. Consider to add an abstract method to be
		 //       extended by subclasses and call it from the abstract model's start method.
		 for(BehaviorVisitor b: behaviors.values()){
			 scheduler.scheduleRepetitiveAction((Runnable)b, b.getIterations(), b.getFrequency(), b.getDelay(), b.getEndTime());
		 }
		 
		 for(ObserverVisitor o: observers.values()){
			 scheduler.scheduleRepetitiveAction((Runnable)o, o.getIterations(), o.getFrequency(), o.getDelay(), o.getEndTime());
		 }
	 }

	 /**
	  * inserts an agent to the model. This method can be invoked by the subclasses to add agents 
	  * they created to the model. Subclasses can also offer methods to insert externally created
	  * agents to the model. 
	  * 
	  * @param agent
	  */
	 protected final void addAgent(ModelAgent agent) {
		 	agents.add(agent);
		 	agentMap.put(agent.getName(), agent);
	 }

	 
	 
	 protected final void addAgent(Object target){
		 addAgent(new ReflexionModelAgent(target));
	 }

	 protected final void addAgent(String name,Object target){
		 addAgent(new ReflexionModelAgent(name,target));
	 }
	 
	 public ModelAgent getAgent(String name){
		 return agentMap.get(name);
	 }
	 
	 /**
	  * Convenience method to insert a batch of agents
	  * @param agents
	  */
	 protected final void addAgents(List<ModelAgent>agents) {
		for(ModelAgent a: agents) {
			addAgent(a);
		}
	 }

	public final long getCurrentTime() {
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
	 * 
	 */
	public final void scheduleEvent(ModelAgent agent,long delay,String method,Object ... args){
		scheduler.scheduleAction(new ModelEvent(agent,method,args), delay);
	}

	public final void scheduleEvent(String name,long delay, String method,Object...args){
		ModelAgent agent = getAgent(name);
		scheduleEvent(agent,delay,method,args);
	}

	/**
	 * Create agents for this model and populate it by calling the {@link #addAgent(ModelAgent)} method
	 * of any of its variants.
	 * 
	 */
	protected abstract void populate() throws ModelException;
	
	/**
	 * Terminates the model, cleaning up any resource. Prepares it for 
	 * a new run. This method is executed after the model is executed.
	 * It is not called before the first execution.
	 * @throws ModelException
	 */
	protected void terminate(){
		return;
	}
}

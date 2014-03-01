package collectivesim.model.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import collectivesim.dataseries.DataSeries;
import collectivesim.dataseries.SeriesFunction;
import collectivesim.experiment.Experiment;
import collectivesim.model.AgentFactory;
import collectivesim.model.AgentSampler;
import collectivesim.model.CompositeBehavior;
import collectivesim.model.Model;
import collectivesim.model.ModelAgent;
import collectivesim.model.ModelException;
import collectivesim.scheduler.Scheduler;
import collectivesim.state.Counter;
import collectivesim.stream.Stream;
import collectivesim.stream.base.FixedValueStream;



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
 * @param <ModelAgent>
 *
 */

public class BasicModel implements Model {
	
	
	private static Logger log = Logger.getLogger("collectivesim.model");

	
	public enum Status {STARTED,PAUSED,STOPED};
	
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
	 * Initial number of agents
	 */
	protected int initialAgents;
	
	/**
	 * Agent factory for initial population
	 */
	protected AgentFactory factory;

	/**
	 * Map of {@link AgentStreamAction} used to drive the agent arrival process
	 */
	protected  Map<String, AgentStream> agentStreams;
	
	/**
	 * Execution status
	 */
	protected Status status;
	
	
	/**
	 * Number of agents in the model
	 */
	protected Counter agentCounter;
	/**
	 * List of submodel
	 */
	protected List<Model> subModels;
	
	/**
	 * Indicates if the actions of the model must be traced (true) or not (false)
	 */
	protected boolean debug;
	
	/**
	 * Constructor
	 * @param name 
	 */
	public BasicModel(String name, Experiment experiment,AgentFactory factory,int numAgents){
		this.experiment = experiment;
		this.name = name;
		this.status = Status.STOPED;
		this.factory = factory;
		this.initialAgents = numAgents;
		this.scheduler = experiment.getScheduler();
		this.agents = new ArrayList<ModelAgent>();
		this.agentMap = new HashMap<String,ModelAgent>();
		this.behaviors = new HashMap<String,BehaviorVisitor>();
		this.observers = new HashMap<String, ObserverVisitor>();
		this.agentStreams = new HashMap<String,AgentStream>();
		this.agentCounter = experiment.addCounter(name+".agents");
		this.subModels = new ArrayList<Model>();
		this.debug = false;
	}

	
	/**
	 * Convenience constructor for models that will not make an initial population of agents.
	 * 
	 * @param name
	 * @param experiment
	 */
	public BasicModel(String name, Experiment experiment){
		this(name,experiment,null,0);
	}
	
	@Override
	public Experiment getExperiment(){
		return  experiment;
	}

	
	public void setDebug(boolean debug){
		this.debug = debug;
		for(Model m: subModels){
			m.setDebug(debug);
		}
	}

	public boolean isDebugging(){
		return debug;
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	/* (non-Javadoc)
	 * @see collectivesim.model.imp.ModelInterface#addBehavior(java.lang.String, java.lang.String, boolean, collectivesim.scheduler.Stream, int, long, collectivesim.scheduler.Stream[])
	 */
	
	public final void addBehavior(String name, String method,AgentSampler sampler,
			boolean active, int iterations,Stream<Long> frequency, long delay, long endTime, int priority,
			Stream<? extends Object> ... args){
	
		//the sampler is optional, if none specified, use a dummy one, 
		//to assure there is always one
		if(sampler == null){
			sampler = new DummySampler();
		}
		
		BehaviorVisitor behavior = new BehaviorVisitor(this,name,sampler,method, active,iterations,frequency,delay,endTime,priority,args);
		behaviors.put(name,behavior);
		
	}
	
	
	public void addBehavior(String name, String method, AgentSampler sampler,
			boolean active, int iterations, long frequency, long delay,
			long endTime, int priority, Stream<? extends Object>... args) {
		
			addBehavior(name,method,sampler,active,iterations,new FixedValueStream<Long>(frequency),
					delay,endTime,priority,args);
	}
	public final void addBehavior(String name, String method,AgentSampler sampler,
			boolean active, int iterations,Stream<Long> frequency, long delay, long endTime,
			Stream<? extends Object> ... args){
	
		addBehavior(name,method,sampler,active,iterations,frequency,delay,endTime,0,args);
		
	}
	
		
	public final void addBehavior(String name, String method,AgentSampler sampler,
			boolean active, int iterations,long frequency, long delay, long endTime,
			Stream<? extends Object> ... args){
		
		addBehavior(name, method,sampler, active, iterations,new FixedValueStream<Long>(frequency), 
				delay, endTime,args);
		
	}
	/* (non-Javadoc)
	 * @see collectivesim.model.imp.ModelInterface#addBehavior(java.lang.String, java.lang.String, boolean, long, collectivesim.scheduler.Stream[])
	 */
	public final void addBehavior(String name,String method,int iterations,long frequency,Stream<? extends Object> ...args){
		addBehavior(name, method,new DummySampler(),true, iterations,new FixedValueStream<Long>(frequency),0,0, args);
	}


	public final void addBehavior(String name, String method,	long frequency, long delay,long endTime,Stream<? extends Object>... args){
		addBehavior(name, method,new DummySampler(),true, 0,new FixedValueStream<Long>(frequency),delay,endTime, args);
		
	}
	
	
	@Override
	public final void addBehavior(String name, String method, long frequency, Stream<? extends Object>... args) {
		addBehavior(name, method,new DummySampler(),true, 0,new FixedValueStream<Long>(frequency),0,0, args);
		
	}

	
	public void addCompositeBehavior(String name, CompositeBehavior behavior,
			boolean active, int iterations,long frequency, long delay, long endTime) {
		
		throw new UnsupportedOperationException();
		
	}

	
/**
 * 
 * @param name
 * @param sampler
 * @param attributes
 * @param values
 * @param reset
 * @param frequency
 * @param delay
 * @param priority
 */
	public final void addObserver(String name, AgentSampler sampler,String[] attributes,DataSeries values,boolean reset,long frequency,long delay,int priority) {

		//the sampler is optional, if none specified, use a dummy one, 
		//to assure there is always one
		if(sampler == null){
			sampler = new DummySampler();
		}
		
		ObserverVisitor visitor = new ObserverVisitor(this,name,sampler,attributes,values,reset,0,new FixedValueStream<Long>(frequency),delay,(long)0,priority);
		
		observers.put(name,visitor);
		
	}


	public final void addObserver(String name, AgentSampler sampler,String attribute,DataSeries values,boolean reset,long frequency,long delay) {
	
		String[] attributes = attribute.split(",");
		addObserver(name,sampler, attributes,values,reset,frequency,delay,Integer.MAX_VALUE);
	}
	
	public final void addObserver(String name, AgentSampler sampler,String[] attributes,DataSeries values,SeriesFunction function,boolean reset,long frequency,long delay,int priority) {
		//the sampler is optional, if none specified, use a dummy one, 
		//to assure there is always one
		if(sampler == null){
			sampler = new DummySampler();
		}
		
		ObserverVisitor visitor = new CalculatingObserverVisitor(this,name,sampler,attributes,values,function,reset,0,new FixedValueStream<Long>(frequency),delay,(long)0,priority);
		
		observers.put(name,visitor);
	}
	
	public final void addObserver(String name, AgentSampler sampler,String attribute,DataSeries values,SeriesFunction function, boolean append,long frequency,long delay) {
		String[] attributes = attribute.split(",");
		
		//add obserever ensuring it is executed at the end of the cycle
		addObserver(name,sampler, attributes,values,function,append,frequency,delay,Integer.MAX_VALUE);
	}
	
	/* (non-Javadoc)
	 * @see collectivesim.model.imp.ModelInterface#getAgents()
	 */
	public final List<ModelAgent> getAgents() {
		
		//return a new List to avoid concurrency problems
		List<ModelAgent> agentList= new ArrayList<ModelAgent>(agents);
		
		for(Model m: subModels){
			agentList.addAll(m.getAgents());
		}
		
		return agentList;
	}


	public void addSubModel(Model model){
		subModels.add(model);
	}
	
	@Override
	public void reset(){
		
//		for(Model m: subModels){
//			m.reset();
//		}
		
		//be sure that all agents are eliminated from the model
		agents.clear();
		agentMap.clear();
		status = Status.STOPED;
		
	}

	@Override
	 public final void pause() {
		status = Status.PAUSED;
		 
	 }

	 @Override
	 public final void resume(){
		 status = Status.STARTED;
	 }

	 
	 /* (non-Javadoc)
	 * @see collectivesim.model.imp.ModelInterface#start()
	 */

	 public final void start() throws ModelException {
		 
//		 for(Model m: subModels){
//			 m.start();
//		 }
		 
		 //add agents to model
		 populate();
		 
		 if(agents.size() == 0){
			 log.warning("Model " + name + " has no agents");
		 }
		 

		 for(BehaviorVisitor b: behaviors.values()){
			 scheduler.scheduleAction((Runnable)b, b.getIterations(), b.getFrequency(), b.getDelay(), b.getEndTime(),b.getPriority());
		 }
		 
		 for(ObserverVisitor o: observers.values()){
			 scheduler.scheduleAction((Runnable)o, o.getIterations(), o.getFrequency(), o.getDelay(), o.getEndTime(),o.getPriority());
		 }
		 
		 for(AgentStream a: agentStreams.values()){
			 scheduler.scheduleAction((Runnable)a, a.getIterations(), a.getFrequency(), a.getDelay(), a.getEndTime(),a.getPriority());
		 }
		 
		 status = Status.STARTED;
	 }

	 /**
	  * inserts an agent to the model. This method can be invoked by the subclasses to add agents 
	  * they created to the model. Subclasses can also offer methods to insert externally created
	  * agents to the model. 
	  * 
	  * When the model is in stated state, agents are immediately initialized. 
	  * 
	  * @param agent
	  */
	 protected final void addAgent(ModelAgent agent) throws ModelException{
		 
		 	if(agentMap.containsKey(agent.getName())){
		 		throw new IllegalArgumentException("Duplucated agent name: "+ agent.getName());
		 	}
		 	
		 	agents.add(agent);
		 	agentCounter.increment();
		 	agentMap.put(agent.getName(), agent);
		 	if(status == Status.STARTED)
		 		agent.init(this);
		 			 		 	
	 }

	 
	 /**
	  * Removes the agent from the model given its name. The agent will no longer be available for any behavior or
	  * observer and will no longer be part of the list returned  by {@link #getAgents()} method
	  * 
	  * @param name an String with the name of the Agent
	  */
	 protected final void removeAgent(String name){

		 removeAgent(getAgent(name));
	 }
	 
	 
	 /**
	  * Convenience method to remove an agent from the model. See {@link #removeAgent(String)}.
	  * 
	  * @param agent a ModelAgent to be removed from the model
	  */
	 protected final void removeAgent(ModelAgent agent){
		
		 if(!agents.contains(agent) || !agentMap.containsKey(agent.getName())){
			 throw new IllegalArgumentException("Agent: " + agent.getName() + 
					                            " not found in this model");
		 }
		 
		 agents.remove(agent);
		 agentMap.remove(agent.getName());
		 agentCounter.decrement();
		 
		 
	 }
	 
	 
	 /**
	  * Adds a new Agent to the model that exposes the public methods of the given Object. 
	  * The name of the agent will be automatically generated based on the object's class name.
	  *  
	  * @param target an Object to be exposed as a {@link ModelAgent}
	  */
	 protected final void addAgent(Object target) throws ModelException{
		 addAgent(new ReflexionModelAgent(target));
	 }

	 /**
	  *  Adds a new Agent to the model that exposes the public methods of a given Object.
	  *  
	  * @param name a String with the name of the agent
	  * @param target an Object to be exposed as a {@link ModelAgent}
	  */
	 protected final void addAgent(String name,Object target) throws ModelException{
		 addAgent(new ReflexionModelAgent(name,target));
	 }
	 
	 
	 /**
	  * Returns the agent registered under the given name, if nay.
	  * 
	  * @return a {@link ModelAgent} or null, if no agent is registered under that name
	  * 
	  */
	 public ModelAgent getAgent(String name){
		 ModelAgent agent = agentMap.get(name);

		 Iterator<Model> m = subModels.iterator(); 
		 while((agent == null) && m.hasNext()){
			 agent = m.next().getAgent(name);
		 }
		 
		 return agent;
	 }
	 
	 /**
	  * Convenience method to insert a batch of agents
	  * @param agents
	  */
	 protected final void addAgents(List<ModelAgent>agents) throws ModelException {
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
		scheduler.scheduleAction(new ModelEvent(agent,method,args), 0,(long)0,delay,(long)0,(int)0);
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
	public void populate() throws ModelException{

		for(int i = 0; i < initialAgents;i++){
			createAgent(factory);
		}
		
		//Initialize every Agent
		for(ModelAgent a: getAgents()){
				a.init(this);
		}
	}
		

	/**
	 * Creates an agent from a factory. 
	 */
	@Override
	public ModelAgent createAgent(AgentFactory factory) throws ModelException{
		
	
		
		//ModelAgent agent = factory.createAgent(this);

		ModelAgent agent = factory.createAgent(this);

		addAgent(agent);
		
		return agent;


	}
	
	
	
	/**
	 * 
	 * @param delay
	 * @param endTime
	 * @param frequency
	 * @param rate
	 * @param factory
	 * @param args
	 */
	public void addAgentStream(String name,long delay,long endTime,Stream<Long>frequency,Stream<Integer> rate,AgentFactory factory){
		
		AgentStream action = new AgentStream(this,name,factory,rate,true,frequency,delay,endTime);
		
		agentStreams.put(name,action);
	}
	
	public void addAgentStream(String name,long delay,long endTime,Stream<Long>frequency,Stream<Integer> rate){
		
		addAgentStream(name,delay,endTime,frequency,rate,this.factory);
	}
	
	/**
	 * Informs that an agent has been added to the model. Can be used by subclasses to handle
	 * this event.
	 * 
	 * @param agent
	 */
	protected void agentAdded(ModelAgent agent){
		
	}
	
	/**
	 * Informs that an agent has been removed from the model. Can be used by subclasses to 
	 * handle this event.
	 * 
	 * @param agent
	 */
	protected void agentRemoved(ModelAgent agent){
		
	}




}

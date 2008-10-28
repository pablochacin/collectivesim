package edu.upc.cnds.collectivesim.collective;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.agents.Agent;
import edu.upc.cnds.collectivesim.collective.Behavior;
import edu.upc.cnds.collectivesim.collective.Collective;
import edu.upc.cnds.collectivesim.models.Model;
import edu.upc.cnds.collectivesim.models.Stream;
import edu.upc.cnds.collectivesim.models.imp.Action;
import edu.upc.cnds.collectivesim.models.imp.BasicModel;
import edu.upc.cnds.collectivesim.topology.discrete2D.Discrete2DLocation;
import edu.upc.cnds.collectivesim.util.TypedMap;

import uchicago.src.sim.util.SimUtilities;


/**
 * Manages the simulation of a Collective by means of behaviors that occur on the agent
 * periodically, events that are trigered according to a probability distribution. The manager
 * allows also to observe the attributes of the agents in the collective.
 *  
 * @author Pablo Chacin
 *
 */

public class CollectiveManager  {



	/**
	 * List of active agents
	 */
	private ArrayList agents;

	/**
	 * behaviors currently registered in the realm
	 */
	private HashMap behaviors;

	/**
	 * Events currently active in the realm
	 */
	private Map events;

	/**
	 * Model on which this realm inhabits
	 */
	protected Model model;

	/**
	 * list of active observers 
	 */
	private ArrayList observers;

	/**
	 * Constructor
	 */
	public CollectiveManager(Model model){
		this.model = model;
		this.agents = new ArrayList();
		this.behaviors = new HashMap();
		this.observers = new ArrayList();
		this.events = new HashMap();
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
	public void addBehavior(String name,String method,Stream[] streams,boolean active, double frequency){
		Behavior behavior = new Behavior(name,this.model,(Collective)this,method, streams,active,frequency);
		behaviors.put(name,behavior);
	}

	/**
	 * Adds an event to agents in the collective, defined as the execution of a of method at certain random intervals. 
	 *
	 * @param name a String that identifies this event
	 * @param model the simulation Model on which this event occurs
	 * @param realm the AgentRealm on which resides the agents this event will be applied to
	 * @param method a String with the name of the methods to be execute
	 * @param frequency a long with the frequency, in ticks, of execution
	 * @param scope indicates if event occurs to all agent or to a sub-set of them
	 * @param numEvents a long with the number of events to generate, 0 means  unlimited. 
	 *        After this number is reached, the event is paused
	 */
	public void addEvent(String name,String method,boolean active, String timeDistribution, long scope,long numEvents){
		Event event = new Event(name,this.model,this,method,timeDistribution,true,scope,numEvents);
		events.put(name,event);
	}



	public void addObserver(CollectiveObserver observer, double frequency) {
		observers.add(observer);

		//schedule observer
		model.scheduleAction(new Action(observer,"calculateAttribute",frequency,true));
	}


	public ArrayList getAgents() {

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




	 public boolean addAgent(Agent agent) {
		 // TODO Auto-generated method stub
		 return false;
	 }





}

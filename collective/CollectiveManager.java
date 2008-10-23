package edu.upc.cnds.collectivesim.collective;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.agents.Agent;
import edu.upc.cnds.collectivesim.collective.Behavior;
import edu.upc.cnds.collectivesim.collective.Collective;
import edu.upc.cnds.collectivesim.models.Action;
import edu.upc.cnds.collectivesim.models.BasicModel;
import edu.upc.cnds.collectivesim.models.Model;
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

public class CollectiveManager implements Collective {


    
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
    private HashMap events;

	/**
     * List of locations
     */
    ArrayList locations;
	
	/**
     * Hash with locations by agent id
     */
    HashMap locationsMap = new HashMap();
    
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
    	this.locations = new ArrayList();
        this.events = new HashMap();
    }
    
    /**
     * Registets an agent in this realm.
     * 
     * @return a boolean indicating if the agent could be added or not to the realm
     */
    public boolean addAgent(Agent agent, Node location) {
	
		//register agent
        agents.add(agent);
        
        //index the location by the agent's id
        locationsMap.put(agent.getId(), location);
        
        return true;
		
	}

	/**
	 * Adds a behavior to agents in the realm, defined as the execution of a series of
	 * methods. 
	 * The way the behavior is executed along the agents is defined by the 
	 * parameter order. This execution can be by agent (all methods are executed to each agent,
	 * agent by agent) or by method (each method is executed in all agents, method by method)
	 *
	 * @param name a String that identifies the behavior
	 * @param methods an array of Strings with the name of the agents
	 * @param active a boolean that indicates if the behavior must be inserted active
	 *         or will be deactivated until the realm activates it.
	 * @param frequency a double that specifies the frequency of execution of the behaviors
	 * @param order a long that defines the order of execution of the agents
	 */
	public void addBehavior(String name, String method, boolean active,double frequency, long order){
		String[] methods = new String[1];
		methods[0]=method;
		addBehavior(name,methods,active,frequency,order);
	}
	
	/**
	 * Adds a behavior to agents in the realm, defined as the execution of a series of
	 * methods. 
	 * The way the behavior is executed along the agents is defined by the 
	 * parameter order. This execution can be by agent (all methods are executed to each agent,
	 * agent by agent) or by method (each method is executed in all agents, method by method)
	 *
	 * @param name a String that identifies the behavior
	 * @param methods an array of Strings with the name of the agents
	 * @param active a boolean that indicates if the behavior must be inserted active
	 *         or will be deactivated until the realm activates it.
	 * @param frequency a double that specifies the frequency of execution of the behaviors
	 * @param order a long that defines the order of execution of the agents
	 */
	public void addBehavior(String name,String[] methods,boolean active, double frequency, long order){
		Behavior behavior = new Behavior(name,this.model,(Collective)this,methods, active, frequency,order);
		behaviors.put(name,behavior);
	}
	
    /**
     * Adds an event to agents in the collective, defined as the execution of a of method at certain random intervals. 
     *
     * @param name a String that identifies this event
     * @param model the simulation Model on which this event occurs
     * @param realm the AgentRealm on which resides the agents this event will be applied to
     * @param methods a String array with the name of the methods to be execute
     * @param frequency a long with the frequency, in ticks, of execution
     * @param scope indicates if event occurs to all agent or to a sub-set of them
     * @param numEvents a long with the number of events to generate, 0 means  unlimited. 
     *        After this number is reached, the event is paused
     */
    public void addEvent(String name,String method,boolean active, String timeDistribution, long scope,long numEvents){
        Event event = new Event(name,this.model,this,method,timeDistribution,true,scope,numEvents);
        events.put(name,event);
    }
    
	protected boolean addLocation(Node location){
        locations.add(location);
        return true;
    }

	public void addObserver(CollectiveObserver observer, double frequency) {
        observers.add(observer);
        
        //schedule observer
        model.scheduleAction(new Action(observer,"calculateAttribute",frequency,true));
    }

	/**
     * Returns a location given the id of an agent that is contained on it
     * @param id
     * @return
     */
    public Node findLocation(Object id){
    	
    	return (Node)locationsMap.get(id);
    }

    
    public ArrayList getAgents() {
		
		return agents;
	}
    
    protected ArrayList getLocations() {
        
        return locations;
    }
       
       public ArrayList inquire(String attribute) {
        ArrayList values = new ArrayList();
        Iterator i = getLocations().iterator();
        while(i.hasNext()){
            Node location = (Node)i.next();
            values.add(inquireLocation(location, attribute));
        }
        return values;
    }
       
    /**
     * Inquire a location
     */
   	protected Object inquireLocation(Node location, String attribute) {
		return location.inquire(attribute);
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
    
    
    public void visit(String method){
        String[] methods = {method};
        
        visit(methods);
       }
    
    
    
    public void visit(String[] methods) {
           //randomize the order of the location l
           SimUtilities.shuffle(locations);
           
           //iterate and execute the given methods
           Iterator locations  = getLocations().iterator();
           while(locations.hasNext()){
               Node location = (Node)locations.next();
               
               //execute each of the given methods in order
               for(int k=0;k < methods.length;k++ ){
                 visitLocation(location,methods[k],(Object[])null);
               }
           }

           
     }

    
    
	public boolean addAgent(Agent agent) {
		// TODO Auto-generated method stub
		return false;
	}
    
       

 }

package edu.upc.cnds.collectivesim.collective.imp;

import java.util.List;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.util.FormatException;
import edu.upc.cnds.collectivesim.agents.AgentException;
import edu.upc.cnds.collectivesim.collective.AgentSampler;
import edu.upc.cnds.collectivesim.collective.CollectiveManager;
import edu.upc.cnds.collectivesim.models.Model;
import edu.upc.cnds.collectivesim.models.Stream;


/**
 * 
 * Executes an specific action on a sample of agents with a distribution
 * that follows certain distribution.
 * 
 * @author Pablo Chacin
 *
 */
public class Event implements Runnable {


	private static Logger log = Logger.getLogger("collectivesim.collective.event");

	/**
	 * Name
	 */
	String name;
	
	/**
	 * Collective
	 */
	CollectiveManager collective;
	
	/**
	 * Sampler used to select the agents that will 
	 */
	AgentSampler sampler;
    
    /**
     * Name of the method to be executed
     */
    private String method;

    /**
     * Arguments to be passed to the method
     */
    private Stream[] args;

    /**
     * Indicates if the event is active or not
     */
    
    private boolean active;
    
    /**
     * Default constructor
	 *
     * @param method a String with the name of the method to be execute
     * @param args array of Streams to be passed as arguments
     */
    public Event(String name,CollectiveManager collective,AgentSampler sampler, boolean active, String method, Stream ... args){
    	this.name = name;
    	this.sampler = sampler;
    	this.method = method;
    	this.args = args;
    	
        //if behavior must be created active
        if(active){
            this.start();
        }    
    	
    }

        
    public void start() {
    	this.active=true;
    }
    
    public void pause() {
    	this.active = false;
    }
    
    /**
     * executes the method according with the scope
     *
     */
    public void run(){
    	
    	if(!active) {
    		return;
    	}
    	
    	try {
    		List<CollectiveAgent> agents = sampler.sample(collective.getAgents());
    		
    		for(CollectiveAgent a: agents) {
			   a.handleVisit(method,args);
    		}
		} catch (AgentException e) {
			log.severe("Exception invoking method" +method+": "+ FormatException.getStackTrace(e));
			active = false;
		}
    }
    


}

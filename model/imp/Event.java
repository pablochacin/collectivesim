package edu.upc.cnds.collectivesim.model.imp;

import java.util.List;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.util.FormatException;
import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.scheduler.Stream;


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
	Model collective;
	
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
    public Event(String name,Model collective,AgentSampler sampler, boolean active, String method, Stream ... args){
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
    		List<ModelAgent> agents = sampler.sample(collective.getAgents());
    		
    		for(ModelAgent a: agents) {
			   a.execute(method,args);
    		}
		} catch (ModelException e) {
			log.severe("Exception invoking method" +method+": "+ FormatException.getStackTrace(e));
			active = false;
		}
    }
    


}

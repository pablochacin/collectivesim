package edu.upc.cnds.collectivesim.collective;

import java.util.List;

import edu.upc.cnds.collectivesim.collective.imp.CollectiveAgent;
import edu.upc.cnds.collectivesim.models.Model;
import edu.upc.cnds.collectivesim.models.Stream;
import edu.upc.cnds.collectivesim.models.imp.Action;


/**
 * 
 * Represents the Collective on each Node and serves as an intermediary. 
 * From the perspective of the Node, is an Agent. From the perspective of the
 * Agent, represent the Collective
 * 
 * @author pchacin
 *
 */
public class Behavior {


    /**
     * Model on which the behavior inhabits
     */
    Model model;

    /**
     * Realm on which the agents this model applies to, reside
     */
    CollectiveManager collective;
    /**
     * Frequency of execution of the behavior
     */
    private double frequency;

    /**
     * Name of the methods to be executed
     */
    private String method;

    
    /**
     * Strems to feed the arguments for the method
     */
    private Stream[] streams;
    
    /**
     * name of the mehavior
     */
    private String name;

 
    
    /**
     * Indicates if the behavior is active
     */
    private boolean active=false;
    
    /**
     * Default constructor
     * @param name a String that identifies this behavior
     * @param model the simulation Model on which this behavior inhabits
     * @param collective the AgentRealm on which resides the agents this behavior will be applied to
     * @param method a String the name of the method to be execute
     * @param streams an array of Streams to feed the arguments of the method
     * @param active a boolean that indicates if the behavior must be inserted active
     *        or will be deactivated until the realm activates it.
     * @param frequency a long with the frequency, in ticks, of execution

     */
    protected Behavior(String name, Model model,CollectiveManager collective,String method, Stream[] streams, boolean active,
            double frequency){
        this.name = name;
        this.method = method;
        this.frequency = frequency;
        this.model = model;
        this.collective = collective;

        //if behavior must be created active
        if(active){
            this.start();
        }
    }

    /**
     * starts the execution of the behavior 
     */	
    public void start(){
        //schedule the execution of the behavior with a repetitive action at 
        //the given frequency 
        if(!active){
          active = true;
          schedule();
        }
    }

    /**
     * pause the execution of the behavior
     */
    public void pause(){
        if(active){
          active = false;
          //TODO: pause the behavior
        }
    }

    /**
     * Schedule the execution of the behavior
     *
     */
    private void schedule(){
       Action execute = new Action(this,"run",frequency,true);
       model.scheduleAction(execute);
    }
    
    
    /**
     * This method is periodically executed
     *
     */
    public void run() {
    	List<CollectiveAgent> nodes = CollectiveManager.getNodes();
   
    	for(CollectiveAgent n: nodes) {
    	//construct an argument list for the method invocation
    	Object[] arguments = new Object[streams.length];
    	for(int i=0;i<arguments.length;i++) {
    		arguments[i] = streams[i].getValue();
    	}
    	
    	n.handleVisit(method,arguments);
    	}
    	
    }
    
  


}

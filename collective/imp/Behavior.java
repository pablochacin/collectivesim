package edu.upc.cnds.collectivesim.collective.imp;

import java.util.List;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.util.FormatException;
import edu.upc.cnds.collectivesim.collective.CollectiveAgent;
import edu.upc.cnds.collectivesim.collective.CollectiveException;
import edu.upc.cnds.collectivesim.collective.CollectiveModel;
import edu.upc.cnds.collectivesim.models.Stream;


/**
 * 
 * Represents the Collective on each Node and serves as an intermediary. 
 * From the perspective of the Node, is an Agent. From the perspective of the
 * Agent, represent the Collective
 * 
 * @author pchacin
 *
 */
public class Behavior implements Runnable{


	private static Logger log = Logger.getLogger("collectivesim.collective.behavior");
    /**
     * Realm on which the agents this model applies to, reside
     */
    CollectiveModel collective;
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
     * @param collective the AgentRealm on which resides the agents this behavior will be applied to
     * @param method a String the name of the method to be execute
     * @param streams an array of Streams to feed the arguments of the method
     * @param active a boolean that indicates if the behavior must be inserted active
     *        or will be deactivated until the realm activates it.
     * @param frequency a long with the frequency, in ticks, of execution

     */
    public Behavior(String name, CollectiveModel collective,String method, Stream[] streams, boolean active,
            double frequency){
        this.name = name;
        this.method = method;
        this.frequency = frequency;
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
          active = true;
    }

    /**
     * pause the execution of the behavior
     */
    public void pause(){
        active = false;
    }

    
    
    /**
     * This method is periodically executed
     *
     */
    public void run() {
   
    	if(!active) {
    		return;
    	}
    	
    	List<CollectiveAgent> agents = collective.getAgents();
   
    	for(CollectiveAgent a: agents) {
    	//construct an argument list for the method invocation
    	Object[] arguments = new Object[streams.length];
    	for(int i=0;i<arguments.length;i++) {
    		arguments[i] = streams[i].getValue();
    	}
    	
    	try {
			a.executeAction(method,arguments);
		} catch (CollectiveException e) {
			log.severe("Exception invoking method" +method+": "+ FormatException.getStackTrace(e));
			active = false;
		}
    	}
    	
    }
    
  


}

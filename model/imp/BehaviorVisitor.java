package edu.upc.cnds.collectivesim.model.imp;

import java.util.logging.Logger;

import edu.upc.cnds.collectives.util.FormatException;
import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.scheduler.Stream;


/**
 * 
 * Represents the Collective on each Node and serves as an intermediary. 
 * From the perspective of the Node, is an Agent. From the perspective of the
 * Agent, represent the Collective
 * 
 * @author pchacin
 *
 */
public class BehaviorVisitor extends AgentVisitor{


	private static Logger log = Logger.getLogger("collectivesim.model");
    /**


    /**
     * Name of the methods to be executed
     */
    private String method;

    
    /**
     * Strems to feed the arguments for the method
     */
    private Stream[] streams;
    


    
    /**
     * Default constructor
     * @param method a String the name of the method to be execute
     * @param streams an array of Streams to feed the arguments of the method
     */
    public BehaviorVisitor(Model model,String name,AgentSampler sampler,String method, Stream[] streams, boolean active){
    	super(model,name,sampler,active);
    	this.method = method;
        if (streams == null){
        	this.streams = new Stream[0];
        }
        else{
            this.streams = streams;
        	
        }

    }
    
    
    /**
     * Visit each agent and execute a method
     */
    public boolean visit(ModelAgent agent) {
   
    	Object[] arguments = new Object[streams.length];
    	for(int i=0;i<arguments.length;i++) {
    		arguments[i] = streams[i].getValue();
    	}
    	
    	try {
			agent.execute(method,arguments);
		} catch (ModelException e) {
			log.severe("Exception invoking method" +method+": "+ FormatException.getStackTrace(e));
			return false;
		}
    	
		return true;
    }
    
  


}

package edu.upc.cnds.collectivesim.model.base;

import java.util.logging.Level;

import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.stream.Stream;


/**
 * 
 * Represents the Collective on each Node and serves as an intermediary. 
 * From the perspective of the Node, is an Agent. From the perspective of the
 * Agent, represent the Collective
 * 
 * @author Pablo Chacin
 *
 */
public class BehaviorVisitor extends AgentVisitor{

	
    private static final String MODEL_ACTION_TYPE = "Behavior";


	/**
     * Name of the methods to be executed
     */
    private String method;

    
    /**
     * Stream to feed the arguments for the method
     */
    private Stream<? extends Object>[] streams;
    
    
    /**
     * Default constructor
     * @param method a String the name of the method to be execute
     * @param streams an array of Streams to feed the arguments of the method
     */
    public BehaviorVisitor(Model model,String name,AgentSampler sampler,String method, boolean active,int iterations,Stream<Long> frequency, long delay, long endTime,int priority,Stream<? extends Object> ... streams){
    	super(model,name,sampler,active,iterations,frequency,delay,endTime,priority);

        
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
    		arguments[i] = streams[i].nextElement();
    	}
    	
    	try {
			agent.execute(method,arguments);
		} catch (ModelException e) {
			if(log.isLoggable(Level.SEVERE))
				log.severe("Exception invoking method" +method+": "+ FormattingUtils.getStackTrace(e));
				this.pause();
				return false;
		}
    	
		return true;
    }
    



	protected String getType(){
		return MODEL_ACTION_TYPE;
	}

}

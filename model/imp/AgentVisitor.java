package edu.upc.cnds.collectivesim.model.imp;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.stream.Stream;


/**
 * 
 * Allows visiting the agents of a model.
 * 
 * Allows specifying a Filter to agents
 * 
 * Subclasses must implement the visit method, to visit each agent. Optionally, they can implement
 * the startVisit and endVisit methods to make initialization and clean out.
 * 
 * @author Pablo Chacin
 *
 */
public abstract class AgentVisitor extends ModelAction{
		
	
	/**
     * Model on which the agents this model applies to, reside
     */
    protected Model model;
    
	/**
	 * Sampler used to select the agents that will 
	 */
	private AgentSampler sampler;
    
    /**
     * name of the visitor
     */
    protected String name;

 
    

    
    
    
    /**
     * Default constructor
     * @param name a String that identifies this behavior
     * @param model the Model on which resides the agents this behavior will be applied to
     * @param active a boolean that indicates if the behavior must be inserted active
     *        or will be deactivated until the realm activates it.
     */
    public AgentVisitor(Model model,String name, AgentSampler sampler, 
    		            boolean active,int iterations,Stream<Long>frequency,long delay,long endTime){
    	
    	super(active,iterations,frequency,delay,endTime);
        this.name = name;
        this.model = model;
        this.sampler = sampler;


    }




    
    
    /**
     * This method is periodically executed. Visits all agenst returned by the sampler, while the
     * visit method (implemented by a subclass) return true. If an exception is encountered, the 
     * visit is finalized.
     *
     * TODO: apply filter to agents
     */
    public void execute() {
   

    	
		List<ModelAgent> agents = sampler.sample(model.getAgents());   
    			
    	startVisit();
    	
    	for(ModelAgent a: agents) {
    		boolean continueVisiting;
			try {
				continueVisiting = visit(a);
	
				if(!continueVisiting){
					break;
				}
			} catch (ModelException e) {
				if(log.isLoggable(Level.SEVERE))
					log.severe("Exception visiting agent "+ FormattingUtils.getStackTrace(e));
				break;
			}
    	}
    	
    	endVisit();
    }
    
    /**
     * Starts visiting agents. Allows pre-visit setup. 
     * Default implementation does nothing.
     */
    protected void startVisit(){
    	
    }
 
    /**
     * Visit an agent. 
     * 
     * @param agent
     * 
     * @return true if the visit must continue, false otherwise
     * @throws ModelException 
     */
    protected abstract boolean visit(ModelAgent agent) throws ModelException;


    /**
     * End visit of agents. Allows post-visit cleanup.
     * Default implementation does nothing.
     */
    protected void endVisit(){
    	
    }
    
    protected String getName(){
    	return name;
    }


 
    
}

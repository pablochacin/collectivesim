package edu.upc.cnds.collectivesim.model.imp;

import java.util.List;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;


/**
 * 
 * Allows visiting the agents of a model.
 * 
 * Allows specifying a Filter to agents
 * 
 * Subclasses must implement the visit model, to visit each agent
 * 
 * @author Pablo Chacin
 *
 */
public abstract class AgentVisitor implements Runnable{


	protected static Logger log = Logger.getLogger("collectivesim.model");
	
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
     * Indicates if the behavior is active
     */
    private boolean active=false;
    
    
    
    /**
     * Default constructor
     * @param name a String that identifies this behavior
     * @param model the Model on which resides the agents this behavior will be applied to
     * @param active a boolean that indicates if the behavior must be inserted active
     *        or will be deactivated until the realm activates it.
     */
    public AgentVisitor(Model model,String name, AgentSampler sampler, boolean active){
        this.name = name;
        this.model = model;
        this.sampler = sampler;
        
        //if visitor is active must be created active
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
     * This method is periodically executed. Visits all agenst returned by the sampler, while the
     * visit method (implemented by a subclass) return true. If an exception is encountered, the 
     * visit is finalized.
     *
     * TODO: apply filter to agents
     */
    public void run() {
   
    	if(!active) {
    		return;
    	}
    	
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

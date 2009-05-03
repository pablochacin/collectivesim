package edu.upc.cnds.collectivesim.model.imp;

import java.util.Vector;
import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.ModelObserver;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * Observes the Collective and calculates an attribute from applying an operator
 * to the attribute on all agents (e.g. MAX, MIN)
 *  
 * @author Pablo Chacin
 *
 */
public class ObserverVisitor extends AgentVisitor{

	private static Logger logger = Logger.getLogger("collectivesim.model");
	
      
    /**
     * Agent attribute being observed
     */
    String attribute;
    
    /**
     * Observer to be informed
     */
    ModelObserver observer;
    
    
    private Vector values;
    /**
     * Default constructor
     */
    public ObserverVisitor(Model model,String name,AgentSampler sampler,ModelObserver observer,String attribute,boolean active,int iterations,Stream<Long> frequency, long delay, long endTime){
        super(model,name,sampler,active,iterations,frequency,delay,endTime);
        this.observer = observer;
        this.attribute = attribute;
        this.values = new Vector();
        
    }

     
    protected void startVisit(){
    	values.clear();
    }
    
    /**
     * 
     * Updates the observed attribute. This method is invoked by the
     * Collective
     * 
     */
    protected boolean visit(ModelAgent agent) throws ModelException{
                    	
       	 try {
				values.add(agent.getAttribute(attribute));
				return true;
			} catch (ModelException e) {
				values.clear();
				throw e;
			}
        
			
     }
         
    protected void endVisit(){
    	observer.updateValues(model, name, values);
    }
}

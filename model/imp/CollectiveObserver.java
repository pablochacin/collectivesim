package edu.upc.cnds.collectivesim.model.imp;

import java.util.List;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.dataseries.DataItem;
import edu.upc.cnds.collectives.dataseries.DataSequence;
import edu.upc.cnds.collectives.dataseries.DataSeries;
import edu.upc.cnds.collectives.dataseries.InvalidDataItemException;
import edu.upc.cnds.collectives.dataseries.baseImp.BaseDataItem;
import edu.upc.cnds.collectivesim.model.CollectiveAgent;
import edu.upc.cnds.collectivesim.model.CollectiveException;
import edu.upc.cnds.collectivesim.model.CollectiveModel;
import edu.upc.cnds.collectivesim.model.Operator;

/**
 * Observes the Collective and calculates an attribute from applying an operator
 * to the attribute on all agents (e.g. MAX, MIN)
 *  
 * @author Pablo Chacin
 *
 */
public class CollectiveObserver implements Runnable, DataSequence {

	private static Logger logger = Logger.getLogger("collectivesim.model");
	
   /**
    * collective that this observer observes
    */
    private CollectiveModel collective;
    
    /**
     * Name of the observer
     */
    private String name;
   
    /**
     * Operator to be applied on the attibute
     */
    Operator operator;
    
    /**
     * Agent attribute being observed
     */
    String attribute;
    
    /**
     * Currrent value
     */
    Object value;
    
    /**
     * Default constructor
     */
    public CollectiveObserver(CollectiveModel collective,String name,Operator operator,String attribute){
        this.collective = collective;
    	this.name = name;
        this.operator = operator;
        this.attribute = attribute;
        operator.reset();
        
    }

   /**
    * @return the name of the observer
    */
    public String getName() {
    	return name;
    }
    
    /**
     *Periodic update of the Observer
     */
    public void run() {
    	calculateAttribute(collective.getAgents());
    }
  
    
    /**
     * 
     * Updates the observed attribute. This method is invoked by the
     * Collective
     * 
     */
    private void calculateAttribute(List<CollectiveAgent> agents) {
                
         //iterate over all agents and calculate an operator
         operator.reset();
         
         for(CollectiveAgent a: agents) {
        	 try {
				operator.calculate(a. inquireAttribute(attribute));
			} catch (CollectiveException e) {
				operator.reset();
				break;
			}
         }
        
         value = operator.getResult();
         
     }
     
    
    public DataItem getItem() {
    	return new BaseDataItem(value);
    }
}

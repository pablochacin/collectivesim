package edu.upc.cnds.collectivesim.collective;

import java.util.List;

import uchicago.src.sim.analysis.DataSource;
import uchicago.src.sim.analysis.Sequence;

/**
 * Observes the Collective and calculates an attribute from applying an operator
 * to the attribute on all agents (e.g. MAX, MIN)
 *  
 * @author Pablo Chacin
 *
 */
public class CollectiveObserver implements Runnable,Observer {

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
        
         //return result of the operator
         value = operator.getResult();
         
     }

   /**
    * Returns a value of a DataSource.
    * TODO: check whether this interface is usefull at all.
    * 
    * @see uchicago.src.sim.analysis.DataSource
    */
     public Object execute() {
         return value;
     }
     
     
     /**
      * Gets the next value in a sequence.
      * Allows the RealmObserver to be plotted in a sequence.
      *
      * @see uchicago.src.sim.analysis.Sequence
      * 
      * @return the value to be plotted.
        */
       public double getSValue() {
          return (Double)value;
       }
}

package edu.upc.cnds.collectivesim.collective;

import java.util.List;

import edu.upc.cnds.collectivesim.agents.Agent;

import uchicago.src.sim.analysis.DataSource;
import uchicago.src.sim.analysis.Sequence;

/**
 * Observes the realm and calculates an attribute from applying an operator
 * to the attribute on all agents (e.g. MAX, MIN)
 *  
 * @author Pablo Chacin
 *
 */
public class CollectiveObserver implements DataSource, Sequence {

   /**
    * collective that this observer observes
    */
    Collective collective;
    
   
    /**
     * Operator to be applied on the attibute
     */
    Operator operator;
    
    /**
     * Agent attribute being observed
     */
    double attribute;
    
    /**
     * Default constructor
     */
    public CollectiveObserver(Operator operator){
        this.operator = operator;
        operator.reset();
    }

   
  
    
    /**
     * 
     * Updates the observed attribute. This method is invoked by the
     * Collective
     * 
     */
    private void calculateAttribute(List<Agent> agents) {
         
         
         //iterate over all agents and calculate an operator
         operator.reset();
         
         for(int i=0;i<agents.size();i++){
           Agent agent = agents.get(i);
           operator.calculate(agent);
        }
        
         //return result of the operator
         attribute = operator.getResult();
         
     }

   /**
    * Returns a value of a DataSource.
    * TODO: check whether this interface is usefull at all.
    * 
    * @see uchicago.src.sim.analysis.DataSource
    */
     public Object execute() {
         return new Double(attribute);
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
          return attribute;
       }
}

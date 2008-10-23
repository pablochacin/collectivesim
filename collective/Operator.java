package edu.upc.cnds.collectivesim.collective;

import edu.upc.cnds.collectivesim.agents.Agent;

/**
 * 
 * @author Pablo Chacin
 *
 */
public interface Operator {

    /**
     * Resets the calculation of the opertor
     */
    public void reset();
    
    /**
     * 
     * @param agent Agent on which the operator must be applied
     */
    public void calculate(Agent agent);
    
    /**
     * Returs the result of the calculation so far
     */
    public Double getResult();
    
}

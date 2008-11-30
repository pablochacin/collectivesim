package edu.upc.cnds.collectivesim.collective;


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
     * @param value value on which the operator must be applied
     */
    public void calculate(Object value);
    
    /**
     * Returs the result of the calculation so far
     */
    public Object getResult();
    
}

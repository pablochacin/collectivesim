package edu.upc.cnds.collectivesim.experiment;

/**
 * Represents a numeric value with a name that forms part of the 
 * Experiment's state.
 * 
 * @author PabloChacin
 *
 */
public interface StateValue {

	/**
	 * @return the name of the value
	 */
	public String getName();
	
	/**
	 * 
	 * @return the current value 
	 */
	public Double getValue();
}

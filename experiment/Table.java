package edu.upc.cnds.collectivesim.experiment;

import java.util.List;

/**
 * A series of values that can be accessed sequential or randomly. It behaves like an
 * immutable list of elements.
 * 
 * It is used primary to hold valid values for experiment's parameters.
 * 
 * Can be hold in memory or backed by a file or database
 * 
 */
public interface Table<T> {

	/**
	 * Loads the values in the table
	 * 
	 * @throws ExperimentException if there is any error loading values, like IOException, 
	 *         lack of memory, etc.
	 */
	public void load() throws ExperimentException;
	
	public String getName();
	
	/**
	 * Returns the 
	 * @param i
	 * @return
	 */
	public T getElement(int i);
	
	/**
	 * @return the list of elements
	 */
	public List<T> getElements();
	
	/**
	 * 
	 * @return the number of values
	 */
	public int getNumValues();
}

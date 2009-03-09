package edu.upc.cnds.collectivesim.dataseries;


/**
 * Defines an operator that can be applied to a DataSeries.
 * The Function follows a visitor design pattern
 * 
 * @author Pablo Chacin
 *
 */
public interface SeriesFunction {
	
	/**
	 * Resets the function. Allows the initialization
	 * of internal variables.
	 *
	 */
	public void reset();
	
	/**
	 * Visits a data item in a DataSeries
	 * 
	 * @param item the value of the item
	 * @return a boolean indicating if the visiting of the data series should continue (true)
	 *         or not (false). It is used to avoid unnecessary visits.
	 */
	public boolean calculate(DataItem item);
	
	
	/**

	 * @return the current value of the function
	 * 
	 */
	public Double getResult();
	
}

package collectivesim.dataseries;


/**
 * Defines an operator that can be applied to a DataSeries.
 * 
 * @author Pablo Chacin
 *
 */
public interface SeriesFunction {
	

	/**
	 * Resets the function's internal values, if any.
	 */
	public void reset();
	
	/**
	 * Applies the function to the item
	 *  
	 * @param item a DataItem
	 * @return true if the function will continue over remaining values.
	 * 
	 */
	public boolean processItem(DataItem item);
	
	/**
	 * Calculates the function's result and produces one or more
	 * DataItems in the result DataSeries
	 * 
	 * @param result
	 */
	public void calculate(DataSeries result);
}

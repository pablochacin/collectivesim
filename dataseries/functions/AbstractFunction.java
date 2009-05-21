package edu.upc.cnds.collectivesim.dataseries.functions;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.DataSequence;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;

/**
 * Calculates the series's basic statistical parameters: 
 * <ul>
 * <li>count: number of elements
 * <li>avg the average of the elements
 * <li>stdev: deviation: standard deviation
 * <li>stderr: standard error = stdev/sqrt(n)
 * <li>errorlow: standard error lower limit = avg-1.96*stderr
 * <li>errorhigh: standard error upper  limit = avg+1.96*stderr
 * <li>min: minimum value
 * <li>max: maximum value
 * </ul>
 * 
 * The standard deviation is calculated by using the formula
 * 
 * stdev = SQRT([SUM((xi-avg)^2]/N) ,   where avg = SUM(xi)/N
 * 
 * Notice that dev depends on the avg. 
 * 
 * This can be simplified by expanding the sum of the errors (xi-avg) as follows
 * 
 * SUM((xi-avg)^2 = SUM(xi^2 -2*xi*avg + avg^2)
 *                = SUM(xi^2) -2*avg*SUM(xi) + N*avg^2 
 *                = SUM(xi^2) -2*avg*N*SUM(xi)/N + N*avg^2
 *                = SUM(xi^2) -2*N*avg^2 + N*avg^2
 *                = SUM(xi^2) -N*avg^2
 *                
 *  Substituting in the original formula we have    
 *  dev = SQRT([SUM(xi^2) -N*avg^2]/N)
 *      = SQRT(SUM(xi^2)/N - avg^2)
 *      
 *             
 * @author Pablo Chacin
 *
 */
public abstract class AbstractFunction implements SeriesFunction {

	
	/**
	 * Name of the attributed used to calculate statistics
	 */
	protected String attribute;
	
	public AbstractFunction(String attribute){
		this.attribute = attribute;
	}
	
	public void apply(DataSeries series,DataSeries result) {
		
		reset();
		
		DataSequence sequence = series.getSequence();
		
		while(sequence.hasItems()){
			if(!processItem(sequence.getItem())){
				break;
			}
		}
		
		getResult(result);
	}

	/**
	 * Resets the function, reading it for a calculation
	 */
	protected abstract void reset();

	/**
	 * Process an item form the DataSeries.
	 * 
	 * @param item
	 * 
	 * @return true if the process must continue, false otherwise.
	 */
	protected abstract boolean processItem(DataItem item);
	
	
	/**
	 * 
	 * @param result
	 */
	protected abstract void getResult(DataSeries result);


}

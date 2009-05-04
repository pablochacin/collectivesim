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
public class StatisticalSummary implements SeriesFunction {

	/**
	 * Sum of values
	 */
	private Double sumX;
	
	/**
	 * Sum of square of values, used to calculate the stdev
	 */
	private Double sumX2;
	
	/**
	 * Number of items processed
	 */
	private Double count;
	
	/**
	 * Minimum value encountered
	 */
	private Double min;
	
	/**
	 * Maximum value encountered
	 */
	private Double max;
	
	/**
	 * Name of the attributed used to calculate statistics
	 */
	private String attribute;
	
	public StatisticalSummary(String attribute){
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

	protected boolean processItem(DataItem item){
		Double value = item.getDouble(attribute);
		count++;		
		sumX += value;
		sumX2 += value*value;
		min = Math.min(min, value);
		max = Math.max(max, value);
		
		return true;	
	}
	
	protected void getResult(DataSeries result) {

		Double avg = sumX/count;
		Double stdev = Math.sqrt((sumX2/count)-(avg*avg));
		Double stderr = stdev/Math.sqrt(count);
		
		Map<String,Object>attributes = new HashMap<String,Object>();
		attributes.put("count", new Double(count));
		attributes.put("average", avg);
		attributes.put("stdev", stdev);
		attributes.put("min", min);
		attributes.put("max", max);
		attributes.put("stderr", stderr);
		attributes.put("errorhigh", avg+1.96*stderr);
		attributes.put("errorlow", avg-1.96*stderr);		
		
		
		result.addItem(attributes);
	}

	/**
	 * Resets the function
	 */
	protected void reset() {
		sumX = 0.0;
		sumX2 = 0.0;
		min = Double.MAX_VALUE;
		max = Double.MIN_VALUE;
		count = 0.0;
	}

}

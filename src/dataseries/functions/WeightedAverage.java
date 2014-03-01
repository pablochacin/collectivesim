package collectivesim.dataseries.functions;

import java.util.HashMap;
import java.util.Map;

import collectivesim.dataseries.DataItem;
import collectivesim.dataseries.DataSequence;
import collectivesim.dataseries.DataSeries;
import collectivesim.dataseries.SeriesFunction;

/**
 * Calculates the weighted average of a series 
 * <ul>
 * <li>count: number of elements
 * <li>avg the weighted of the elements
 * <li>min: minimum value
 * <li>max: maximum value
 * </ul>
 * 
 *             
 * @author Pablo Chacin
 *
 */
public class WeightedAverage implements SeriesFunction {

	/**
	 * Sum of values
	 */
	private Double sumX;
		
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
	
	protected String attribute;

	
	protected String weightAttribute;
	
	public WeightedAverage(String attribute,String weighteAttribute){
		this.attribute =attribute;
		this.weightAttribute =weighteAttribute;
	}
	

	public boolean processItem(DataItem item){
		Double value = item.getDouble(attribute);
		Double weight = item.getDouble(weightAttribute);
		count += weight;		
		sumX += value*weight;
		min = Math.min(min, value);
		max = Math.max(max, value);
		
		return true;	
	}
	
	public void calculate(DataSeries result) {

		if(count == 0){
			return;
		}
		
		Double avg = sumX/count;
		
		Map<String,Object>attributes = new HashMap<String,Object>();
		attributes.put("count", new Double(count));
		attributes.put("avg", avg);
		attributes.put("min", min);
		attributes.put("max", max);		
		
		result.addItem(attributes);
	}

	/**
	 * Resets the function
	 */
	public void reset() {
		sumX = 0.0;
		min = Double.MAX_VALUE;
		max = Double.MIN_VALUE;
		count = 0.0;
	}

}

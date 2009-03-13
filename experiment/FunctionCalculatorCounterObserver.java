package edu.upc.cnds.collectivesim.experiment;

import java.util.Map;

import edu.upc.cnds.collectivesim.dataseries.DataSeries;

/**
 * Creates a DataSeries from the values calculated using a series of
 * counters from the experiment.
 * 
 * @author Pablo Chacin
 *
 */
public class FunctionCalculatorCounterObserver implements Runnable {

	/**
	 * Counters use to calculate values
	 */
	private Counter[] counters;
	
	/**
	 * DataSeries on which the values are placed
	 */
	private DataSeries series;
	
	/**
	 * Function used to calculate the DataSeries value
	 */
	private Function function;
	
	/**
	 * Attributes added to each dataitem
	 */
	private Map attributes;
	
	
	/**
	 * Constructor with full attributes
	 * 
	 * @param counters
	 * @param series
	 * @param function
	 * @param attributes
	 */
	public FunctionCalculatorCounterObserver(Counter[] counters, DataSeries series,
			Function function, Map attributes) {
		this.counters = counters;
		this.series = series;
		this.function = function;
		this.attributes = attributes;
	}



	@Override
	public void run() {
		Double[] values = new Double[counters.length];
		for(int i=0;i<counters.length;i++){
			values[i] = counters[i].getValue();
		}
		
		Double value = function.calculate(values);
		
		series.addItem(attributes,value);

	}

}
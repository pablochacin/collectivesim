package edu.upc.cnds.collectivesim.state;

import edu.upc.cnds.collectivesim.dataseries.DataSeries;

/**
 * 
 * Generates a DataSeries from a state value by observing it periodically.
 * 
 * 
 * @author Pablo Chacin
 *
 */
public class StateValueObserver implements Runnable {

	/**
	 * StateValue used as data series source
	 */
	private StateValue state;
	
	/**
	 * DataSeries generated
	 */
	private DataSeries series;
	
	/**
	 * Indicates if the value reported is the accumulated or the increment between updates
	 */
	private boolean incremental;
	
	/**
	 * Last value received, used for incremental reports
	 */
	private Double lastValue = 0.0;
		
	
	public StateValueObserver(StateValue state, DataSeries series,boolean incremental) {
		this.state = state;
		this.series = series;
		this.incremental = incremental;
	}


	@Override
	public void run() {
		
		Double value = state.getValue();
		if(incremental) {
		 Double currentValue = value;	
		 value = value - lastValue;
		 lastValue = currentValue;
		}
		
		series.addItem( state.getName(),value);
	}

}

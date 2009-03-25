package edu.upc.cnds.collectivesim.experiment;

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
	private StateValue value;
	
	/**
	 * DataSeries generated
	 */
	private DataSeries series;
		
	
	public StateValueObserver(StateValue value, DataSeries series) {
		this.value = value;
		this.series = series;
	}


	@Override
	public void run() {
		series.addItem(value.getValue());
	}

}

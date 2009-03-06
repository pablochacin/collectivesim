package edu.upc.cnds.collectivesim.experiment;

import edu.upc.cnds.collectives.dataseries.DataSeries;
import edu.upc.cnds.collectives.dataseries.InvalidDataItemException;

/**
 * 
 * Generates a DataSeries from the values of a Counter
 * 
 * @author Pablo Chacin
 *
 */
public class CounterObserverAction implements Runnable {

	/**
	 * Counter used as data series source
	 */
	private Counter counter;
	
	/**
	 * DataSeries generated
	 */
	private DataSeries series;
		
	
	public CounterObserverAction(Counter counter, DataSeries series) {
		this.counter = counter;
		this.series = series;
	}



	@Override
	public void run() {
		
		try {
			series.addItem(counter.getAsDataItem());
		} catch (InvalidDataItemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

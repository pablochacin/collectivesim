package edu.upc.cnds.collectivesim.experiment.base;

import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.DataSeriesObserver;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;

/**
 * Applies a function to a target DataSeries to produce a result DataSeries.
 * The function is applied either periodically or when the target DataSeries is
 * updated.
 * 
 * @author Pablo Chacin
 *
 */
public class DataSeriesObserverTask implements Runnable, DataSeriesObserver {

	DataSeries targetSeries;
	
	SeriesFunction function;
	
	DataSeries resultSeries;
	
	public DataSeriesObserverTask(DataSeries targetSeries, SeriesFunction function,
			DataSeries resultSeries) {
		
			this.targetSeries = targetSeries;
			this.function = function;
			this.resultSeries = resultSeries;
		
	}

	@Override
	public void run() {
		function.apply(targetSeries, resultSeries);
	}

	@Override
	public void update(DataItem item) {
		run();
	}

}

package collectivesim.experiment.base;

import collectivesim.dataseries.DataItem;
import collectivesim.dataseries.DataSequence;
import collectivesim.dataseries.DataSeries;
import collectivesim.dataseries.DataSeriesObserver;
import collectivesim.dataseries.SeriesFunction;

/**
 * Applies a function to a target DataSeries to produce a result DataSeries.
 * The function is applied either periodically or when the target DataSeries is
 * updated.
 * 
 * @author Pablo Chacin
 *
 */
public class DataSeriesObserverTask implements Runnable, DataSeriesObserver {

	protected DataSeries targetSeries;
	
	protected SeriesFunction function;
	
	protected DataSeries resultSeries;
	
	/**
	 * indicates if the new values must be appended to the result DataSeries (true)
	 * or they will replace existing values (false)
	 */
	protected boolean append;
	
		
	public DataSeriesObserverTask(DataSeries targetSeries, SeriesFunction function,
			DataSeries resultSeries,boolean autoUpdate,boolean append) {
		
			this.targetSeries = targetSeries;
			this.function = function;
			this.resultSeries = resultSeries;
			this.append = append;
			
			if(autoUpdate){
				function.reset();
				targetSeries.addObserver(this);
			}
			
		
	}

	@Override
	public void run() {
		if(!append){
			resultSeries.reset();
		}
		
		function.reset();
		
		DataSequence items = targetSeries.getSequence();
		while(items.hasItems()){
			function.processItem(items.getItem());
		}
		
		function.calculate(resultSeries);
	}

	@Override
	public void update(DataItem item) {
		function.processItem(item);
		resultSeries.reset();
		function.calculate(resultSeries);
	}

}

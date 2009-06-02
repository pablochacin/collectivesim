package edu.upc.cnds.collectivesim.dataseries.functions;

import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;


public class RunningAverage extends AbstractIncrementalFunction {

	/**
	 * Number of items to consider to calculate the average
	 */
	private int period;

	private Double sum;

	protected Double visited;
	
	public RunningAverage(String attribute, int period) {
		super(attribute);
		this.period = period;
	}
	
	
	@Override
	public void reset() {
		sum = new Double(0.0);
		visited = new Double(0.0);
	}



	@Override
	protected void getResult(DataSeries result) {
		Double average = Double.NaN;
		
		if(visited > 0) 
			average = sum/visited;
	
		result.addItem("average", average);
	}


	@Override
	protected boolean processItem(DataItem item) {
		sum = sum + item.getDouble(attribute);
		visited++;
		if(visited == period) {
			return false;
		}
		
		return true;
	}

}

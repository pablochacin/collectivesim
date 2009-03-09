package edu.upc.cnds.collectives.dataseries.functions;

import edu.upc.cnds.collectives.dataseries.DataItem;
import edu.upc.cnds.collectives.dataseries.SeriesFunction;

public class RunningAverage implements SeriesFunction {

	/**
	 * Number of items to consider to calculate the average
	 */
	private int period;

	private Double sum;

	private int visited;
	
	public RunningAverage(int period) {
		this.period = period;
	}
	
	public Double getResult() {
	 if(visited > 0) 
		 return sum/visited;
	 else
		 return Double.NaN;
	}

	public void reset() {
		sum = new Double(0);
		visited = 0;
	}

	public boolean calculate(DataItem item) {
		sum = sum + item.getValue();
		visited++;
		if(visited == period) {
			return false;
		}
		
		return true;
	}

}

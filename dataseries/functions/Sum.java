package edu.upc.cnds.collectivesim.dataseries.functions;

import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;

public class Sum implements SeriesFunction {

	Double sum;
	
	public Double getResult() {
		return sum;
	}

	public void reset() {
		sum = 0.0;
	}

	public boolean calculate(DataItem item) {
		sum += item.getValue();
		return true;
	}

}

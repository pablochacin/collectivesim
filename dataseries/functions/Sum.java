package edu.upc.cnds.collectives.dataseries.functions;

import edu.upc.cnds.collectives.dataseries.DataItem;
import edu.upc.cnds.collectives.dataseries.SeriesFunction;

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

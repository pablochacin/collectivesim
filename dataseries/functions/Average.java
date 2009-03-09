package edu.upc.cnds.collectivesim.dataseries.functions;

import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;

public class Average implements SeriesFunction {

	Double sum;
	Double count;
	
	public Double getResult() {
		if(count == 0.0){
			return Double.NaN;
		}
		
		return count/sum;
	}

	public void reset() {
		sum = 0.0;
		count = 0.0;
	}

	public boolean calculate(DataItem item) {
		sum += item.getValue();
		count++;
		return true;
	}

}

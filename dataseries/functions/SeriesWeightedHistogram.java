package edu.upc.cnds.collectivesim.dataseries.functions;

import edu.upc.cnds.collectivesim.dataseries.DataItem;

public class SeriesWeightedHistogram extends SeriesHistogram {

	/**
	 * Attribute to read the count of elements of a given attribute
	 */
	protected String countAttribute;
	
	public SeriesWeightedHistogram(String attribute, String count, Double binWidth) {
		super(attribute, binWidth);
		this.countAttribute = count;
		
	}


	public SeriesWeightedHistogram(String attribute, String count, Double min, Double max,
			int numBins, boolean truncate) {
		super(attribute, min, max, numBins, truncate);
		this.countAttribute = count;
	}


	@Override
	public boolean processItem(DataItem item) {

		Double value = item.getDouble(attribute);
		Double count = item.getDouble(countAttribute);
		
		histogram.addValue(value, count);
		
		return true;
	}

	
	
	
	

}

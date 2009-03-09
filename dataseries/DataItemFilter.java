package edu.upc.cnds.collectivesim.dataseries;


/**
 * Filters data items added to a DataSeries
 * 
 * @author Pablo Chacin
 *
 */
public interface DataItemFilter {
	
	/**
	 * 
	 * @param series
	 * @param item the DataItem
	 * @return true if the items pass the filter (and should be added)
	 */
	public boolean filter(String series, DataItem item);
}

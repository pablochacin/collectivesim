package edu.upc.cnds.collectivesim.dataseries;

/**
 * Receives updates of a DataSeries.
 * 
 * @author Pablo Chacin
 *
 */
public interface DataSeriesObserver {

	public void update(DataItem item);
}

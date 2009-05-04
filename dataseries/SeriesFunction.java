package edu.upc.cnds.collectivesim.dataseries;


/**
 * Defines an operator that can be applied to a DataSeries.
 * 
 * @author Pablo Chacin
 *
 */
public interface SeriesFunction {
	
/**
 *  Produces one or more DataItems to the result DataSeries
 * @param series
 * @param result
 */
	public void apply(DataSeries series, DataSeries result);
	
	
}

package edu.upc.cnds.collectivesim.dataseries;


/**
 * Mantais a set of data series. 
 * 
 * 
 * @author Pablo Chacin
 *
 */
public interface DataSet {
	
	
	/**
	 * 
	 * @return the number of data sets in the set
	 */
	public int getNumSeries();
	
	/**
	 * Returns the names of the series in the set
	 */
	public String[] getSeriesNames();
	
	/**
	 * Adds a new series to the set
	 * @param name of the Series
	 * @param size maximun number of elements in the series
	 */
	public DataSeries addSeries(String name,int size);
	

	/**
	 * merges the data items of the dataset with those of another dataset. Is a series already exits
	 * values are added. In a series doesn't exist, is created.
	 * 
	 * @param set a DataSet 
	 * 
	 */
	public void mergeDataSet(DataSet set) throws InvalidDataItemException;
	

	/**
	 * Returns a series in the dataset
	 * @param name
	 * @return
	 */
	public DataSeries getSeries(String name);
	
}

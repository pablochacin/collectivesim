package edu.upc.cnds.collectivesim.dataseries;

import java.util.Map;

/**
 * Maintains a set of data items. 
 * 
 * 
 * @author Pablo Chacin
 *
 */
public interface DataSeries {
	
	/**
	 * Adds a data item to the plot
	 * 
	 * @param serie Series to which this item belongs
	 * @param item value of the item
	 * 
	 * @return true if the item was added, false otherwise 
	 * 
	 */
	public boolean addItem(DataItem item);
	
	/**
	 * Convenience method to add values which has not associated categories
	 * (this is 	public ModelDataSeries(DataSeries series){
		this(series,false);
	}
 for time series)
	 * 
	 * @param value
	 * @return true if the item was added, false otherwise 
	 */
	public boolean addItem(Double value);
	
	/**
	 * Convenience Method to added data items without instancing a DataItem class
	 * 
	 * @param categories
	 * @param value
	 * @return true if the item was added, false otherwise 
	 */
	public boolean addItem(Map categories,Double value);
	
	/**
	 * Adds a filter to select items that are added to the series
	 * @param filter
	 */
	public void setFilter(DataItemFilter filter);
	
	
	/**
	 * Sets the maximun size (number of elements) of any series in the the data set.
	 * Adding more than this number of elements will produce diverse behaviors
	 * depending on the implementation (e.g. not include the last element, or lost the oldest one)
	 * 
	 * Existing elements are also kept/loose accordingly to this bahavior.
	 */
	public void resize(int size);
	
	/**
	 * 
	 * @return the maximun size of the dataset
	 */
	public int getSize();
	
	
	/**
	 * Returns the name of the series
	 * 
	 * @return
	 */
	public String getName();
	
	
	/**
	 * 
	 * @param index relative position of the item in the series
	 * 
	 * @return the i-th element of the data series. 
	 * 
	 * @throws IndexOutOfBoundsException if the index if out of bounds
	 */
	public DataItem getItem(int index);

	
	/**
	 * @return a sequence with the current values of the series
	 */
	public DataSequence getSequence();
	

	
	/**
	 * Removes the series' data items.
	 * 
	 */
	public void reset();
	
	/**
	 * 
	 * @param observer an DataSequenceObserver to be called when the sequence has a new value
	 * 
	 */
	public void addObserver(DataSeriesObserver observer);
	
	/**
	 * Removes the observer
	 * 
	 * @param observer
	 */
	public void removeObserver(DataSeriesObserver observer);
	
	
	/**
	 * Applies a function to the series and returns a result
	 * 
	 * @param function

	 * @return
	 */
	public Double applyFunction(SeriesFunction function);
	
	/**
	 * Applies a series of functions to the values of a series and returns
	 * the results as an array of Doubles.
	 * 
	 * This method is convenient when iterating along very large series
	 * stored (or partially backed) in secondary storage to avoid repetitive
	 * scans.
	 * 
	 * @param functions
	 * @return
	 */
	public Double[] applyFunctions(SeriesFunction functions[]);
	
}

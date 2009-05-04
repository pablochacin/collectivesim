package edu.upc.cnds.collectivesim.dataseries;

import java.util.Map;

/**
 * Maintains a series of DataItems. 
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
	 * Adds a data item specifying the values for the 
	 * @param value
	 * @return true if the item was added, false otherwise 
	 */
	public boolean addItem(Map<String,Object> attributes);
	
	/**
	 * Convenience method used to add a data item with only one attribute
	 * @param attribute
	 * @param value
	 * @return
	 */
	public boolean addItem(String attribute,Double value);

	/**
	 * Convenience method used to add a data item with only one attribute
	 * @param attribute
	 * @param value
	 * @return
	 */
	public boolean addItem(String attribute,String value);

	/**
	 * Convenience method used to add a data item with only one attribute
	 * @param attribute
	 * @param value
	 * @return
	 */
	public boolean addItem(String attribute,Boolean value);

	
	/**
	 * Adds a filter to select items that are added to the series
	 * @param filter
	 */
	public void setFilter(DataItemFilter filter);
	
	
	/**
	 * Sets the maximum size (number of elements) of any series in the the data set.
	 * Adding more than this number of elements will produce diverse behaviors
	 * depending on the implementation (e.g. not include the last element, or lost the oldest one)
	 * 
	 * Existing elements are also kept/loose accordingly to this bahavior.
	 */
	public void resize(int size);
	
	/**
	 * 
	 * @return the maximum size of the series
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
	
	
	
}

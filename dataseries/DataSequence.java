package edu.upc.cnds.collectivesim.dataseries;

/**
 * A stream of DataItems
 * 
 * @author pchacin
 *
 */
public interface DataSequence {

	/**
	 * Returns the "next" value of the sequence. The meaning of
	 * "next" depends of the implementation. Repetitive calls to this
	 * method might return the same value. If the sequence is "empty"
	 * a null is returned.
	 * 
	 * @return
	 */
	public DataItem getItem();
	
	/**
	 * 
	 * @return true if the sequence has at least one item available for the getItem method
	 */
	public boolean hasItems();
	
	
}

package collectivesim.util;

import java.util.List;



/**
 * Selects a sample of objects from a List according to a
 * selection criteria.
 * 
 * @author Pablo Chacin
 *
 */
public interface Selector<T> {

	/**
	 * Selects a sample of (at most) a given number of elements
	 * 
	 * @param size maximum number or elements of the sample
	 * 
	 * @return a List of objects nodes that meets the selection criteria
	 */
	public List<T> getSample(List<T> objectes,int size);
}

package collectivesim.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import collectivesim.util.Selector;

/**
 * Samples the n first objects from a list based on a comparator
 * 
 * @author Pablo Chacin
 *
 */
public class OrderedSelector<T> implements Selector<T> {

	private Comparator<T> comparator;
	
	public OrderedSelector(Comparator<T> comparator) {
		this.comparator = comparator;
	}
	
	public List<T> getSample(List<T> objects, int size) {

			//keep lowest part, including head
			List<T> sample = new ArrayList<T>();

			//if already has as many as requested, return the actual content
			if(objects.size() <= size) {
				sample.addAll(objects);
			}
			else{
				//if not, add the lowest (first) size elements
				//sorting first to be ensure proper order
				Collections.sort(objects,comparator);

				for(int i = 0;i < size;i++) {
					sample.add(objects.get(i));
				}
			}

			return sample;


		}


}

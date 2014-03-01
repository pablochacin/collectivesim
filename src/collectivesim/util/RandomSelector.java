package collectivesim.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import collectivesim.util.Selector;

/**
 * Obtains a random sample of the objects from a List
 * 
 * @author Pablo Chacin
 *
 */
public class RandomSelector<T> implements Selector<T> {

	public List<T> getSample(List<T> objects, int size) {
	
		List<T>sample = new ArrayList<T>();

		//if already within limit, return same
		if(objects.size() <= size) {

			sample.addAll(objects);
		}
		else {
			//arrange the nodes randomly
			List<T> randomObjects = new ArrayList<T>(objects);
			Collections.shuffle(randomObjects);

			//return the beginning of the list
			sample.addAll(randomObjects.subList(0, size));
		}

		return sample;
	}

}

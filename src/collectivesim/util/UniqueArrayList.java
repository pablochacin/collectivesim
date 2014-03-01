package collectivesim.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Extension to {@link ArrayList} that assures repeated objects are not
 * added to the list. Relies on the objects to properly implement 
 * equalsTo method.
 * 
 * @author Pablo Chacin
 *
 * @param <T>
 */
public class UniqueArrayList<T> extends ArrayList<T> {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UniqueArrayList(Collection<T> c) {
		super(c.size());
		addAll(c);
	}

	public UniqueArrayList() {
		super();
	}

	public UniqueArrayList(int size){
		super(size);
	}
	
	@Override
	public void add(int index, T element) {
		if(!contains(element)) {
			super.add(index, element);
		}

	}

	@Override
	public boolean add(T e) {
		if(!contains(e)) {
			return super.add(e);
		}
		return true;
	}

	@Override
	public boolean addAll(Collection c) {
		ArrayList tmp = new ArrayList();
		tmp.addAll(c);
		tmp.removeAll(this);
		return super.addAll(tmp);
	}

	@Override
	public boolean addAll(int index, Collection c) {
		ArrayList tmp = new ArrayList();
		tmp.addAll(c);
		tmp.removeAll(this);
		return super.addAll(index, tmp);
	}

}

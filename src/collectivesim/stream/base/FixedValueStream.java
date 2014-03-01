package collectivesim.stream.base;

import collectivesim.stream.Stream;


/**
 * A simple Stream that return always the same value
 * 
 * @author Pablo Chacin
 * @param <T>
 *
 */
public class FixedValueStream<T> implements Stream<T>{

	
	private T value;
	
	public FixedValueStream( T value) {
		if(value == null){
			throw new IllegalArgumentException("Value cannot be null");
		}
		this.value = value;		
	}
	
	

	public T nextElement() {
	
		return value;
	}


	@Override
	public void reset() {
		//DO nothing.
		
	}

	@Override
	public void open() {
		// Do nothing.
		
	}
	
	public String toString(){
		return "Fixed value " + value.toString();
	}
	
	@Override
	public boolean hasMoreElements(){
		return true;
	}
}

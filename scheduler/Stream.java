package edu.upc.cnds.collectivesim.scheduler;

/**
 * A stream of values that can feed the simulation.
 * The objects can be obtained from any external source
 * like a file, a random number generator or even be a fixed value.
 * 
 * @author Pablo Chacin
 *
 */
public interface Stream<T> {

	/**
	 * 
	 * @return the name of the Stream
	 */
	public String getName();
	
	
	/**
	 * Return the next value of the Stream
	 * 
	 * @return
	 */
	public T getValue();
}

package edu.upc.cnds.collectivesim.stream;

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
	 * Initiates the access to the Stream. Useful when accessing external resources like files or data bases.
	 * 
	 * @throws StreamException
	 */
	public void open() throws StreamException;
	
	/**
	 * Return the next value of the Stream
	 * 
	 * @return
	 */
	public T getValue();
	
	/**
	 * Resets the stream. This is convenient to re-position file bases streams
	 * or change the seed for random value streams.
	 */
	public void reset();
}

package edu.upc.cnds.collectivesim.stream;

import java.util.Enumeration;

import edu.upc.cnds.collectives.factory.Factory;

/**
 * A stream of values that can feed the simulation.
 * The objects can be obtained from any external source
 * like a file, a random number generator or even be a fixed value.
 * 
 * @author Pablo Chacin
 *
 */
public interface Stream<T> extends Enumeration<T>, Factory<T> {

	/**
	 * 
	 * @return the name of the Stream
	 */
	//public String getName();
	
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
	public T nextElement();
	
	
	/**
	 * Indicates if the Stream has more elements to be retrieved with the {@link #nextElement()} method
	 * 
	 * @return true if there are more elements, false otherwise
	 */
	public boolean hasMoreElements();
	/**
	 * Resets the stream. This is convenient to re-position file bases streams
	 * or change the seed for random value streams.
	 */
	public void reset();
}

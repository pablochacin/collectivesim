package edu.upc.cnds.collectivesim.models;

/**
 * A stream of objects that can feed the simulation.
 * The objects can be obtained from any external source
 * like a file, a random number generator or even be a fixed value.
 * 
 * @author Pablo Chacin
 *
 */
public interface Stream {

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
	public Object getValue();
}

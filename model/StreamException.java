package edu.upc.cnds.collectivesim.model;


/**
 * Indicates an Exception accessing an Stream
 * 
 * @author Pablo Chacin
 *
 */
public class StreamException extends Exception {

	public StreamException() {
		super();
	}

	public StreamException(String message, Throwable cause) {
		super(message, cause);
	}

	public StreamException(String message) {
		super(message);
	}

	public StreamException(Throwable cause) {
		super(cause);
	}

}

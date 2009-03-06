package edu.upc.cnds.collectivesim.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Reads  a series of objects from a file stream
 */
public class FileStream<T> implements Stream<T> {

	private String name;
	
	
	private ObjectInputStream input;
	
	/**
	 * Constructor from the path to a file
	 * 
	 * @param name a String with the  name of the stream
	 * @param file a String with the path to the input Stream
	 */
	public FileStream(String name, String file){
		try {
			this.name = name;
			InputStream inStream = new FileInputStream(new File(file));
			this.input = new ObjectInputStream(inStream);
		} catch (Exception e) {
			throw new IllegalArgumentException("Exception accessing file " + file,e);
		}

	}
	
	/**
	 * Constructor from a FileInputStream
	 * 
	 * @param name a String with the  name of the stream
	 * @param input a FileImputStram from which the stream's objects are read
	 */
	public FileStream(String name,FileInputStream input){
		this.name = name;
		try {
			this.input = new ObjectInputStream(input);
		} catch (IOException e) {
			throw new IllegalArgumentException("Exception accessing input stream " ,e);

		}
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public T getValue() {

		try {
			return (T)(input.readObject());
		} catch (Exception e) {
			//TODO: handle properly this exception. 
			return null;
		} 
	}

}

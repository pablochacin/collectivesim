package edu.upc.cnds.collectivesim.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;

import edu.upc.cnds.collectives.identifier.BasicIdentifier;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.util.ReflectionUtils;

/**
 * Reads  a series of objects from a file stream
 */
public class FileStream<T> implements Stream<T> {

	private String name;
	
	
	private RandomAccessFile input;
	
	private Class type;
	
	private String file;
	
	/**
	 * Constructor from the path to a file
	 * 
	 * @param name a String with the  name of the stream
	 * @param file a String with the path to the input Stream
	 */
	public FileStream(String name, String file,Class type){
		try {
			this.name = name;
			this.file = file;
			this.type = type;
			input = new RandomAccessFile(file,"r");
		} catch (Exception e) {
			throw new IllegalArgumentException("Exception accessing file " + file,e);
		}

	}
	

	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public T getValue() {

		try {
			String value = input.readLine(); 
			return (T) ReflectionUtils.parseValue(value,type);
		} catch (Exception e) {
			//TODO: handle properly this exception. 
			return null;
		} 
	}

	
	@Override
	public void reset() {

		try {
			input.seek(0);
		} catch (IOException e) {
			throw new IllegalStateException("Can't reset file stream for file " + file,e);
		}
	}


}

package collectivesim.stream.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import collectives.util.ReflectionUtils;
import collectivesim.stream.Stream;
import collectivesim.stream.StreamException;

/**
 * Reads  a series of objects from a file stream
 */
public class FileStream<T> implements Stream<T> {
	
	
	private RandomAccessFile input;
	
	private Class type;
	
	private String file;
	
	
	/**
	 * Constructor from the path to a file
	 * 
	 * @param name a String with the  name of the stream
	 * @param file a String with the path to the input Stream
	 */
	public FileStream(String file,Class type){
			this.file = file;
			this.type = type;
	}
	

	public void open() throws StreamException{
		try {
			input = new RandomAccessFile(file,"r");
		} catch (FileNotFoundException e) {
			throw new StreamException("Exception accessing file "+file,e);
		}
	}
	
	@Override
	public T nextElement() {

		String value = null;
		try {
			value = input.readLine(); 
			return (T) ReflectionUtils.parseValue(value,type);
		} catch (Exception e) {
			throw new IllegalArgumentException("Value " + value + " is not valid for type " + type.getName());
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

	
	public String toString(){
		return "File " + file;
	}

	public boolean hasMoreElements(){
		try {
			return (input.getFilePointer() < input.length());
		} catch (IOException e) {
			return false;
		}
	}
}

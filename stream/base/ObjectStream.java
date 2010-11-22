package edu.upc.cnds.collectivesim.stream.base;

import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectives.util.ReflectionUtils;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.stream.StreamException;

/**
 * Creates a Stream of objects of a given class, using the class' constructor
 * and a list of Streams used to get the arguments for that constructor.
 * 
 * @author Pablo Chacin
 *
 */
public class ObjectStream  implements Stream<Object> {


	/**
	 * Name of the class to instantiate
	 */

	private String className;
	
	
	private Class objectClass;
	
	/**
	 * Streams o
	 */
	private Stream[] argumentStreams;
	
	public ObjectStream(String className, Stream ... argumentStreams) {
		super();
		this.className = className;
		this.argumentStreams = argumentStreams;
	}


	@Override
	public boolean hasMoreElements() {
		return true;
	}

	@Override
	public Object nextElement() {
		
		Object[] arguments =  new Object[argumentStreams.length];
		
		for(int i=0;i<arguments.length;i++) {
			arguments[i] = argumentStreams[i].nextElement();
		}
		
		try {
			
			Object instance = ReflectionUtils.createInstance(objectClass, arguments);
			return instance;
			
		} catch (Exception e) {
			throw new InstantiationError("Exception instantiating class " + className + 
					                     " :" + e.getClass().getName());
		}
		
		
		
	}

	@Override
	public void open() throws StreamException {
	
		try {
			objectClass = this.getClass().getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new StreamException("Class Not found " + className);
		}
	}

	@Override
	public void reset() {

		
	}

}

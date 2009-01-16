package edu.upc.cnds.collectivesim.model.imp;

import java.lang.reflect.Method;

import edu.upc.cnds.collectives.util.FormatException;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;

/**
 * Implements a ModelAgent by delegating to a target object using 
 * reflexion.  This allows using any class as a Model Agent.
 * 
 * The target must expose attributes by means of getter methods.
 * 
 * @author Pablo Chacin
 *
 */
public class ReflexionModelAgent implements ModelAgent {

	/**
	 * target object that will handle requests
	 */
	private Object target;
	
	/**
	 * Type of the agent
	 */
	private String type;
	
	/**
	 * Constructor without parameters. Used for convenience.
	 * Assumes the name of the class as the type and the object itself as
	 * its own target. Useful for classes that extend this base class and don't
	 * need to differentiate type of objects
	 */
	public ReflexionModelAgent() {
		this.type = this.getClass().getSimpleName();
		this.target = this;
	}
	
	
	/**
	 * Constructor without target. Assumes that this object is its own target
	 * Useful for classes that extend this base class
	 * 
	 * @param type
	 */
	public ReflexionModelAgent(String type) {
		this.type = type;
		this.target = this;

	}
	
	
	/**
	 * Constructor with an "external" target.
	 *  
	 * @param type
	 * @param target
	 */
	public ReflexionModelAgent(String type, Object target) {
		this.type = type;
		this.target = target;
	}
	
	public void execute(String action, Object ... args) throws ModelException {
		try {			
			
			
			Class[] classes = new Class[args.length];
			for(int i=0;i <args.length;i++){
				classes[i] = args[i].getClass();
			}
			
			Method m = target.getClass().getMethod(action, classes);

			m.invoke(target, args);
			
		} catch (Exception e) {
			throw new ModelException("Exception executing action "+ action +FormatException.getStackTrace(e));
			
		} 
		
	}

	public Object getAttribute(String attribute) throws ModelException{
		String getter = "get"+ attribute;
		Method m;
		try {
			m = target.getClass().getMethod(getter, new Class[0]);
			return m.invoke(target, (Object[])null);
		} catch (Exception e) {
			throw new ModelException("Exception accesssing attribute "+ attribute +FormatException.getStackTrace(e));

		} 

		
	}
	
	public String getType() {
		return type;
	}

}

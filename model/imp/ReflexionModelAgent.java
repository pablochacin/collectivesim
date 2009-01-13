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

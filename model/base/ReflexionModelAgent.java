package edu.upc.cnds.collectivesim.model.base;

import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectives.util.ReflectionUtils;
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
	 * Counts the agents created for each subclass, to be used when generating 
	 * agent names 
	 */
	private static int agentCounter = 0;
	
	/**
	 * target object that will handle requests
	 */
	private Object target;
	
	/**
	 * Name of the agent
	 */
	private String name;
	
	/**
	 * Constructor without parameters. Used for convenience.
	 * Assumes the name of the class as the type and the object itself as
	 * its own target. Useful for classes that extend this base class and don't
	 * need an specific name. 
	 */
	public ReflexionModelAgent() {
		this.name = generateName(this);
		this.target = this;
	}
	
	
	/**
	 * Constructor without target. Assumes that this object is its own target
	 * Useful for classes that extend this base class
	 * 
	 * @param name
	 */
	public ReflexionModelAgent(String name) {
		this.name = name;
		this.target = this;

	}
	
	
	/**
	 * Constructor with an "external" target. Uses target's class name plus a correlative
	 * as the agent's name. 
	 *  
	 * @param target
	 */
	public ReflexionModelAgent(Object target) {
		this.name = generateName(target);
		this.target = target;
	}
		
	/**
	 * Constructor with an "external" target. The agent will redirect all 
	 * attribute inquires and method invocations to the target using reflexion.
	 * Useful to create agents from classes that must inherit another
	 * super classes. 
	 *  
	 * @param name
	 * @param target
	 */
	public ReflexionModelAgent(String name, Object target) {
		this.name = name;
		this.target = target;
	}
	
	/**
	 * Generates an agent name from the agent's classname and a correlative number.
	 * Correlative is increase for each instance of this class, including all subclasses.
	 * Therefore, it might not be correlative acroos all the instances of a given subclass
	 * resulting in names like class1-1, class1-2, class2-3, class1-4 ....
	 *  
	 * @return a name composed of the class name and a correlative
	 */
	private String generateName(Object obj){
		return obj.getClass().getSimpleName()+"-"+agentCounter++;
	}

	
	public void execute(String action) throws ModelException {
		execute(action, new Object[0]);
	}
	
	public void execute(String action, Object[] args) throws ModelException {
		try {			

			ReflectionUtils.invoke(target,action, args);
			
		} catch (Exception e) {
			throw new ModelException("Exception executing action "+ action +" " +FormattingUtils.getStackTrace(e));
			
		} 
		
	}

	public Object getAttribute(String attribute) throws ModelException{
		String getter = "get"+ attribute;
		try {
			return ReflectionUtils.invoke(target, getter, new Object[0]);
			
		} catch (Exception e) {
			throw new ModelException("Exception accesssing attribute "+ attribute +" "+FormattingUtils.getStackTrace(e));

		} 

		
	}
	
	public String getName() {
		return name;
	}

}

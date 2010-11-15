package edu.upc.cnds.collectivesim.model.base;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectives.util.ReflectionUtils;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;

/**
 * Implements a ModelAgent by delegating to a target object using 
 * reflection.  This allows using any class as a Model Agent.
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
	 * 
	 */
	protected Model<? extends ModelAgent> model;
	
	/**
	 * target object that will handle requests
	 */
	private Object target;
	
	/**
	 * Name of the agent
	 */
	private String name;
	
	/**
	 * Attributes exposed by this agent
	 */
	protected String[] attributeNames;
	
	/**
	 * Constructor without parameters. Used for convenience.
	 * Assumes the name of the class as the type and the object itself as
	 * its own target. Useful for classes that extend this base class and don't
	 * need an specific name. 
	 */
	public ReflexionModelAgent(Model model) {
		this.name = generateName(this);
		this.target = this;
		this.model = model;
	}
	
	
	/**
	 * Constructor without target. Assumes that this object is its own target
	 * Useful for classes that extend this base class
	 * 
	 * @param name
	 */
	public ReflexionModelAgent(Model model,String name) {
		this.name = name;
		this.target = this;
		this.model = model;
		attributeNames = getAttributeNames(target);
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
	public ReflexionModelAgent(Model model,String name, Object target) {
		this.model= model; 
		this.name = name;
		this.target = target;			
		attributeNames = getAttributeNames(target);
		
	}
	
	/**
	 * Constructor with an "external" target. Uses target's class name plus a correlative
	 * as the agent's name. 
	 *  
	 * @param target
	 */
	public ReflexionModelAgent(Model model,Object target) {
		this(model,generateName(target),target);
	}
		
	
	@Override
	public Model getModel(){
		return model;
	}
	
	/**
	 * Retrieves the names of the attributes accessible by getter methods
	 * 
	 * @param object the Object from which the getters must be extracted
	 * 
	 * @return
	 */
	private static String[] getAttributeNames(Object object){
		
		String[] names;
		try {
			names = (String[])ReflectionUtils.invoke(object, "getAttributeNames", new Object[0]);
			return names;
		} catch (Exception e) {
			//ignore exceptions
		}
		
		//if no attributes are defined, expose all attributes exposed by get methods		
		List<Method>getters = ReflectionUtils.getGetters(object.getClass());
		names = new String[getters.size()];
		for(int i=0;i<names.length;i++){
			//get the name without the "get"
			names[i] = getters.get(i).getName().substring(3);
		}
		
		return names;
	}
	
	
	/**
	 * Generates an agent name from the agent's classname and a correlative number.
	 * Correlative is increase for each instance of this class, including all subclasses.
	 * Therefore, it might not be correlative acroos all the instances of a given subclass
	 * resulting in names like class1-1, class1-2, class2-3, class1-4 ....
	 *  
	 * @return a name composed of the class name and a correlative
	 */
	private static String generateName(Object obj){
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

	
	@Override
	public String[] getAttributeNames(){
		return attributeNames;
	}
	
	@Override
	public Map<String,Object> inquire(){
		
		return inquire(getAttributeNames());
	}
	
	@Override
	public Map<String,Object> inquire(String[] attributeNames){
		Map<String,Object> attributes = new HashMap<String,Object>();
		for(String a: attributeNames){
			try {
				attributes.put(a,inquire(a));
			} catch (ModelException e) {
				//ignore if an attribute is not available
			}
			
		}
		
		return attributes;
	}
 	
	public Object inquire(String attribute) throws ModelException{
		
		//TODO: first should check if the attribute is accessible (is in the attributes array)
		
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


	@Override
	public void finish() {
		
	}


	@Override
	public void init() {
		
	}

}

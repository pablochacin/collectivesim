package edu.upc.cnds.collectivesim.collective.imp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import edu.upc.cnds.collectivesim.models.Action;
import edu.upc.cnds.collectivesim.models.BasicModel;

import uchicago.src.sim.util.SimUtilities;


/**
 * 
 * @author pchacin
 *
 */
public class BehaviorWithReflection {

	/**
	 * Order of execution of methods of the behavior
	 */
	public static long BEHAVIOR_ORDER_BY_AGENT = 0;
	public static long BEHAVIOR_ORDER_BY_METHOD = 1;
	
	
	/**
	 * Model on which the behavior inhabits
	 */
	BasicModel model;
	
	/**
	 * Realm on which the agents this model applies to, reside
	 */
	Collective realm;
	/**
	 * Frequency of execution of the behavior
	 */
	private double frequency;
	/**
	 * Agent's method to be executed
	 */
	private Method[] methods;
	
	/**
	 * Name of the methods to be executed
	 */
	private String[] methodNames;
	
	/**
	 * name of the mehavior
	 */
	private String name;
	
	/**
	 * order of execution of methods
	 */
	private long order;
	/**
	 * Default constructor
	 * @param name a String that identifies this behavior
	 * @param model the simulation Model on which this behavior inhabits
	 * @param realm the AgentRealm on which resides the agents this behavior will be applied to
	 * @param method a String array with the name of the methods to be execute
	 * @param active a boolean that indicates if the behavior must be inserted active
	 *        or will be deactivated until the realm activates it.
	 * @param frequency a long with the frequency, in ticks, of execution
	 * @param order order of execution (by agent or by method)
	 */
	protected BehaviorWithReflection(String name, BasicModel model,Collective realm,String[] methodNames, boolean active,
			           double frequency, long order){
		this.name = name;
		this.methodNames = methodNames;
		this.frequency = frequency;
	    this.model = model;
	    this.realm = realm;
	    this.order = order;

	    //if behavior must be created active
	    if(active){
	    	this.start();
	    }
 	}
	
    /**
     * starts the execution of the behavior 
     */	
	public void start(){
	    //schedule the execution of the behavior at the given frequency
	    Action execute = new Action(this,"execute",frequency);
	    model.scheduleAction(execute);	
	}
	
	/**
	 * pause the execution of the behavior
	 */
	public void pause(){
		//TODO: pause the behavior
	}
	

	
	/**
	 * executes the methods on one agents
	 *
	 */
	public void execute(){
		//get the list of agents
		
		ArrayList agentList = realm.getLocations();
		
		if(agentList!=null){
			
			//pick first agent as a prototype
			Object prototype = agentList.get(0);
			
			//iterate along the method name and retrieve the method objects
			for(int i=0;i<methodNames.length;i++){
				try {
					Method method = prototype.getClass().getMethod(methodNames[i],new Class[0]);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				//reorder agent list randomly
		        SimUtilities.shuffle(agentList);
		        
				//decide how to execute behavior: by agent or by method
				if(order == BEHAVIOR_ORDER_BY_AGENT){
					executeByAgent(agentList,methods);
				}
				else{
					if(order == BEHAVIOR_ORDER_BY_METHOD){
						executeByMethod(agentList,methods);
					}
					else{
						//TODO: handle this error!
					}
				}
				
			}
		}
	}

  /**	
   * Executes a series of method on a list of agents. All methods in the serie are 
   * executed for each agent before starting the execution in the next agent.
   * 
   * @param agentList list of agents
   * @param methods array of methods.
   */
  private void executeByAgent(ArrayList agentList,Method[] methods){
	
	  //iterate along the agents in the list
	  for(int agent=0;agent < agentList.size();agent++){
		  
		  //iterte along the methods in the vector
		  for(int method=0;method < methods.length;method++){
			  executeMethod(methods[method],agentList.get(agent));
		  }
	  }
  }
  
  /**
   * Executes a series of methods in all the members of a list of agents.
   * Each methods is executed in all the agents before the next method is
   * executed.
   * 
   * @param agentList list of agents
   * @param methods array of methods.
   */
  private void executeByMethod(ArrayList agentList,Method[] methods){

	  //iterte along the methods in the vector
	  for(int method=0;method < methods.length;method++){
		  //iterate along the agents in the list
		  for(int agent=0;agent < agentList.size();agent++){

			  executeMethod(methods[method],agentList.get(agent));
		  }
	  }
  }
  
  /**
   * Executes a method in an object
   * @param method
   * @param agent
   */
  private void executeMethod(Method method,Object obj,Object[] args){
	  try {
			//execute method on object
			method.invoke(obj, args);
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
	
  /**
   * Convenience method to invoke a method without parameters
   * @see #executeMethod(Method, Object, Object[])
   */
  private void executeMethod(Method method,Object obj){
      executeMethod(method,obj,(Object[])null);
  }
}

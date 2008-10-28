package edu.upc.cnds.collectivesim.models.imp;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;

import edu.upc.cnds.util.FormatException;

public class Action extends BasicAction {

	private static Logger log = Logger.getLogger("edu.upc.cnds.collectivesim.models");
	
	
	/**
	 * Scheduler on which this action is executed
	 */
	Schedule schedule;
	
	/**
	 * object on which the action will be executed
	 */
	private Object target;
	
	/**
	 * method to be scheduled
	 */
	private String methodName;
	
	/**
	 * arguments for the method
	 */
	private Object[] arguments;
	
	/**
	 * Deleay between executions of the action
	 */
	private double delay;

    /**
     * indicates if the action is repetitive or is executed one single time
     */
    private boolean repetitive;
	
	/**
	 * Constructor with all parameters
	 * 
	 * @param schedule the Schedule on which this action is executed
	 * @param target an Object on which the action will be executed
	 * @param methodName a String with the name of method to be scheduled
	 * @param arguments an array of Objects to be passed as arguments to the method
	 * @param delay time between executions
	 * @param repetitive indicates if the action is repetitive
	 */
	public Action(Schedule schedule,Object target,String methodName, Object[] arguments,double delay, boolean repetitive) {
		this.schedule = schedule;
		this.target = target;
        this.methodName = methodName;
        this.arguments = arguments;
        this.delay = delay;
        this.repetitive = repetitive;
	}
	
    
	public void start() {
		  if(repetitive) {
	          schedule.scheduleActionAtInterval(delay, this);
	      }
	      else{
	          schedule.scheduleActionAt(delay,this);
	      }
	}
    
	/**
	 * return frequency
	 */
	public double getDelay(){
		return this.delay;
	}
	
	/**
	 * Return target object.
	 * 
	 */
     public Object getTarget(){
    	 return this.target;
     }
     
     /**
      * Returns the name of the method to be executed
      */
     public String getMethod(){
    	 return this.methodName;
     }
     
     /**
      * Returns an idicator of whether this action is repetitive or not
      */
     public boolean isRepetitive(){
         return this.repetitive;
     }
     
     
     /**
      * Execute's the action
      *
      */
     public void execute() {
 		try {

 			Class[] classes = new Class[arguments.length];
 			                          
 			for(int i = 0;i<arguments.length;i++) {
 				classes[i] = arguments[i].getClass();
 			}
 			
			Method method = target.getClass().getMethod(methodName, classes);

			method.invoke(this, arguments);

		} catch (Exception e) {
			log.severe("Exception invoking method "+methodName+ ": "+FormatException.getStackTrace(e));
			cancel();
		} 
     }
     
    
     public void cancel() {
    	 schedule.removeAction(this);
     }
}

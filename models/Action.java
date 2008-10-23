package edu.upc.cnds.collectivesim.models;

public class Action {

	/**
	 * object on which the action will be executed
	 */
	private Object target;
	
	/**
	 * method to be scheduled
	 */
	private String method;
	
	/**
	 * frequency of the action
	 */
	private double frequency;

    /**
     * indicates if the action is repetitive or is executed one single time
     */
    private boolean repetitive;
	
	/**
	 * Constructor with all parameters
	 * 
	 * @param target an Object on which the action will be executed
	 * @param method a String with the name of method to be scheduled
	 * @param frequency a long with the frequency of the action. 0 Means
	 *        single shot action.
	 */
	public Action(Object target,String method, double frequency, boolean repetitive) {
		this.target = target;
        this.method = method;
        this.frequency = frequency;
        this.repetitive = repetitive;
	}
	
    
    
	/**
	 * return frequency
	 */
	public double getFrequency(){
		return this.frequency;
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
    	 return this.method;
     }
     
     /**
      * Returns an idicator of whether this action is repetitive or not
      */
     public boolean isRepetitive(){
         return this.repetitive;
     }
}

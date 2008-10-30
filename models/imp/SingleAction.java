package edu.upc.cnds.collectivesim.models.imp;

import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.models.Stream;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;

public class SingleAction extends BasicAction {

	private static Logger log = Logger.getLogger("collectivesim.models");
	
	
	/**
	 * object on which the action will be executed
	 */
	private Runnable target;
	

	
	/**
	 * Deleay between executions of the action
	 */
	private long delay;

	/**
	 * Constructor with all parameters
	 * 
	 * @param target an Object on which the action will be executed
	 * @param delay time between executions
	 */
	public SingleAction(Runnable target,long delay) {
		this.target = target;
        this.delay = delay;
        

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
      * Execute's the action
      *
      */
     public void execute() {
    	 
    	 target.run();
     }
     
    
}

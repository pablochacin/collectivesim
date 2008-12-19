package edu.upc.cnds.collectivesim.scheduler.repast;

import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.scheduler.Stream;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;

public class RepetitiveAction extends AbstractScheduledAction {

	private static Logger log = Logger.getLogger("collectivesim.models");
	
	
	/**
	 * Scheduler on which this action is executed
	 */
	Schedule schedule;
	
	/**
	 * object on which the action will be executed
	 */
	private Runnable target;
	
	/**
	 * Deleay between executions of the action
	 */
	private Stream distribution;
	
	private Double interval;

	
	/**
	 * Constructor with all parameters
	 * 
	 * @param schedule the Schedule on which this action is executed
	 * @param target an Object on which the action will be executed
	 * @param distribution time between executions
	 * @param repetitive indicates if the action is repetitive
	 */
	public RepetitiveAction(Schedule schedule,Runnable target,Stream distribution) {
		super(schedule);
		this.target = target;
        this.distribution = distribution;
	   
        
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
    	 //calculate the interval until next execution
    	 interval = (Double)distribution.getValue();
    	 
    	 //set interval time
    	 super.setIntervalTime(interval);
    	 
    	 //call the target
    	 target.run();
     }
          
     
     /**
      * Return the interval from the current time to the next execution
      */
     public double getIntervalTime() {
    	 return interval;
     }
     
     /**
      * Returns the next execution time, calculated from the current time
      */
     public double getNextTime() {
    	 return schedule.getCurrentTime()+interval;
     }
}

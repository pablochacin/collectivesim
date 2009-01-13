package edu.upc.cnds.collectivesim.scheduler.repast;

import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.scheduler.Stream;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;

public class RepetitiveAction extends AbstractScheduledAction {

	private static Logger log = Logger.getLogger("collectivesim.scheduler");
	
	
	/**
	 * Scheduler on which this action is executed
	 */
	Schedule schedule;
	
	/**
	 * object on which the action will be executed
	 */
	private Runnable target;
	
	/**
	 * Delay between executions of the action
	 */
	private Stream distribution;
	
	private Double interval;

	private Double endTime;
	
	private int iterations;
	
	private long maxIterations;
	
	
	
	/**
	 * Constructor with all parameters
	 * 
	 * @param schedule the Schedule on which this action is executed
	 * @param target an Object on which the action will be executed
	 * @param distribution time between executions
     * @param maxIterations maximum number of executions of the task
     * @param endTime time limit for execution of the task
	 */
	public RepetitiveAction(Schedule schedule,Runnable target,Stream distribution,long maxIterations,Double endTime) {
		super(schedule);
		this.schedule = schedule;
		this.target = target;
        this.distribution = distribution;
        this.iterations = 0;
        this.maxIterations = maxIterations;
        this.endTime = endTime;
	   
   	 //calculate the interval until next execution
   	 interval = (Double)distribution.getValue();
   	 
   	 //set interval time
   	 super.setIntervalTime(interval);

	}
	

	/**
	 * Convenience constructor without either iterations or time limits
	 * @param schedule
	 * @param target
	 * @param distribution
	 */
	public RepetitiveAction(Schedule schedule,Runnable target,Stream distribution) {
		this(schedule,target,distribution,0,0.0);
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
    	 
    	 //call the target
    	 target.run();
    	 

    	 //calculate the interval until next execution
    	 interval = (Double)distribution.getValue();

    	 //check if must continue executin
    	 iterations++;
    	 if((maxIterations != 0) && (iterations == maxIterations)) {
    		 schedule.removeAction(this);
    	 }

    	 //check if next invocation would pass execution limit
    	 if((endTime != 0) && ((schedule.getCurrentTime()+interval) >= endTime)){
    		 schedule.removeAction(this);
    		 
    	 }
    	 
    	 //set interval time
    	 super.setIntervalTime(interval);

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

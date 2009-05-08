package edu.upc.cnds.collectivesim.scheduler.repast;

import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.stream.Stream;

public class RepetitiveAction extends AbstractScheduledAction {

	private static Logger log = Logger.getLogger("collectivesim.scheduler");
	
	
		
	/**
	 * Delay between executions of the action
	 */
	private Stream<Long> frequency;
	
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
	public RepetitiveAction(RepastScheduler scheduler,Runnable target,Stream<Long> frequency,long maxIterations,Double endTime) {
		super(scheduler,target);
		this.target = target;
        this.frequency = frequency;
        this.iterations = 0;
        this.maxIterations = maxIterations;
        this.endTime = endTime;
	   
   	 //calculate the interval until next execution
   	 interval = (double)frequency.getValue();
   	 
   	 //set interval time
   	 super.setIntervalTime(interval);

	}
	

	/**
	 * Convenience constructor without either iterations or time limits
	 * @param schedule
	 * @param target
	 * @param distribution
	 */
	public RepetitiveAction(RepastScheduler schedule,Runnable target,Stream<Long> distribution) {
		this(schedule,target,distribution,0,0.0);
	}
	               
     
     /**
      * Execute's the action
      *
      */
     public void doExecute() {
    	 
    	 
    	 //call the target
    	 target.run();
    	 

    	 //calculate the interval until next execution
    	 interval = (double)frequency.getValue();

    	 //check if must continue executin
    	 iterations++;
    	 if((maxIterations != 0) && (iterations == maxIterations)) {
    		 scheduler.cancelAction(this);
    	 }

    	 //check if next invocation would pass execution limit
    	 if((endTime != 0) && ((scheduler.getTime()+interval) >= endTime)){
    		 scheduler.cancelAction(this);
    		 
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
    	 return scheduler.getTime()+interval;
     }
     
}

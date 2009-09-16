package edu.upc.cnds.collectivesim.scheduler.base;

import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.stream.Stream;

public class RepetitiveAction extends AbstractScheduledAction {
	
	
	private static Logger log = Logger.getLogger("collectivesim.scheduler.base");
				
	/**
	 * Delay between executions of the action
	 */
	private Enumeration<Long> frequency;
	
	private long nextTime;

	private Long endTime;
	
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
	public RepetitiveAction(BasicScheduler scheduler,Runnable target,Long startTime,Enumeration<Long> frequency,long maxIterations,Long endTime) {
		super(scheduler,target);
		this.target = target;
        this.frequency = frequency;
        this.iterations = 0;
        this.maxIterations = maxIterations;
        this.endTime = endTime;	   
        
   	 	//calculate the interval until next execution
        nextTime = startTime;
   	 
	}
	
	/**
	 * Convenience constructor without either iterations or time limits
	 * @param schedule
	 * @param target
	 * @param frequency
	 */
	public RepetitiveAction(BasicScheduler scheduler,Runnable target,Long startTime,Enumeration<Long> frequency) {
		this(scheduler,target,startTime,frequency,(long)0,(long)0);
	}
	               
     
     /**
      * Execute's the action
      *
      */
     public void doExecute() {
    	 
    	 
    	 //call the target
    	 target.run();
    	 

    	 //check if must continue execution
    	 iterations++;
    	 if((maxIterations != 0) && (iterations == maxIterations)) {
    		 return;
    	 }

    	 //calculate the interval until next execution
    	 long interval = frequency.nextElement();
    	 
    	 //a 0 interval is not allowed and terminates the repetitive action
    	 if(interval == 0){
    		 return;
    	 }
    	 
    	 nextTime = scheduler.getTime() + interval;

    	 //check if next invocation would pass execution limit
    	 if((endTime != 0) && (nextTime > endTime)){
    	  return;
    	 }

    	 //rescheduler the action
		 ((BasicScheduler)scheduler).addAction(this);
    	 
     }


	@Override
	public long getExecutionTime() {
		return nextTime;
	}
            
     
     
}

package edu.upc.cnds.collectivesim.scheduler;

import edu.upc.cnds.collectivesim.model.Stream;


/**
 * Offers an interface to simulation engine to schedule actina and to access 
 * parameters and {@link Stream}s of values for simulation variables. 
 * 
 * @author Pablo Chacin
 *
 */
public interface Scheduler {



	/**
	 * Returns the current simulation time
	 * 
	 * @return
	 */
	public abstract Double getTime();

	/**
	 * Schedules the execution of the target's run method at after a certain delay.
	 *
	 * @param target an Runnable object on which the action will be executed
	 * @param delay time between executions
	 *
	 */
	 public ScheduledAction scheduleAction(Runnable target, long delay);
	 

	/**
	 * Schedules a repetitive task until the given time. The last execution is guaratee to be not greater
	 * that the given time.
	 * 
	 * @param target a Runnable to execute
	 * @param iterations maximum number of executions
	 * @param frequency a Stream with the inter-execution times
	 * @param delay the earlier time this action can be executed 
	 * @param endTime the maximun time this action can be executed
	 * @return
	 */
	public abstract ScheduledAction scheduleRepetitiveAction(Runnable target, int iterations,Stream<Long> frequency,long delay, long endTime);
	
	 
	/** 
	 * Convenience method. Schedules an action to be executed immediately with a certain frequency and
	 * neither an end time nor a limit of executions.  
	 * @param target
	 * @param frequency
	 */
	public abstract ScheduledAction scheduleRepetitiveAction(Runnable target, Stream<Long> frequency);
	
	/**
	 * Stops the scheduling of actions. Pending actions remains in the execution queue.
	 */
	public void pause();
	
	/**
	 * Finalizes the scheduling of tasks and frees any resource. All pending actions
	 * are deleted
	 */
	public void reset();

	/**
	 * Continues the scheduling of tasks
	 */
	public void resume();
	

	public long getSpeed();
	
	/**
	 * Sets the execution speed. Execution speed defines the relationship between
	 * simulation time and real clock time. 
	 * @param speed
	 */
	public void setSpeed(long speed);
}
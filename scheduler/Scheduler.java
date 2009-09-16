package edu.upc.cnds.collectivesim.scheduler;

import java.util.Enumeration;

import edu.upc.cnds.collectivesim.stream.Stream;


/**
 * Offers an interface to simulation engine to schedule actina and to access 
 * parameters and {@link Stream}s of values for simulation variables. 
 * 
 * @author Pablo Chacin
 *
 */
public interface Scheduler {

	/**
	 * gets the finalization time
	 * 
	 * @return
	 */
	public long getEndTime();
	
	/**
	 * Returns the current simulation time
	 * 
	 * @return
	 */
	public abstract long getTime();

	 
	/**
	 * Schedules a repetitive task at a varying frequency until the given time. 
	 * The last execution is guarantee to be not greater that the given time.	 * 
	 * @param target a Runnable to execute
	 * @param iterations maximum number of executions
	 * @param frequency an Enumeration with the frequency (execution interval)
	 * @param delay the earlier time this action can be executed 
	 * @param endTime the maximum time this action can be executed
	 * @return
	 */
	public abstract ScheduledAction scheduleAction(Runnable target, int iterations,Enumeration<Long> frequency,long delay, long endTime);
	
	 
	/**
	 * Schedules a repetitive task at fixed frequency until the given time. 
	 * The last execution is guarantee to be not greater that the given time.
	 * 
	 * @param target a Runnable to execute
	 * @param iterations maximum number of executions
	 * @param frequency an Enumeration with the frequency (execution interval)
	 * @param delay the earlier time this action can be executed 
	 * @param endTime the maximum time this action can be executed
	 * @return
	 */
	public abstract ScheduledAction scheduleAction(Runnable target, int iterations,Long frequency,long delay, long endTime);
	

	/**
	 * Starts the scheduling of tasks with an undefined duration
	 */
	public void start();
	
	/**
	 * start scheduling tasks with a given maximum duration
	 * 
	 * @param endTime
	 */
	public void start(long endTime);
	
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
	

	/**
	 * 
	 * @return the relative execution speed with respect of real time execution
	 */
	public long getSpeed();
	
	/**
	 * Sets the execution speed. Execution speed defines the relationship between
	 * simulation time and real clock time. 
	 * @param speed
	 */
	public void setSpeed(long speed);
	
	/**
	 * Sets a tasks to be executed when the scheduler finalizes
	 * @param task
	 */
	public void setTerminationHandler(Runnable task);
}
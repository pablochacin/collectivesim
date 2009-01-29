package edu.upc.cnds.collectivesim.scheduler;


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
	 * Schedules the execution of the target's run method on a repetitive way
	 * following a given distribution in the time interval between successive executions.
	 * 
	 * @param target
	 * @param distribution
	 */
	public abstract ScheduledAction scheduleRepetitiveAction(Runnable target, Stream<Long> distribution);
	
	/**
	 * Schedules a repetitive task until the given time. The last execution is guaratee to be not greater
	 * that the given time.
	 * 
	 * @param target
	 * @param distribution
	 * @param endTime
	 * @return
	 */
	public abstract ScheduledAction scheduleRepetitiveAction(Runnable target, Stream<Long> distribution,long endTime);
	
	/**
	 * Schedules a repetitive task the given number of times.
	 * 
	 * @param target
	 * @param distribution
	 * @param iterations
	 * @return
	 */
	public abstract ScheduledAction scheduleRepetitiveAction(Runnable target, Stream<Long> distribution,int iterations);

	/**
	 * schedule an action until either the endtime or a maximum number of iterations are reached.
	 * @param target
	 * @param distribution
	 * @param iterations
	 * @param endTime
	 * @return
	 */
	public abstract ScheduledAction scheduleRepetitiveAction(Runnable target, Stream<Long> distribution,int iterations,long endTime);

	/**
	 * Start the scheduling of tasks
	 * 
	 * @throws IllegalStateException if the scheduler has already been started
	 */
	public void start();

	/**
	 * Stops the scheduling of actions. Pending actions remains in the execution queue.
	 */
	public void pause();
	
	/**
	 * Finalizes the scheduling of tasks and frees any resource. All pending actions
	 * are deleted
	 */
	public void stop();

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
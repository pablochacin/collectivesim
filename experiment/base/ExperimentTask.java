package edu.upc.cnds.collectivesim.experiment.base;

import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.stream.base.FixedValueStream;

/**
 * An Experiment related task planned for execution in the Scheduler.
 * 
 * This class is used to maintain a list of the tasks that must be scheduled
 * for each experiment run, such as observers, the start of models, and others.
 * 
 * @author Pablo Chacin
 *
 */
public class ExperimentTask {

	private Runnable task;
	
	private long delay;
	
	private long frequency;
	
	private int priority;
	
	private Scheduler scheduler;
	
	/**
	 * Task that is executed repetitively and has a (optional) delay for the first execution.
	 * 
	 * @param scheduler
	 * @param task
	 * @param delay
	 * @param frequency
	 * @param priority
	 */
	public ExperimentTask(Scheduler scheduler,Runnable task, long delay,long frequency,int priority) {
		super();
		this.scheduler = scheduler;
		this.task = task;
		this.delay = delay;
		this.frequency = frequency;
		this.priority = priority;
	}

	/**
	 * Task that is planned for a given time and is executed just once.
	 * @param scheduler
	 * @param task
	 * @param delay
	 */
	public ExperimentTask(Scheduler scheduler,Runnable task, long delay) {
		this(scheduler,task,delay,0,0);
	}


	
	public Runnable getTask() {
		return task;
	}

	public long getDelay() {
		return delay;
	}
	
	public long getFrequency(){
		return frequency;
	}

	
	/**
	 * Schedule the task in the associated scheduler.
	 */
	public void schedule(){
			scheduler.scheduleAction(task, 0, frequency, delay, 0,priority);
	}
}

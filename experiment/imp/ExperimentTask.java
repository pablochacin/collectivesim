package edu.upc.cnds.collectivesim.experiment.imp;

import edu.upc.cnds.collectivesim.model.SingleValueStream;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;

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
	
	private Scheduler scheduler;
	
	/**
	 * Task that is executed repetitively and has a (optional) delay for the first execution.
	 * 
	 * @param scheduler
	 * @param task
	 * @param delay
	 * @param frequency
	 */
	public ExperimentTask(Scheduler scheduler,Runnable task, long delay,long frequency) {
		super();
		this.scheduler = scheduler;
		this.task = task;
		this.delay = delay;
		this.frequency = frequency;
	}

	/**
	 * Task that is planned for a given time and is executed just once.
	 * @param scheduler
	 * @param task
	 * @param delay
	 */
	public ExperimentTask(Scheduler scheduler,Runnable task, long delay) {
		this(scheduler,task,delay,0);
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
		if(frequency == 0){
			scheduler.scheduleAction(task, delay);
		}
		else{
			scheduler.scheduleRepetitiveAction(task, 0, new SingleValueStream<Long>("",frequency), delay, 0);
		}
	}
}

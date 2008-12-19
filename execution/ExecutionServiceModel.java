package edu.upc.cnds.collectivesim.execution;

import edu.upc.cnds.collectives.execution.ExecutionService;
import edu.upc.cnds.collectives.execution.Task;
import edu.upc.cnds.collectivesim.scheduler.ScheduledAction;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;

public class ExecutionServiceModel implements ExecutionService {

	private Scheduler scheduler;
	
	
	public ExecutionServiceModel(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void doWait(long time) {
		throw new UnsupportedOperationException();

	}

	public Task execute(Runnable task) {
		ScheduledAction action = scheduler.scheduleAction(task, 0);
		return new TaskModel(action);
	}

	
	public Task scheduleRepetitiveTask(Runnable task, long delay, long period) {
		ScheduledAction action = scheduler.scheduleRepetitiveAction(task, period);
		return new TaskModel(action);
	}

	public Task scheduleTask(Runnable task, long delay) {
		throw new UnsupportedOperationException();
	}


}

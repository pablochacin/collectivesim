package edu.upc.cnds.collectivesim.execution;

import edu.upc.cnds.collectives.execution.ExecutionService;
import edu.upc.cnds.collectives.execution.Task;
import edu.upc.cnds.collectivesim.model.SingleValueStream;
import edu.upc.cnds.collectivesim.model.Stream;
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
		return new ExecutionServiceModelTask(action);
	}

	
	public Task scheduleRepetitiveTask(Runnable task, long delay, long period) {
		Stream periodStream = new SingleValueStream("",new Double(period));
		
		ScheduledAction action = scheduler.scheduleRepetitiveAction(task, periodStream);
		return new ExecutionServiceModelTask(action);
	}

	public Task scheduleTask(Runnable task, long delay) {
		throw new UnsupportedOperationException();
	}


}

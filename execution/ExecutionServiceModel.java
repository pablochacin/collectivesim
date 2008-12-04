package edu.upc.cnds.collectivesim.execution;

import edu.upc.cnds.collectives.execution.ExecutionService;
import edu.upc.cnds.collectives.execution.Task;
import edu.upc.cnds.collectivesim.models.ScheduledAction;
import edu.upc.cnds.collectivesim.models.SimulationModel;

public class ExecutionServiceModel implements ExecutionService {

	private SimulationModel model;
	
	
	public ExecutionServiceModel(SimulationModel model) {
		this.model = model;
	}
	
	public void doWait(long time) {
		throw new UnsupportedOperationException();

	}

	public Task execute(Runnable task) {
		ScheduledAction action = model.scheduleAction(task, 0);
		return new TaskModel(action);
	}

	
	public Task scheduleRepetitiveTask(Runnable task, long delay, long period) {
		ScheduledAction action = model.scheduleRepetitiveAction(task, period);
		return new TaskModel(action);
	}

	public Task scheduleTask(Runnable task, long delay) {
		throw new UnsupportedOperationException();
	}


}

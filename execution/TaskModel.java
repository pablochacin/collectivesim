package edu.upc.cnds.collectivesim.execution;

import edu.upc.cnds.collectives.execution.Task;
import edu.upc.cnds.collectivesim.scheduler.ScheduledAction;

/**
 * Implements an ExecutionService's task using the SimulationModel.
 *  
 * @author Pablo Chacin
 *
 */
public class TaskModel implements Task {

	/**
	 * The model's scheduled actino associated with this task
	 */
	ScheduledAction action;
	
	public TaskModel(ScheduledAction action) {
		this.action = action;
	}

	public void cancel() {
		action.cancel();
	}

	public long getDueTime() {
		throw new UnsupportedOperationException();
	}

	public Runnable getRunnable() {
		throw new UnsupportedOperationException();
	}

	public boolean isExpired() {
		throw new UnsupportedOperationException();
	}

}

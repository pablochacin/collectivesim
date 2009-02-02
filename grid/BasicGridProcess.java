package edu.upc.cnds.collectivesim.grid;

import edu.upc.cnds.collectiveg.GridLRM;
import edu.upc.cnds.collectiveg.GridProcess;
import edu.upc.cnds.collectiveg.GridTask;

public class BasicGridProcess implements GridProcess {

	private ExecutionState state;
	
	private long endTime;
	
	private long startTime;
		
	private long executionTime;
	
	private GridTask task;
	
	private GridLrmAgent lrm;
	
	public BasicGridProcess(GridLrmAgent lrm,GridTask task){
		this.task = task;
		this.startTime = lrm.getCurrentTime();
		this.endTime = 0;
		this.executionTime = 0;
		this.state = ExecutionState.RUNNING;
	}


	@Override
	public long endTime() {
		return endTime;
	}

	@Override
	public GridTask getGridTask() {
		return task;
	}

	@Override
	public long startTime() {
		return startTime;
	}

	public void complete(){
		this.state = ExecutionState.COMPLETED;
		this.endTime = lrm.getCurrentTime();
	}
	
	public void suspend(){
		if(!state.equals(ExecutionState.RUNNING)){
			throw new IllegalStateException();
		}
		
		state = ExecutionState.SUSPENDED;
	}
	
	public void resume(){
		if(!state.equals(ExecutionState.SUSPENDED)){
			throw new IllegalStateException();
		}
		
		state = ExecutionState.RUNNING;
		
	}
	
	/**
	 * Accounts for some execution time. Used to update the total execution time.
	 *  
	 * @param time
	 */
	public void accountExecutionTime(long time){
		if(state.equals(ExecutionState.COMPLETED)){
			throw new IllegalStateException();
		}
		
		executionTime += time;
		if(executionTime >= task.getDuration()){
			state = ExecutionState.COMPLETED;
		}
	}


	@Override
	public long getExecutionTime() {
		return executionTime;
	}


	@Override
	public ExecutionState getState() {
		return state;
	}
}

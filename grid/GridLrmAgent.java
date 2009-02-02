package edu.upc.cnds.collectivesim.grid;

import java.util.Iterator;

import edu.upc.cnds.collectiveg.GridProcess;
import edu.upc.cnds.collectiveg.GridResource;
import edu.upc.cnds.collectiveg.GridTask;
import edu.upc.cnds.collectiveg.InvalidResourceEspecification;
import edu.upc.cnds.collectiveg.GridProcess.ExecutionState;
import edu.upc.cnds.collectiveg.baseImp.BasicGridLRM;
import edu.upc.cnds.collectivesim.scheduler.Stream;


public class GridLrmAgent extends BasicGridLRM {

	private GridLrmModel model;
	
	private long lastUpdate;
	
	private Stream<Double>backgroundLoad;
	
	public GridLrmAgent(GridLrmModel model,String name, GridResource resource, Stream<Double>backgroundLoad,POLICY policy) throws InvalidResourceEspecification {
		super(name, resource, policy);
		this.model = model;
		this.backgroundLoad  = backgroundLoad;
		lastUpdate = model.getCurrentTime();
	}


	public long getCurrentTime() {
		return model.getCurrentTime();
	}


	/**
	 * Submits a task to the resource
	 * @param task
	 */
	public GridProcess sumbit(GridTask task){

		BasicGridProcess process = new BasicGridProcess(this,task);
		processQueue.add(process);
		return process;
	}

	
	/**
	 * Updates the execution state of the process
	 * 
	 */
	public void updateProcesses(){

		if(processQueue.size() == 0){
			return;
		}
		
		//depending on the policy, assign execution time to all or just the first job
		//in the queue
		int limit;
		if(policy.equals(POLICY.FCFS)){
			limit= 1;
		}
		else{
			limit = processQueue.size();
			
		}
		
		//calculate the execution slice for each job in the queue
		//based on the time they have been executing, the speed of the resource
		//the background load of the system (jobs not managed by the LRM) and the 
		//number of jobs being served
		long executionTime = getCurrentTime()-lastUpdate;
		double cpuRate = resource.getSpeed()*(1-getBackgroundLoad());		
		long slice =(long)((executionTime/limit)*cpuRate);

		//update execution time		
		Iterator<GridProcess> i  = processQueue.subList(0, limit).iterator();
		
		while(i.hasNext()){
			BasicGridProcess p = (BasicGridProcess)i.next();
			p.accountExecutionTime(slice);
			if(p.getState().equals(ExecutionState.COMPLETED)){
				i.remove();
			}
		}

	}

	protected double getBackgroundLoad(){
		return backgroundLoad.getValue();
	}
	
	@Override
	public Double getCurrentLoad() {
		throw new UnsupportedOperationException();
	}


	@Override
	public Double getServiceRate() {
		throw new UnsupportedOperationException();	
	}

	
	
}

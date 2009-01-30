package edu.upc.cnds.collectivesim.grid;

import edu.upc.cnds.collectiveg.GridLRM;
import edu.upc.cnds.collectiveg.GridTask;
import edu.upc.cnds.collectivesim.model.imp.ReflexionModelAgent;
import edu.upc.cnds.collectivesim.scheduler.Stream;

public class GridWorkloadAgent extends ReflexionModelAgent implements Runnable, Stream<Long>{
	
	private GridWorkload workload;
	
	private GridLRM lrm;
	
	private GridWorkLoadModel model;
	
	private GridTask nextArrival;
	
	public GridWorkloadAgent(GridWorkLoadModel model, GridLRM lrm,GridWorkload workload) {
		this.model = model;
		this.lrm = lrm;
		this.workload = workload;
		this.nextArrival = workload.getNextArrival();
	}
	
	public Stream<Long> arrivalTimeStream(){
		return this;
	}
	
	public void run(){
		lrm.sumbit(workload.getNextArrival());
		nextArrival =  workload.getNextArrival();
	}

	public String getName(){
		return workload.getName();
	}
	/**
	 * Returns the next arrival time for the workload.
	 */
	public Long getValue() {
		return nextArrival.getTimeSumitted();
	}
	
}
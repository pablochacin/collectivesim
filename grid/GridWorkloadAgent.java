package edu.upc.cnds.collectivesim.grid;

import edu.upc.cnds.collectiveg.GridResourceBroker;
import edu.upc.cnds.collectiveg.GridTask;
import edu.upc.cnds.collectivesim.model.imp.ReflexionModelAgent;
import edu.upc.cnds.collectivesim.scheduler.Stream;

public class GridWorkloadAgent extends ReflexionModelAgent implements Runnable, Stream<Long>{
	
	private GridWorkload workload;
	
	private GridResourceBroker broker;
	
	private GridWorkLoadModel model;
	
	private GridTask nextArrival;
	
	public GridWorkloadAgent(GridWorkLoadModel model, GridResourceBroker broker,GridWorkload workload) {
		this.model = model;
		this.broker = broker;
		this.workload = workload;
		this.nextArrival = workload.getNextArrival();
	}
	
	public Stream<Long> arrivalTimeStream(){
		return this;
	}
	
	/**
	 * Process the next arrival, submitting it to the Broker and
	 * preparing the following arrival.
	 */
	public void run(){
		broker.sumbit(workload.getNextArrival());
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
package edu.upc.cnds.collectivesim.grid;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectiveg.GridLRM;
import edu.upc.cnds.collectiveg.GridProcess;
import edu.upc.cnds.collectiveg.GridResource;
import edu.upc.cnds.collectiveg.InvalidResourceEspecification;
import edu.upc.cnds.collectiveg.GridLRM.POLICY;
import edu.upc.cnds.collectives.dataseries.DataSequence;
import edu.upc.cnds.collectives.dataseries.DataSeries;
import edu.upc.cnds.collectives.dataseries.InvalidDataItemException;
import edu.upc.cnds.collectives.dataseries.baseImp.BaseDataSeries;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.imp.AbstractModel;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.scheduler.Stream;

public class GridLrmModel extends AbstractModel {

	/**
	 * Frequency of updates for LRMs
	 */
	private long timeSlice;
	
	private DataSeries executions;
	/**
	 * 
	 * @param scheduler
	 * @param timeSlice
	 */
	public GridLrmModel(Scheduler scheduler,DataSeries executions,long timeSlice) {
		super(scheduler);
		this.timeSlice = timeSlice;
		this.executions = executions;		
		super.addBehavior("update", "updateProcesses", true, timeSlice);
	}

	/**
	 * Convenience Method, creates a GridLrmModel using a default DataSeries for
	 * executions.
	 * 
	 * @param scheduler
	 * @param timeSlice
	 */
	public GridLrmModel(Scheduler scheduler,long timeSlice){
		this(scheduler,new BaseDataSeries("GridLrmModel-Executions",0),timeSlice);
	}
	
	/**
	 * 
	 * @param name
	 * @param resource
	 * @param backgroundLoad
	 * @param policy
	 * @throws ModelException
	 */
	public GridLRM addLRM(String name,GridResource resource,Stream<Double> backgroundLoad,POLICY policy) throws ModelException{
		
		GridLrmAgent agent;
		try {
			agent = new GridLrmAgent(this,name,resource,backgroundLoad,policy);
			super.addAgent(agent);

			return agent;
		} catch (InvalidResourceEspecification e) {
			throw new ModelException(e);
		}
	
		

		
	}

	/**
	 * Informs that a process has ended. Current implementation only reports
	 * the execution time
	 * 
	 * @param process
	 * @param agent
	 */
	void reportProcessEnded(GridProcess process,GridLrmAgent agent){
		Map attributes = getAttributes(process);
		try {
			long executionTime = process.endTime()-process.startTime();
			executions.addItem(attributes,(double)executionTime);
		} catch (InvalidDataItemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the attributes for a metric associated with the process.
	 * Subclasses can override it to provide other attributes for a
	 * metric.
	 * 
	 * @param process
	 * @return
	 */
	protected Map getAttributes(GridProcess process)  {
		Map attributes = new HashMap();
		attributes.put("user", process.getGridTask().getUser());
		attributes.put("lrm", process.getLRM());
		attributes.put("submitted",process.getGridTask().getTimeSumitted());
		
		return attributes;
	}
	
	
	/**
	 * 
	 * @return a DataSeries with the execution time of jobs ended in all LRMs of this
	 *         model.
	 */
	public DataSeries getExecutions(){
		return executions;
	}
}
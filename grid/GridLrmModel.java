package edu.upc.cnds.collectivesim.grid;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectiveg.GridLRM;
import edu.upc.cnds.collectiveg.GridProcess;
import edu.upc.cnds.collectiveg.GridProcessEvent;
import edu.upc.cnds.collectiveg.GridResource;
import edu.upc.cnds.collectiveg.InvalidResourceEspecification;
import edu.upc.cnds.collectiveg.GridLRM.POLICY;
import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.base.BasicModel;
import edu.upc.cnds.collectivesim.stream.Stream;

public class GridLrmModel extends BasicModel {

	/**
	 * Frequency of updates for LRMs
	 */
	private long timeSlice;
	
	private DataSeries executions;
	/**
	 * 
	 * @param experiment
	 * @param timeSlice
	 */
	public GridLrmModel(String name,Experiment experiment,DataSeries executions,long timeSlice) {
		super(name,experiment);
		this.timeSlice = timeSlice;
		this.executions = executions;		
		super.addBehavior("update", "updateProcesses", timeSlice);
	}

	
	/**
	 * 
	 * @param name
	 * @param resource
	 * @param backgroundLoad
	 * @param policy
	 * @throws ModelException
	 */
	public GridLRM addLRM(Node node,String name,GridResource resource,Stream<Double> backgroundLoad,POLICY policy) throws ModelException{
		
		GridLrmAgent agent;
		try {
			agent = new GridLrmAgent(node,this,name,resource,backgroundLoad,policy);
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
	void reportProcessEnded(GridLrmAgent agent,GridProcess process){
		Map attributes = getAttributes(process);
		//set default attributes
		attributes.put("lrm", process.getLRM());
		attributes.put("user", process.getGridTask().getUser());
		attributes.put("submited",String.valueOf(process.getGridTask().getTimeSumitted()));
		attributes.put("cpu", String.valueOf(process.getCpuTime()));
		attributes.put("execution", String.valueOf(process.getExecutionTime()));
		attributes.put("start",String.valueOf(process.startTime()));
		attributes.put("end",String.valueOf(process.endTime()));
		long elapsed = process.endTime() - process.getGridTask().getTimeSumitted();
		attributes.put("elapsed", String.valueOf(elapsed));
		
		Event event = new GridProcessEvent(agent.getNode(),process,attributes);
		
		experiment.reportEvent(event);
		
		executions.addItem(attributes,new Double(process.getExecutionTime()));
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
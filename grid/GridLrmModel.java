package edu.upc.cnds.collectivesim.grid;

import edu.upc.cnds.collectiveg.GridLRM;
import edu.upc.cnds.collectiveg.GridResource;
import edu.upc.cnds.collectiveg.InvalidResourceEspecification;
import edu.upc.cnds.collectiveg.baseImp.BasicGridLRM.POLICY;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.imp.AbstractModel;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.scheduler.Stream;

public class GridLrmModel extends AbstractModel {

	/**
	 * Frequency of updates for LRMs
	 */
	private long timeSlice;
	
	/**
	 * 
	 * @param scheduler
	 * @param timeSlice
	 */
	public GridLrmModel(Scheduler scheduler,long timeSlice) {
		super(scheduler);
		this.timeSlice = timeSlice;

		super.addBehavior("update", "updateProcesses", true, timeSlice);
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
		} catch (InvalidResourceEspecification e) {
			throw new ModelException(e);
		}
	
		super.addAgent(agent);
		
		return agent;
		
	}

}
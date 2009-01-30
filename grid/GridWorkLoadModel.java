package edu.upc.cnds.collectivesim.grid;

import edu.upc.cnds.collectiveg.GridLRM;
import edu.upc.cnds.collectivesim.model.imp.AbstractModel;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;

/**
 * Models the workload of a grid.
 * 
 * @author Pablo Chacin
 *
 */
public class GridWorkLoadModel extends AbstractModel {

	
	public GridWorkLoadModel(Scheduler scheduler) {
		super(scheduler);
	}

	
	/**
	 * Adds a new workload to the grid. The workload is associated to a LRM
	 * to which the tasks are sumitted.
	 * 
	 * @param lrm a GridLRM to which tasks are submitted
	 * @param workload a {@link GridWorkload} that generates the tasks
	 */
	
	public void addWorkLoad(GridLRM lrm,GridWorkload workload){
		GridWorkloadAgent agent = new GridWorkloadAgent(this,lrm,workload);
		
		scheduler.scheduleRepetitiveAction(agent, agent.arrivalTimeStream());
		
		super.addAgent(agent);
	}

	
}

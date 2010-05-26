package edu.upc.cnds.collectivesim.grid;

import edu.upc.cnds.collectiveg.GridResourceBroker;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.base.BasicModel;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;

/**
 * Models the workload of a grid.
 * 
 * @author Pablo Chacin
 *
 */
public class GridWorkLoadModel extends BasicModel {

	
	public GridWorkLoadModel(String name,Experiment experiment) {
		super(name,experiment);
	}

	
	/**
	 * Adds a new workload to the grid. The workload is associated to a LRM
	 * to which the tasks are sumitted.
	 * 
	 * @param broker a GridResourceBrojer to which tasks are submitted
	 * @param workload a {@link GridWorkload} that generates the tasks
	 */
	
	public void addWorkLoad(GridResourceBroker broker,GridWorkload workload){
		GridWorkloadAgent agent = new GridWorkloadAgent(this,broker,workload);
		
		scheduler.scheduleRepetitiveAction(agent, agent.arrivalTimeStream());
		
		super.addAgent(agent);
	}

	
}

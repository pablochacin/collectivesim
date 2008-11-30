package edu.upc.cnds.collectivesim.models;

import edu.upc.cnds.collectivesim.models.imp.ScheduledAction;
import edu.upc.cnds.collectivesim.models.imp.SingleAction;
import edu.upc.cnds.collectivesim.views.View;
import edu.upc.cnds.collectives.util.TypedMap;

/**
 * Offers an interface to simulation engine to schedule actina and to access 
 * parameters and {@link Stream}s of values for simulation variables. 
 * 
 * @author Pablo Chacin
 *
 */
public interface SimulationModel {



	/**
	 * @return the arguments passed to the Simulation
	 */
	public TypedMap getArgument(); 

	/**
	 * Returns the current simulation time
	 * 
	 * @return
	 */
	public abstract Double getTime();

	/**
	 * Schedules the execution of the target's run method at after a certain delay.
	 *
	 * @param target an Runnable object on which the action will be executed
	 * @param delay time between executions
	 *
	 */
	 public ScheduledAction scheduleAction(Runnable target, long delay);
	 
	 
	/** 
	 * Schedules the execution of the target's run method on a repetitive way
	 * following a given distribution in the time interval between sucessive executions.
	 * 
	 * @param target
	 * @param distribution
	 */
	public abstract ScheduledAction scheduleAction(Runnable target, Stream distribution);
	

}
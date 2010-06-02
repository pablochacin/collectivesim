package edu.upc.cnds.collectivesim.scheduler;

/**
 * An action scheduled for execution in the SimulationModel
 * 
 * TODO: extend the interface ScheduleTask to give access to the
 *       needed information, like the due time, the executable, and
 *       the execution status.
 * 
 * @author Pablo Chacin
 *
 */
public interface ScheduledAction {

	/**
	 * Cancel this action
	 */
	public void cancel();
	

}
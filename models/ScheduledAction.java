package edu.upc.cnds.collectivesim.models;

/**
 * An action scheduled for exection in the SimulationModel
 * 
 * TODO: extend the interface ScheduleTask to give access to the
 *       needed information, like the due time, the executable, and
 *       the exection status.
 * 
 * @author Pablo Chacin
 *
 */
public interface ScheduledAction {

	public void cancel();

}
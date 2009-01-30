package edu.upc.cnds.collectivesim.grid;

import edu.upc.cnds.collectiveg.GridTask;

/**
 * Generates a stream of {@link GridTask} to be submitted to a grid. 
 * May be used to represent a user,  a user community, an application or a single multi-task job.
 * 
 * It has a distribution of request is terms of attributes like:
 * <ul>
 * <li> frequency of arrivals
 * <li> cpu demand
 * <li> hardware/software requirements (CPU type, OS type)
 * </ul>
 * 
 * This base class requires that subclasses implements the logic to calculate the time of the next 
 * arrival (simultaneous arrival are not allowed) and to retrieve the corresponding {@link GridTask}.
 * The time of the next arrival is used to schedule the time when the task will be requested to be
 * submitted.
 * 
 * 
 * @author Pablo Chacin
 *
 */
public interface GridWorkload {
			
	/**
	 * @return a name that describes the workload (e.g. "night batch")
	 */
	public String getName();
	
	/**
	 * Retrieves the definition of the next task to be submitted.
	 * 
	 * @return
	 */
	public GridTask getNextArrival();
	

	
}

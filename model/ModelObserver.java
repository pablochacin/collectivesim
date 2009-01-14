package edu.upc.cnds.collectivesim.model;

import java.util.Vector;

/**
 * Allows the observation of a model, receiving updates or values from model's agents
 * 
 * @author Pablo Chacin
 *
 */
public interface ModelObserver {

	
	/**
	 * Informs the update of model's values
	 * @param model the Model being observed
	 * @param name a String with the name of the observer as defined in the AddObserver method
	 * @param values values from model's agents
	 */
	public void updateValues(Model model,String name,Vector<Object>values);
}

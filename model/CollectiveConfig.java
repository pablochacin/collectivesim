package edu.upc.cnds.collectivesim.model;

import edu.upc.cnds.collectives.protocol.Protocol;

/**
 * Allows the configuration of the Collective
 * 
 * @author Pablo Chacin
 *
 */
public interface CollectiveConfig {

	/**
	 * Register an action and associates it with a protocol
	 * 
	 * @param action name of the action
	 * @param protocol protocol used to propagate the request for the action
	 * 
	 */
	public void registerAction(String action, Protocol protocol);
	
	/**
	 * 
	 * @param attribute name of the attribute
	 * @param protocol Protocol used to inquire the attribute
	 */
	public void registerAttribute(String attribute, Protocol protocol);
}

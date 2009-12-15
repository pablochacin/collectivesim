package edu.upc.cnds.collectivesim.underlay.Space2D;

import edu.upc.cnds.collectives.underlay.UnderlayNode;

public interface SpaceLocationStrategy {

	/**
	 * Returns a location to be assigned to a new node in the topology
	 * 
	 * @param topology
	 * @return
	 */
	Space2DLocation  getLocation(Space2DTopology topology,UnderlayNode node);
	
}

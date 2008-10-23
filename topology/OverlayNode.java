package edu.upc.cnds.collectivesim.topology;

import collectivesim.nodes.Node;
import collectivesim.nodes.NodeView;


/**
 * Represents the location of an agent in a Topology 
 * 
 * @author Pablo Chacin
 *
 */
public interface OverlayNode extends Node{

	
	
	/**
	 * Returns a vector with the neighbor locations
	 * 
	 * @return
	 */
	public NodeView getNeighbors();
		    
	
	
}


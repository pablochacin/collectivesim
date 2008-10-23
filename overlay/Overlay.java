package edu.upc.cnds.collectivesim.overlay;

import java.util.List;

import edu.upc.cnds.collectives.node.Node;

/**
 * Mantains a logical organization of Nodes.
 * 
 * @author 
 *
 */
public interface Overlay {

	
	/**
	 * @return a List of Nodes currently in the overlay
	 * 
	 */
	public List<Node> getNodes();
	
	
	/**
	 * Returns the neigbors of a node.
	 * 
	 * @param node not for which the neighbor is requested
	 * @return
	 */
	public List<Node> getNeighbors(Node node);
}

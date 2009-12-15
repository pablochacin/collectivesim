package edu.upc.cnds.collectivesim.underlay;

import java.util.Collection;
import java.util.List;

import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.underlay.Grid2D.UnderlayModelException;

/**
 * Maintains the topology of a network of nodes
 * 
 * @author Pablo Chacin
 *
 */
public interface NetworkTopology {

	/**
	 * Adds a node to the topology and defines its list of known nodes (see {@link #getKnownNodes(UnderlayNode)})
	 * If the topology is bidirectional, for each of those nodes, adds the new node in their list of known nodes.
	 * 
	 * @param node
	 * @throws UnderlayModelException 
	 */
	public void addNode(UnderlayNode node) throws UnderlayModelException;
	
	/**
	 * Removes a node from the topology. Removes it from the list of known nodes of other nodes as necessary.
	 * @param id
	 */
	public void removeNode(UnderlayNode id);
	
	/**
	 * Returns the nodes which are considered "neighbors" of a node, according to the topology.
	 * 
	 * @param node
	 * 
	 * @return
	 */
	public List<UnderlayNode> getKnownNodes(UnderlayNode node);
	
	/**
	 * Generates a topology from a list of Nodes.
	 * 
	 * @param nodes
	 */
	public void generateTopology(List<? extends UnderlayNode> nodes) throws UnderlayModelException;
	
}

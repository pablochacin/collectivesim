package edu.upc.cnds.collectivesim.topology;

/**
 * Represents the logical organization of a set of Nodes.
 * 
 * @author Pablo Chacin
 *
 */
public interface Topology {

	/**
	 * Adds a node to the topology
	 * 
	 * @param node the Node to be added to the topology
	 * 
	 */
	public void addNode(Node node);
	
	
	/**
	 * Builds a logical organization for the nodes currently in the
	 * topology.
	 * 
	 */
	public void build();
}

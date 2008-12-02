package edu.upc.cnds.collectivesim.topology;

import java.util.List;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.topology.Topology;

/**
 * 
 * Builds and mantains a topology of nodes, generating the local topology view for
 * each node. 
 * 
 * @author Pablo Chacin
 *
 */
public interface TopologyGenerator {
	
	/**
	 * Adds the node to the topology
	 * 
	 * @param node a Node that will form part of the topology
	 * 
	 * @throws TopologyGeneratorException if the node cannot be added to
	 *         the topology (e.g. lack of space)
	 */
	public void addNode(Node node) throws TopologyGeneratorException;
		
	/**
	 * Generates the local view of the topology for a particular node
	 * @param node Node to
	 * @return the local view of the topology for a node
	 * 
	 * @throws TopologyGeneratorException if the node's local topology view couldn't be 
	 *         generated (for example, the node is not currently part of the topology)
	 */
	public Topology getTopology(Node node) throws TopologyGeneratorException;
	
	/**
	 * 
	 * @return a list of the nodes currently in the topology
	 */
	public List<Node> getNodes();
	
	/**
	 * Removes a node from the topology. All other nodes' local views
	 * are updated
	 * 
	 * @param node to remove
	 * 
	 * @throws TopologyGeneratorException if the node couldn't be removed (for instance
	 *         if it currently doesn't belongs to the topology)
	 */
	public void removeNode(Node node) throws TopologyGeneratorException;
}

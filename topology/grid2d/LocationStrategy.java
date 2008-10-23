package edu.upc.cnds.collectivesim.topology.grid2d;

import java.util.Vector;

import edu.upc.cnds.collectivesim.topology.Topology;
/**
 * 
 * Represents a strategy to position nodes in a Topology.
 * 
 * 
 * @author Pablo Chacin
 *
 */
public interface LocationStrategy {
	
	/**
	 * 
	 * Locates a node set of nodes in the Topology
	 * 
	 * @return true if the node could be located, false otherwise
	 */
	public boolean locate(Vector<Node> nodes);

	/**
	 * Sets the topology on which the strategy will work
	 */
	public void setTopology(Topology topology);
    
	/**
     * Resets the location strategy's state, if any 
	 */
    public void reset();
}

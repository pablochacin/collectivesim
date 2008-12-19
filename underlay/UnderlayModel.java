package edu.upc.cnds.collectivesim.underlay;

import java.util.Set;


import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayMetricType;
import edu.upc.cnds.collectives.underlay.UnderlayNode;

/**
 * Implements a simulated UnderlayNode. Delegates most functions to the UnderlayModel
 * 
 * @author Pablo Chacin
 *
 */
public interface UnderlayModel extends Underlay{
	
	
	/**
	 * Returns a list of metrics for the conecction between two given nodes
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public Metric[] probe(UnderlayNode source,UnderlayNode target,Set<UnderlayMetricType> metrics);

	/**
	 * Returns the know nodes for a given node
	 * @param model
	 * @return
	 */
	public Node[] getKnownNode(UnderlayNodeModel model);
}

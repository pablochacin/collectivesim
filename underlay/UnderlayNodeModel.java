package edu.upc.cnds.collectivesim.underlay;

import java.util.Set;


import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.imp.BasicNode;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayAddress;
import edu.upc.cnds.collectives.underlay.UnderlayMetricType;
import edu.upc.cnds.collectives.underlay.UnderlayNode;

/**
 * Simulates an underlay
 * 
 * @author Pablo Chacin
 *
 */
public class UnderlayNodeModel extends BasicNode implements UnderlayNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UnderlayModel underlay;
	
	public UnderlayNodeModel(Identifier id, UnderlayAddress address,UnderlayModel underlay) {
		super(id, address);
	    this.underlay = underlay;
	}

	public Underlay getUnderlay() {
		return underlay;
	}

	public Metric[] probe(UnderlayNode node, Set<UnderlayMetricType> metrics) {
		return underlay.probe(this, node, metrics);
	}

	public Node[] getKnownNodes() {
		return underlay.getKnownNode(this);
	}



		

}

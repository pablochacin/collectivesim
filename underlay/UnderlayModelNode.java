package edu.upc.cnds.collectivesim.underlay;

import java.util.Set;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.UnderlayAddress;
import edu.upc.cnds.collectives.underlay.UnderlayMetricType;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectives.underlay.imp.AbstractUnderlayNode;

/**
 * Simulates an underlay node at network location. Delegates most functions to the
 * UnderlayModel.
 * 
 * @author Pablo Chacin
 *
 */
public abstract class UnderlayModelNode extends AbstractUnderlayNode  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UnderlayModel model;
	
	
	public UnderlayModelNode(String type,Identifier id, UnderlayAddress address,UnderlayModel model) {
		super(model,id,address);
		this.model = model;
	}
	
	public UnderlayModelNode(Identifier id, UnderlayAddress address,UnderlayModel model) {
		super(model,id, address);
	    this.model = model;
	}



	public Metric[] probe(UnderlayNode node, Set<UnderlayMetricType> metrics) {
		return model.probe(this, node, metrics);
	}

	public Node[] getKnownNodes() {
		return model.getKnownNodes(this);
	}

}

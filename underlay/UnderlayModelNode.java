package edu.upc.cnds.collectivesim.underlay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Measurable;
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
public class UnderlayModelNode extends AbstractUnderlayNode  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UnderlayModel model;
	
	
	public UnderlayModelNode(String type,Identifier id, UnderlayAddress address,UnderlayModel model) {
		super(model,id,address,new HashMap());
		this.model = model;
	}
	
	public UnderlayModelNode(Identifier id, UnderlayAddress address,UnderlayModel model,Map attributes) {
		super(model,id, address,attributes);
	    this.model = model;
	}



	@Override
	public Measurable<UnderlayMetricType> getProbe(UnderlayNode node) {
		throw new UnsupportedOperationException();
	}

	public List<Node> getKnownNodes() {
		return model.getKnownNodes(this);
	}



}

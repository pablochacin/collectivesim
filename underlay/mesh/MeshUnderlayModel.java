package edu.upc.cnds.collectivesim.underlay.mesh;

import java.net.InetAddress;
import java.util.Vector;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.UnderlayException;
import edu.upc.cnds.collectives.underlay.UnderlayMetricType;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.Stream;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;
import edu.upc.cnds.collectivesim.underlay.UnderlayModelNode;
import edu.upc.cnds.collectivesim.underlay.UnderlayModelNodeAddress;

/**
 * Implements an underlay on which all nodes can see each othe (as in a subnetwork)
 * A node's neighbors are all other nodes in the mesh.
 * 
 * @author Pablo Chacin
 *
 */
public class MeshUnderlayModel extends UnderlayModel {


	
	public MeshUnderlayModel(Scheduler scheduler) {
		super(scheduler);
	}

	@Override
	public UnderlayNode[] getKnownNodes(UnderlayNode node) {
		Vector<UnderlayNode> neighbors = new Vector<UnderlayNode>(nodes.values());
		neighbors.remove(node);
		return neighbors.toArray(new UnderlayNode[neighbors.size()]);
		
	}


	@Override
	public UnderlayNode getNode(Identifier id) throws UnderlayException {
		UnderlayModelNode node = new UnderlayModelNode(id,new UnderlayModelNodeAddress(""),this);
		super.addNode(node);
		return node;
	}

	@Override
	public UnderlayMetricType[] getSupportedMetrics() {
		return new UnderlayMetricType[0];
	}

	@Override
	public Node[] resolve(InetAddress host) throws UnderlayException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Metric[] probe(UnderlayNode source, UnderlayNode target,UnderlayMetricType[] metrics) {
		throw new UnsupportedOperationException();
	}


}

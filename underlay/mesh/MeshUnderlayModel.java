package edu.upc.cnds.collectivesim.underlay.mesh;

import java.net.InetAddress;
import java.util.Set;
import java.util.Vector;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.UnderlayException;
import edu.upc.cnds.collectives.underlay.UnderlayMetricType;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;
import edu.upc.cnds.collectivesim.underlay.UnderlayModelNode;

/**
 * Implements an underlay on which all nodes can see each othe (as in a subnetwork)
 * A node's neighbors are all other nodes in the mesh.
 * 
 * @author Pablo Chacin
 *
 */
public class MeshUnderlayModel extends UnderlayModel {

	private Vector<UnderlayModelNode>nodes;
	
	public MeshUnderlayModel(Scheduler scheduler) {
		super(scheduler);
		nodes = new Vector<UnderlayModelNode>();
	}

	@Override
	public UnderlayNode[] getKnownNodes(UnderlayNode node) {
		Vector<UnderlayNode> neighbors = new Vector<UnderlayNode>(nodes);
		neighbors.remove(node);
		return neighbors.toArray(new UnderlayNode[neighbors.size()]);
		
	}

	@Override
	public Metric[] probe(UnderlayNode source, UnderlayNode target,	Set<UnderlayMetricType> metrics) {
		throw new UnsupportedOperationException();	}

	@Override
	public UnderlayNode getNode(Identifier id) throws UnderlayException {
		UnderlayModelNode node = new UnderlayModelNode(id,null,this);
		nodes.add(node);
		super.addNode(node);
		return node;
	}

	@Override
	public Set<UnderlayMetricType> getSupportedMetrics() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node[] resolve(InetAddress host) throws UnderlayException {
		// TODO Auto-generated method stub
		return null;
	}

}

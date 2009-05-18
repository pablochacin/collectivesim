package edu.upc.cnds.collectivesim.underlay.mesh;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.UnderlayException;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;
import edu.upc.cnds.collectivesim.underlay.Grid2D.UnderlayModelException;

/**
 * Implements an underlay on which all nodes can see each othe (as in a subnetwork)
 * A node's neighbors are all other nodes in the mesh.
 * 
 * @author Pablo Chacin
 *
 */
public class MeshUnderlayModel extends UnderlayModel {

	
	public MeshUnderlayModel(String name,Experiment experiment,Stream<Identifier> ids,int numNodes,Stream...attributes) {
		super(name,experiment, ids, numNodes,attributes);
	}

	@Override
	public List<Node> getKnownNodes(UnderlayNode node) {
		Vector<UnderlayNode> neighbors = new Vector<UnderlayNode>(nodes.values());
		neighbors.remove(node);
		return new ArrayList<Node>(neighbors);
		
	}



	@Override
	public List<Node> resolve(InetAddress host) throws UnderlayException {
		throw new UnsupportedOperationException();
	}



	@Override
	protected void generateNetworkTopology(List<? extends UnderlayNode> nodes)
			throws UnderlayModelException {
			//Do nothing. The mesh topology implies that all nodes know each other, so
		    //there is no need to create any topological structure
		
	}

	@Override
	protected void terminate() {		
		//Do nothing.
	}



}

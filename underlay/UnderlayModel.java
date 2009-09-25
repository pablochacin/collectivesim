package edu.upc.cnds.collectivesim.underlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayEvent;
import edu.upc.cnds.collectives.underlay.UnderlayException;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.base.AbstractModel;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.transport.UnderlayModelTransport;
import edu.upc.cnds.collectivesim.underlay.Grid2D.UnderlayModelException;

/**
 * Implements a simulated Underlay.
 * 
 * @author Pablo Chacin
 *
 */
public abstract class UnderlayModel extends AbstractModel implements Underlay {

	/**
	 * Keeps a mapping of nodes to identifiers
	 */
	protected Map<Identifier,UnderlayModelNode> nodes; 

	protected int numNodes;

	/**
	 * IdSpace used to generate the ids for the nodes
	 */
	protected Stream<Identifier> idStream;




	public UnderlayModel(String name,Experiment experiment, Stream<Identifier> idStream,int numNodes,Stream...attributeStreams) {
		super(name,experiment,attributeStreams);
		this.idStream = idStream;
		this.numNodes = numNodes;
		this.nodes = new HashMap<Identifier,UnderlayModelNode>(); 

	}

	@Override
	public void populate() throws ModelException{
		try {
			List<UnderlayModelNode> nodeList = new ArrayList<UnderlayModelNode>(numNodes);

			for(int i=0;i<numNodes;i++){
				Identifier id = idStream.nextElement();
				nodeList.add(buildNode(id,getAttributes()));			
			}

			generateNetworkTopology(nodeList);

			for(UnderlayModelNode n: nodeList){
				addNode(n);
			}


		} catch (UnderlayModelException e) {
			throw new ModelException("Exception generating underlay",e);
		}
	}

	/**
	 * 
	 * @return a List of the nodes in the model
	 */
	/**
	 * Returns all the nodes in the topology
	 */
	public List<UnderlayNode> getNodes() {
		List<UnderlayNode> nodeList = new ArrayList<UnderlayNode>();

		for(UnderlayNode n : nodes.values()) {
			nodeList.add(n);
		}

		return nodeList;
	}


	/**
	 * Removes the node from the topology. Informs the node's neighbors that the node
	 * is leaving the topology.
	 * 
	 */
	public void removeNode(UnderlayModelNode node)  {

		if(!nodes.containsValue(node)){
			return;
		}

		//Inform all neighbors of the lost node
		for(Node n : getKnownNodes(node)) {
			((UnderlayModelNode)n).notifyNodeLost(node);
		}
	}

	@Override
	public UnderlayNode createNode(Identifier id) throws UnderlayException {
		UnderlayModelNode node;
		try {
			node = buildNode(id,getAttributes());
		} catch (UnderlayModelException e) {

			throw new UnderlayException(e);
		}
		addNode(node);

		return node;
	}

	/**
	 * Builds a new instance of UnderlayNode for this underlay model.
	 * @param id
	 * @throws UnderlayModelException 
	 */
	protected UnderlayModelNode buildNode(Identifier id,Map attributes) throws UnderlayModelException{


		UnderlayModelTransport transport = new UnderlayModelTransport(this);

		UnderlayModelNode node = new UnderlayModelNode(this,id,new UnderlayModelNodeAddress(""),transport,attributes);

		return node;

	}

	protected void addNode(UnderlayModelNode node){

		nodes.put(node.getId(),node);

		super.addAgent(node.getId().toString(),node);

		//Inform all neighbors of the new node
		for(Node n : getKnownNodes(node)) {
			((UnderlayModelNode)n).notifyNodeDiscovered(node);
		}

		//report the creation of a new node in the underlay

		UnderlayEvent event = new UnderlayEvent(node,UnderlayEvent.NODE_FOUND,scheduler.getTime());
		experiment.reportEvent(event);
	}

	
	@Override
	public Metric[] probe(Node source, Node Target, String[] metrics)
			throws UnderlayException {
		throw new UnderlayException("Unsuported metrics" + FormattingUtils.arrayToString(metrics));
	}


	@Override
	public String[] getSupportedMetrics() {
		return new String[0];
	}
	/**
	 * Returns the know nodes for a given node
	 * @param topology
	 * @return
	 */
	public abstract List<Node> getKnownNodes(UnderlayNode node);




	/**
	 * Generates a topology for the given list of nodes
	 * 
	 * @param nodes (unordered) list of nodes of the Underlay
	 * @throws UnderlayModelException 
	 */
	protected abstract void generateNetworkTopology(List<? extends UnderlayNode>nodes) throws UnderlayModelException;

	public UnderlayModelNode getNode(Identifier id) {

		return nodes.get(id);
	}
}

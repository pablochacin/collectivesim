package edu.upc.cnds.collectivesim.underlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayException;
import edu.upc.cnds.collectives.underlay.UnderlayMetricType;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.Stream;
import edu.upc.cnds.collectivesim.model.imp.AbstractModel;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
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
    
	public UnderlayModel(Scheduler scheduler, Stream<Identifier> idStream,int numNodes) {
		super(scheduler);
		this.idStream = idStream;
		this.numNodes = numNodes;
        this.nodes = new HashMap<Identifier,UnderlayModelNode>(); 
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
			node = buildNode(id);
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
	protected UnderlayModelNode buildNode(Identifier id) throws UnderlayModelException{
		UnderlayModelNode node = new UnderlayModelNode(id,new UnderlayModelNodeAddress(""),this);
		return node;

	}
	
	protected void addNode(UnderlayModelNode node){
		
		nodes.put(node.getId(),node);
		
		super.addAgent(node);
		
		//Inform all neighbors of the new node
		for(Node n : getKnownNodes(node)) {
			((UnderlayModelNode)n).notifyNodeDiscovered(node);
		}
	}
	
	
	/**
	 * Returns a list of metrics for the connection between two given nodes
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public abstract Metric[] probe(UnderlayNode source,UnderlayNode target,UnderlayMetricType[] metrics);

	/**
	 * Returns the know nodes for a given node
	 * @param topology
	 * @return
	 */
	public abstract List<Node> getKnownNodes(UnderlayNode node);



	/**
	 * Generates an underlay with the given number of nodes. 
	 * @throws UnderlayException 
	 */
	public  List<? extends UnderlayNode> generateUnderlay() throws UnderlayModelException{
		List<UnderlayModelNode> nodeList = new ArrayList<UnderlayModelNode>(numNodes);
		
		for(int i=0;i<numNodes;i++){
			Identifier id = idStream.getValue();
			nodeList.add(buildNode(id));			
		}
				
		generateNetworkTopology(nodeList);

		for(UnderlayModelNode n: nodeList){
			addNode(n);
		}
		
		return nodeList;
	}
	
	/**
	 * Generates a topology for the given list of nodes
	 * 
	 * @param nodes (unordered) list of nodes of the Underlay
	 * @throws UnderlayModelException 
	 */
	protected abstract void generateNetworkTopology(List<? extends UnderlayNode>nodes) throws UnderlayModelException;
}

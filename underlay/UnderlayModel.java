package edu.upc.cnds.collectivesim.underlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayMetricType;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.imp.AbstractModel;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;

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
    
	public UnderlayModel(Scheduler scheduler) {
		super(scheduler);
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
		for(UnderlayNode n : getKnownNodes(node)) {
			((UnderlayModelNode)n).notifyNodeLost(node);
		}
	}
	
	
	
	protected void addNode(UnderlayModelNode node){
		
		nodes.put(node.getId(),node);
		
		super.addAgent(node);
		
		//Inform all neighbors of the new node
		for(UnderlayNode n : getKnownNodes(node)) {
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
	public abstract Metric[] probe(UnderlayNode source,UnderlayNode target,Set<UnderlayMetricType> metrics);

	/**
	 * Returns the know nodes for a given node
	 * @param model
	 * @return
	 */
	public abstract UnderlayNode[] getKnownNodes(UnderlayNode node);



}

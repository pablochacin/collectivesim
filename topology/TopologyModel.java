package edu.upc.cnds.collectivesim.topology;

import java.util.List;

import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.imp.AbstractModel;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;

/**
 * 
 * Builds and maintains a topology of nodes, generating the local topology view for
 * each node. This view is maintained by a TopologyModelAgent.
 * 
 * @author Pablo Chacin
 *
 */
public abstract class TopologyModel extends AbstractModel {
	
	protected List<UnderlayNode>nodes; 
		
	public TopologyModel(Scheduler scheduler,List<UnderlayNode>nodes) {
		super(scheduler);
		this.nodes = nodes;
		addBehavior("Topology Update", "updateTopology", true,100);
	}

	/**
	 * Generates the local view of the topology for a particular UnderlayNode
	 * 
	 * @param node the UnderlayNode to generate the view for.
	 * 
	 * @return the local view of the topology for a node
	 * 
	 */
	public Topology getTopology(UnderlayNode node){
		TopologyAgent agent = getTopologyAgent(node);
		super.addAgent(agent);
		return agent;
	}
	
	
	/**
	 * Generates a topology
	 */
	public abstract void generateTopology();
	
	/**
	 * Adds node to the topology
	 * 
	 * @param node
	 */
	public void addNode(UnderlayNode node){
		nodes.add(node);
	}
	
	/**
	 * Construct a topology agent for the given node
	 * 
	 * @param node
	 * @return
	 */
	protected abstract TopologyAgent getTopologyAgent(UnderlayNode node) ;
}

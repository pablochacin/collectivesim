package edu.upc.cnds.collectivesim.topology.ordered;

import java.util.List;

import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeAttributeComparator;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.OrderedSelector;
import edu.upc.cnds.collectives.node.RandomSelector;
import edu.upc.cnds.collectives.topology.BasicTopology;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.topology.TopologyAgent;
import edu.upc.cnds.collectivesim.topology.TopologyModel;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * Creates a Random Topology out of a list of UnderlayNodes. Assumes that
 * all nodes in the underlay are connected (that is, that there is a routing
 * mechanism that allows communicating not adjacent nodes.
 * 
 * @author Pablo Chacin
 *
 */
public class OrderedTopologyModel extends TopologyModel{

	protected int viewSize;
		
	private IdSpace space;
	
	public OrderedTopologyModel(String name,Experiment experiment,UnderlayModel underlay,int viewSize,IdSpace space) {
		super(name,experiment,underlay);
		this.viewSize = viewSize;
		this.space = space;
	}



	@Override
	protected void buildTopology() {
		
		for(UnderlayNode node: underlay.getNodes()){
							
			Topology topology = createTopology(node);
			
			//initiate the topology (without this call,the seeds are not considered!)
			//topology.update();
			
			topologies.put(node.getId(),topology);
		}
	}

	
	/**
	 * Creates the selector used to construct the topology
	 * 
	 * @param node
	 * @return
	 */
	protected Topology createTopology(UnderlayNode node){
		//TODO: find a way to create the comparator without calling space.getDistanceComparator
		//      to allow creating the OrderedSelector from model parameters

		NodeSelector selector = new OrderedSelector(new NodeAttributeComparator("key",space.getDistanceComparator(node.getId())));
		List<Node>seeds = node.getKnownNodes();
		Topology topology = new BasicTopology(node,seeds,selector,viewSize,false);
		
		return topology;
	}
	
	/**
	 * Factory method to create the TopologyAgents. Allows the subclasses of TopologyModel to
	 * instantiate their own agent classes.
	 * 
	 * @param topology the Topology that represents a local view of the topology in a node
	 * 
	 * @return the TopologyAgent that handles this topology
	 * 
	 */
	public TopologyAgent createAgent(Topology topology){
		OrderedTopologyAgent agent = new OrderedTopologyAgent(this,topology, new RandomSelector(), viewSize);
		return agent;
	}



	@Override
	protected void terminate() {
		// Do nothing
		
	}


}

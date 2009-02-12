package edu.upc.cnds.collectivesim.topology.ordered;

import java.util.ArrayList;
import java.util.List;

import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeComparator;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.OrderedSelector;
import edu.upc.cnds.collectives.node.RandomSelector;
import edu.upc.cnds.collectives.topology.BasicTopology;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.Stream;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.topology.TopologyAgent;
import edu.upc.cnds.collectivesim.topology.TopologyModel;

/**
 * Creates a Random Topology out of a list of UnderlayNodes. Assumes that
 * all nodes in the underlay are connected (that is, that there is a routing
 * mechanism that allows communicating not adjacent nodes.
 * 
 * @author Pablo Chacin
 *
 */
public class OrderedTopologyModel extends TopologyModel{

	private int size;
	
	private List<UnderlayNode> nodes;
	
	private IdSpace space;
	
	public OrderedTopologyModel(Scheduler scheduler,List<UnderlayNode> nodes,int size,IdSpace space) {
		super(scheduler,nodes);
		this.size = size;
		this.space = space;
		this.nodes = new ArrayList<UnderlayNode>(nodes);
	}

	@Override
	public void generateTopology() {
		for(UnderlayNode n: nodes){
			getTopology(n);
		}
	}

	@Override
	protected Topology buildTopology(UnderlayNode node) {
		NodeSelector randomSelector= new RandomSelector();
		List<Node>seeds = randomSelector.getSample(new ArrayList<Node>(nodes), size);
		
		NodeSelector selector = new OrderedSelector(new NodeComparator(space.getDistanceComparator(node.getId())));
		Topology topology = new BasicTopology(node,seeds,selector,size);
		return topology;
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
		Topology topology = buildTopology(node);
		OrderedTopologyAgent agent = new OrderedTopologyAgent(this,topology, new RandomSelector(), size);
		super.addAgent(agent);
		return topology;
	}


}

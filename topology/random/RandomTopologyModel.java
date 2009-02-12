package edu.upc.cnds.collectivesim.topology.random;

import java.util.ArrayList;
import java.util.List;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.RandomSelector;
import edu.upc.cnds.collectives.topology.BasicTopology;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
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
public class RandomTopologyModel extends TopologyModel{

	private int size;
	
	private List<UnderlayNode> nodes;
	
	public RandomTopologyModel(Scheduler scheduler,List<UnderlayNode> nodes,int size) {
		super(scheduler,nodes);
		this.size = size;
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
		NodeSelector selector= new RandomSelector();
		List<Node>seeds = selector.getSample(new ArrayList<Node>(nodes), size);
		Topology topology = new BasicTopology(node,seeds,new RandomSelector(),size);
		return topology;
	}


}

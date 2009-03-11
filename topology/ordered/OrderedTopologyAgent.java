package edu.upc.cnds.collectivesim.topology.ordered;

import java.util.List;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.topology.TopologyAgent;
import edu.upc.cnds.collectivesim.topology.TopologyModel;

/**
 * Offers a view of the topology for a particular underlay Node.
 * 
 * @author Pablo Chacin
 *
 */
public class OrderedTopologyAgent extends TopologyAgent {
			
	/**
	 * Size of the topology's view
	 */
	protected int size;
	
	/**
	 * Node selector used to select candidates for topology view
	 */
	protected NodeSelector selector;
	
	public OrderedTopologyAgent(TopologyModel model,Topology topology,NodeSelector selector,int size){
		super(model,topology);
		this.selector = selector;
		this.size = size;
	}

	public void updateTopology(){
		List<Node> candidates = selector.getSample(model.getUnderlay().getNodes(), size);
		topology.propose(candidates);
		topology.update();
		
	}


}

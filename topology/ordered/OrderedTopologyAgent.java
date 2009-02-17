package edu.upc.cnds.collectivesim.topology.ordered;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.topology.BasicTopology;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.TopologyObserver;
import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.model.imp.ReflexionModelAgent;
import edu.upc.cnds.collectivesim.topology.TopologyModel;

/**
 * Offers a view of the topology for a particular underlay Node.
 * 
 * @author Pablo Chacin
 *
 */
public class OrderedTopologyAgent extends ReflexionModelAgent implements TopologyObserver{
	
	private static Logger log = Logger.getLogger("colectivesim.topology");
	
	private Topology topology;
	
	private OrderedTopologyModel model;
	
	private int size;
	
	NodeSelector selector;
	
	public OrderedTopologyAgent(OrderedTopologyModel model,Topology topology,NodeSelector selector,int size){
		this.topology = topology;
		this.model = model;
		this.selector = selector;
		this.size = size;
		this.topology.addObserver(this);
	}

	public void updateTopology(){
		List<Node> candidates = selector.getSample(model.getUnderlay().getNodes(), size);
		topology.propose(candidates);
		topology.update();
		
		//log.info("Topology at node " + topology.getLocalNode().getId() + 
		//		 " "+FormattingUtils.listToString(topology.getNodes()));
	}

	@Override
	public void join(Node node) {
		model.nodeJoin(this.topology.getLocalNode(),node);
		
	}

	@Override
	public void leave(Node node) {
		model.nodeLeave(this.topology.getLocalNode(),node);
		
	}
}

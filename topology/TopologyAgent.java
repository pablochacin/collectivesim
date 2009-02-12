package edu.upc.cnds.collectivesim.topology;

import java.util.List;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.topology.BasicTopology;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.TopologyObserver;
import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.model.imp.ReflexionModelAgent;

/**
 * Offers a view of the topology for a particular underlay Node.
 * 
 * @author Pablo Chacin
 *
 */
public class TopologyAgent extends ReflexionModelAgent implements TopologyObserver{
	
	private static Logger log = Logger.getLogger("colectivesim.topology");
	
	private Topology topology;
	
	private TopologyModel model;
	
	public TopologyAgent(TopologyModel model,Topology topology){
		this.topology = topology;
		this.model = model;
		this.topology.addObserver(this);
	}

	public void updateTopology(){
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

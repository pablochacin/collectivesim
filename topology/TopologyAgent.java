package edu.upc.cnds.collectivesim.topology;

import java.util.logging.Logger;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.TopologyObserver;
import edu.upc.cnds.collectivesim.model.imp.ReflexionModelAgent;

/**
 * Offers a view of the topology for a particular underlay Node.
 * 
 * @author Pablo Chacin
 *
 */
public class TopologyAgent extends ReflexionModelAgent implements TopologyObserver{
	
	protected static Logger log = Logger.getLogger("colectivesim.topology");
	
	protected Topology topology;
	
	protected TopologyModel model;
	
	public TopologyAgent(TopologyModel model,Topology topology){
		this.topology = topology;
		this.model = model;
		this.topology.addObserver(this);
	}

	public void updateTopology(){
		topology.update();
	}

	@Override
	public void join(Node node) {
		model.nodeJoin(this.topology.getLocalNode(),node);
		
	}

	@Override
	public void leave(Node node) {
		model.nodeLeave(this.topology.getLocalNode(),node);
		
	}
	
	public Double getSize(){
		return (double)topology.getSize();
	}
}

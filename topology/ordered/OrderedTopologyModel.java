package edu.upc.cnds.collectivesim.topology.ordered;

import java.util.ArrayList;
import java.util.List;

import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeIdComparator;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.OrderedSelector;
import edu.upc.cnds.collectives.node.RandomSelector;
import edu.upc.cnds.collectives.topology.BasicTopology;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
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

	private int size;
		
	private IdSpace space;
	
	public OrderedTopologyModel(String name,Experiment experiment,UnderlayModel underlay,int size,IdSpace space) {
		super(name,experiment,underlay);
		this.size = size;
		this.space = space;
	}



	@Override
	protected void buildTopology() {
		
		for(UnderlayNode node: underlay.getNodes()){
		
			List<Node>seeds = node.getKnownNodes();
		
			NodeSelector selector = new OrderedSelector(new NodeIdComparator(space.getDistanceComparator(node.getId())));
			Topology topology = new BasicTopology(node,seeds,selector,size,false);
			
			//initiate the topology (without this call,the seeds are not considered!)
			//topology.update();
			
			topologies.put(node.getId(),topology);
		}
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
		OrderedTopologyAgent agent = new OrderedTopologyAgent(this,topology, new RandomSelector(), size);
		return agent;
	}


}

package edu.upc.cnds.collectivesim.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.TopologyEvent;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.imp.AbstractModel;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * 
 * Builds and maintains a topology of nodes, generating the local topology view for
 * each node. This view is maintained by a TopologyModelAgent.
 * 
 * @author Pablo Chacin
 *
 */
public abstract class TopologyModel extends AbstractModel  {
	

	protected UnderlayModel underlay;
	
	protected Map<Identifier,Topology>topologies;
	
		
	public TopologyModel(String name,Experiment experiment,UnderlayModel underlay) {
		super(name,experiment);
		this.underlay =  underlay;
		this.topologies = new HashMap<Identifier,Topology>();			
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
		
		return topologies.get(node.getId());		
	}
	
	public UnderlayModel getUnderlay(){
		return underlay;
	}
	
	public void populate() throws ModelException{
		generateTopology();
	}
	
	/**
	 * Generates a topology. 
	 */
	public void generateTopology(){
		
		//invoke topology building algorithm
		buildTopology();
		
		//create the topology agent to maintain each topology
		for(UnderlayNode n: underlay.getNodes()){
			
			Topology topology = getTopology(n);

			TopologyAgent agent =  createAgent(topology);
		
			topology.update();
			
			super.addAgent(agent);
		}
		
	}
	
	/**
	 * Creates a TopologyAgent responsible for the given local view of the topology.
	 * 
	 * @param topology the local representation of the topology for the agent
	 * @return
	 */
	protected abstract TopologyAgent createAgent(Topology topology);
	
	/**
	 * Constructs an initial topology for each node
	 * 
	 * @param node
	 * @return
	 */
	protected abstract void buildTopology() ;
	

	public List<Topology> getTopologies(){
		return new ArrayList<Topology>(topologies.values());
	}
	/**
	 * 
	 * @param localNode
	 * @param node
	 */
	public void nodeJoin(Node localNode, Node node) {
        //Report that the node is part of the topology
        Event event = new TopologyEvent(localNode,TopologyEvent.TOPOLOGY_JOIN,
        		                        getCurrentTime(),node);
        experiment.reportEvent(event);
	}

	/**
	 * 
	 * @param localNode
	 * @param node
	 */
	public void nodeLeave(Node localNode, Node node) {
        //Report that the node is part of the topology
        Event event = new TopologyEvent(localNode,TopologyEvent.TOPOLOGY_LEAVE,
        								getCurrentTime(),node);
        experiment.reportEvent(event);
		
	}
	
    
}

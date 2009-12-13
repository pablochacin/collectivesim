package edu.upc.cnds.collectivesim.overlay;


import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.topology.TopologyEvent;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.AgentFactory;
import edu.upc.cnds.collectivesim.model.base.BasicModel;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * 
 * Builds and maintains a topology of nodes, generating the local topology view for
 * each node. This view is maintained by a TopologyModelAgent.
 * 
 * @author Pablo Chacin
 *
 */
public class OverlayModel extends BasicModel<OverlayAgent>  {
	
				
	public OverlayModel(String name,Experiment experiment,int numAgents,AgentFactory factory,Stream ... attributes) {
		super(name,experiment,factory,numAgents,attributes);
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
        
        OverlayAgent agent = (OverlayAgent)getAgent(node.getId().toString());
        
        agent.incIndegree();
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
		
        OverlayAgent agent = (OverlayAgent)getAgent(node.getId().toString());
        
        agent.decIndegree();
	}
	
    
}

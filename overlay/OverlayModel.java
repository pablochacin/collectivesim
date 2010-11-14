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
	
				
	public OverlayModel(String name,Experiment experiment,int numAgents,AgentFactory factory) {
		super(name,experiment,factory,numAgents);
	}

		
	public OverlayModel(String name,Experiment experiment) {
		super(name,experiment);
	}

		
	/**
	 * 
	 * @param localNode
	 * @param node
	 */
	public void nodeLink(Node localNode, Node node) {
        //Report that the node is part of the topology
        Event event = new TopologyEvent(localNode,TopologyEvent.TOPOLOGY_LINK,
        		                        getCurrentTime());
        experiment.reportEvent(event);
        
        OverlayAgent agent = (OverlayAgent)getAgent(node.getId().toString());
        
        if(agent != null)
        	agent.incIndegree();
	}

	/**
	 * 
	 * @param localNode
	 * @param node
	 */
	public void nodeUnlink(Node localNode, Node node) {
        //Report that the node is part of the topology
        Event event = new TopologyEvent(localNode,getCurrentTime(),
        								TopologyEvent.TOPOLOGY_UNLINK,node);
        experiment.reportEvent(event);
		
        OverlayAgent agent = (OverlayAgent)getAgent(node.getId().toString());
        
        if(agent != null)
        	agent.decIndegree();
	}
	
    
	void nodeLeave(OverlayAgent agent){		
		
		Event event = new TopologyEvent(agent.getOverlay().getLocalNode(),
				                         TopologyEvent.TOPOLOGY_LEAVE,
				                         getCurrentTime());
		experiment.reportEvent(event);

		for(Node n: agent.getOverlay().getNodes()){
			nodeUnlink(agent.getOverlay().getLocalNode(),n);
		}
		
		super.removeAgent(agent);
	}
	
	
	void nodeJoin(OverlayAgent agent){
		 Event event = new TopologyEvent(agent.getOverlay().getLocalNode(),
                 TopologyEvent.TOPOLOGY_JOIN,
                 getCurrentTime());
		 
		 experiment.reportEvent(event);
	}
}

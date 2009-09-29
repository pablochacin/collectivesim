package edu.upc.cnds.collectivesim.overlay;


import java.util.Map;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.topology.TopologyEvent;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.base.AbstractModel;
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
public abstract class OverlayModel extends AbstractModel  {
	

	protected UnderlayModel underlay;
	
	protected OverlayFactory factory;

		
	public OverlayModel(String name,Experiment experiment,UnderlayModel underlay,OverlayFactory factory,Stream ... attributes) {
		super(name,experiment,attributes);
		this.underlay =  underlay;	
		this.factory = factory;
	}

	
	public UnderlayModel getUnderlay(){
		return underlay;
	}
	
	public void populate() throws ModelException{

		//create the topology agent to maintain each topology
		for(UnderlayNode n: underlay.getNodes()){
			
			Overlay overlay =  factory.getOverlay(n);
			Map attributes = getAttributes();
			OverlayAgent agent = createOverlayAgent(overlay,attributes);
			super.addAgent(agent);
		}
		
		
		//make every agent to join the overlay
		for(ModelAgent a: getAgents()){
			((OverlayAgent)a).join();
		}

	}
	
	/**
	 * 
	 * @param overlay
	 * @param attributes
	 * @return
	 * @throws ModelException 
	 */
	protected abstract OverlayAgent createOverlayAgent(Overlay overlay,Map attributes) throws ModelException;
	
		
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

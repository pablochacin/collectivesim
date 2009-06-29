package edu.upc.cnds.collectivesim.overlay;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.topology.TopologyEvent;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.base.AbstractModel;
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

		
	public OverlayModel(String name,Experiment experiment,UnderlayModel underlay) {
		super(name,experiment);
		this.underlay =  underlay;			
	}

	
	public UnderlayModel getUnderlay(){
		return underlay;
	}
	
	public void populate() throws ModelException{

		//create the topology agent to maintain each topology
		for(UnderlayNode n: underlay.getNodes()){
			
			OverlayAgent agent =  createAgent(n);
					
			super.addAgent(agent);
		}

	}
	
	
	/**
	 * Creates a TopologyAgent responsible for the given local view of the topology.
	 * 
	 * @param topology the local representation of the topology for the agent
	 * @return
	 * @throws ModelException 
	 */
	protected abstract OverlayAgent createAgent(UnderlayNode node) throws ModelException;
	
		
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

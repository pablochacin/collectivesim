package edu.upc.cnds.collectivesim.overlay.gradient;

import java.util.ArrayList;
import java.util.List;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;

/**
 * Maintains a gradient topology. 
 * 
 * The updateTopology method contacts a randomly selected peer from the gradient topology and
 * gets its peer list, considering as candidates for the gradient topology.
 * 
 * The randomWalk method contacts a randomly selected peer from the random topology and gets its
 * list of peers, considering as candidates for the random topology and the gradient topology.
 * 
 * @author Pablo Chacin
 *
 */
public class GradientOverlayAgent extends OverlayAgent {

	/**
	 * Size of the topology's view
	 */
	protected Topology randomTopology;


	public GradientOverlayAgent(OverlayModel model, Topology topology,
			Routing router,Topology randomTopology) {
			
			super(model, topology, router);
			
			this.randomTopology = randomTopology;
		
	}
	
	public void updateRandom(){
		randomTopology.update();
		
		topology.propose(randomTopology.getNodes());
	}
	

	
	/**
	 * Updates the node's utility
	 * @param variation
	 */
	public void updateUtility(Double variation){
		
		
		Double utility = (Double)topology.getLocalNode().getAttributes().get("utility");
				
		if((utility == 1) && (variation > 0))
			variation = -1.0*variation;

		if((utility == 0) && (variation < 0))
			variation = -1.0*variation;

		//ensure that 0 <= utility+variation `<= 1
		if(variation < 0)
			utility = Math.max(0,utility+variation);
		else
			utility = Math.min(1, utility+variation);
		
				
		topology.getLocalNode().getAttributes().put("utility", utility);

	}

	
	public Double getUtility(){
		return (Double)topology.getLocalNode().getAttributes().get("utility");
	}

	/**
	 * 
	 * @return the average of the (absolute) difference between the node's utility 
	 *         and its peer's utility
	 */
	public Double getGradient(){

		
		Double utility = (Double)topology.getLocalNode().getAttributes().get("utility");
		Double gradient = 0.0;
		for(Node n: topology.getNodes()){
			//get the actual utility of the node,not the local value 
			GradientOverlayAgent agent = (GradientOverlayAgent)model.getAgent(n.getId().toString());
			
			Double otherUtility = (Double)agent.getUtility();
			Double difference = + Math.abs(utility-otherUtility);
			gradient += difference;
		}
		
		
		return gradient/(double)topology.getSize();

	}

	
	
	
	
	public List<Node>getPeers(){
		List<Node> peers = new ArrayList<Node>(topology.getNodes());
		return peers;
		
	}
}

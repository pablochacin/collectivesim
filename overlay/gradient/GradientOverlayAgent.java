package edu.upc.cnds.collectivesim.overlay.gradient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Normal;
import cern.jet.random.engine.MersenneTwister64;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.base.RandomSelector;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectives.util.FormattingUtils;
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
	protected Overlay randomOverlay;

	/**
	 * Node selector used to select random peers
	 */
	protected NodeSelector randomSelector;

	
	public GradientOverlayAgent(OverlayModel model,Overlay overlay,Overlay randomOverlay){
		super(model,overlay);
		this.randomOverlay = randomOverlay;
		this.randomSelector = new RandomSelector();

	}

	
	public List<Node>getPeers(){
		List<Node> peers = new ArrayList<Node>(overlay.getNodes());
		return peers;
		
	}
	public void updateOverlay(){

		overlay.update();
		
		/*overlay.getTopology().update();
		
		Node peer = randomSelector.getSample(overlay.getNodes(),1).get(0);
		GradientOverlayAgent agent = (GradientOverlayAgent)model.getAgent(peer.getId().toString());
		List<Node> candidates = agent.getPeers();
		overlay.getTopology().propose(candidates);
	
		
		if(overlay.getLocalNode().getId().toString().equals("0000000000000000")){
			System.out.println("Utility: "+ getUtility() + "\n" +
				           "Gradient: " + getGradient() + "\n" +
				           FormattingUtils.collectionToString(overlay.getNodes()));
		}
		*/
	}
	
	
	/**
	 * 
	 * Gets the peers form other node and consider them as candidates
	 * 
	 * @param candidates
	 */
	public void updateRandom(){

			randomOverlay.update();
	}



	/**
	 * Updates the node's utility
	 * @param variation
	 */
	public void updateUtility(Double variation){
		
		Double utility = (Double)overlay.getLocalNode().getAttributes().get("utility");
				
		if((utility == 1) && (variation > 0))
			variation = -1.0*variation;

		if((utility == 0) && (variation < 0))
			variation = -1.0*variation;

		//ensure that 0 <= utility+variation `<= 1
		if(variation < 0)
			utility = Math.max(0,utility+variation);
		else
			utility = Math.min(1, utility+variation);
		
				
		overlay.getLocalNode().getAttributes().put("utility", utility);

	}

	
	public Double getUtility(){
		return (Double)overlay.getLocalNode().getAttributes().get("utility");
	}

	/**
	 * 
	 * @return the average of the (absolute) difference between the node's utility 
	 *         and its peer's utility
	 */
	public Double getGradient(){

		
		Double utility = (Double)overlay.getLocalNode().getAttributes().get("utility");
		Double gradient = 0.0;
		for(Node n: overlay.getNodes()){
			Double otherUtility = (Double)n.getAttributes().get("utility");
			Double difference = + Math.abs(utility-otherUtility);
			gradient += difference;
		}
		
		
		return gradient/(double)overlay.getSize();

	}

}

package edu.upc.cnds.collectivesim.topology.gradient;

import java.util.List;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.RandomSelector;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.topology.TopologyAgent;
import edu.upc.cnds.collectivesim.topology.TopologyModel;

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
public class GradientTopologyAgent extends TopologyAgent {
			
	/**
	 * Size of the topology's view
	 */
	protected Topology randomTopology;
	
	/**
	 * Node selector used to select random peers
	 */
	protected NodeSelector randomSelector;
	
	
	public GradientTopologyAgent(TopologyModel model,Topology topology,Topology randomTopology){
		super(model,topology);
		this.randomTopology = randomTopology;
		this.randomSelector = new RandomSelector();
	}

	
	/**
	 * Updates the Random topology and delegates to super class
	 * the update of the gradient topology
	 */
	public void updateTopology(){
		randomTopology.update();
		exchangePeers();
		super.updateTopology();
	}
	
	
	/**
	 * 
	 * Gets the peers form other node and consider them as candidates
	 * 
	 * @param candidates
	 */
	protected void exchangePeers(){
		if(topology.getSize() > 0){
			Node peer = randomSelector.getSample(topology.getNodes(),1).get(0);
			GradientTopologyAgent agent = (GradientTopologyAgent)model.getAgent(peer.getId().toString());
			List<Node> candidates = agent.getPeers();
			topology.propose(candidates);

		}

	}

	/**
	 * 
	 * @return the list of current peers
	 */
	protected List<Node> getPeers(){
		return getTopology().getNodes();
	}
	
	
	protected List<Node> getRandomPeers(){
		return randomTopology.getNodes();	
	}
	
	/**
	 * Select a node from its list of random peers, contact it and exchange random	 * 
	 * peers. It is assumed there is at least a random peer.
	 * 
	 * random peers are also considered candidates for the gradient topology.
	 */
	public void randomWalk(){
		Node peer = randomSelector.getSample(randomTopology.getNodes(),1).get(0);
		
		GradientTopologyAgent peerAgent = (GradientTopologyAgent)model.getAgent(peer.getId().toString());
		List<Node> candidates = peerAgent.getRandomPeers();
		randomTopology.propose(candidates);
		getTopology().propose(candidates);
	}

	
	
	/**
	 * Updates the node's utility
	 * @param variation
	 */
	public void updateUtility(Double variation){
		Double utility = (Double)topology.getLocalNode().getAttributes().get("utility");
		utility = Math.max(0,utility+variation);
	}
	
}

package edu.upc.cnds.collectivesim.topology.gradient;

import java.util.List;
import java.util.Vector;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.RandomSelector;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.util.FormattingUtils;
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
		randomTopology.update();
	}


	/**
	 * Get new candidates and update the topology 
	 */
	public void updateTopology(){
		exchangePeers();
		topology.update();

		if(topology.getLocalNode().getId().toString().equals("0000000000000000")){
			Vector<Double> utilities = new Vector<Double>();
			for(Node n: topology.getNodes()){
				utilities.add((Double)n.getAttributes().get("utility"));
			}
			
			System.out.println("-- utility="+ topology.getLocalNode().getAttributes().get("utility")+ 
					           " Gradient=" + getGradient() +"\n" + 
					           FormattingUtils.collectionToString(utilities));
			
		}
	}


	/**
	 * 
	 * Gets the peers form other node and consider them as candidates
	 * 
	 * @param candidates
	 */
	protected void exchangePeers(){

			Node peer = randomSelector.getSample(topology.getNodes(),1).get(0);
			GradientTopologyAgent agent = (GradientTopologyAgent)model.getAgent(peer.getId().toString());
			List<Node> candidates = agent.getPeers();
			topology.propose(candidates);

	}

	/**
	 * 
	 * @return the list of current peers
	 */
	protected List<Node> getPeers(){
		return  topology.getNodes();
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
		randomTopology.update();
		
		topology.propose(candidates);
	}



	/**
	 * Updates the node's utility
	 * @param variation
	 */
	public void updateUtility(Double variation){
		Double utility = (Double)topology.getLocalNode().getAttributes().get("utility");

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
			gradient = + Math.abs(utility-(Double)n.getAttributes().get("utility"));
		}

		return gradient/topology.getSize();
	}

}

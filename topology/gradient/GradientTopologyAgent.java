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
		randomTopology.update();
	}


	/**
	 * Get new candidates and update the topology 
	 */
	public void updateTopology(){
		topology.update();
		exchangePeers();

	}


	/**
	 * 
	 * Gets the peers form other node and consider them as candidates
	 * 
	 * @param candidates
	 */
	protected void exchangePeers(){

			Node peer =topology.getNodes().get(0);
			GradientTopologyAgent agent = (GradientTopologyAgent)model.getAgent(peer.getId().toString());
			List<Node>candidates = topology.getNodes();
			candidates.add(topology.getLocalNode());
			agent.propose(candidates);

	}

	
	/**
	 * Consider nodes for the next update
	 * @param candidates
	 */
	protected void propose(List<Node>candidates){
		topology.propose(candidates);
	}
	
	
	/**
	 * 
	 * Gets the peers form other node and consider them as candidates
	 * 
	 * @param candidates
	 */
	public void updateRandom(){

			randomTopology.update();
			Node peer = randomSelector.getSample(randomTopology.getNodes(),1).get(0);
			GradientTopologyAgent agent = (GradientTopologyAgent)model.getAgent(peer.getId().toString());
			List<Node>candidates = randomTopology.getNodes();
			candidates.add(randomTopology.getLocalNode());
			agent.proposeRandom(candidates);

	}


	/**
	 * Consider nodes for the next update
	 * @param candidates
	 */
	protected void proposeRandom(List<Node>candidates){
		randomTopology.propose(candidates);
	}



	/**
	 * Updates the node's utility
	 * @param variation
	 */
	public void updateUtility(Double variation){
		Double utility = (Double)topology.getLocalNode().getAttributes().get("utility");

		if((utility == 1) && (variation > 0)){
			variation = -1.0*variation;
		}

		if((utility == 0) && (variation < 0)){
			variation = -1.0*variation;
		}

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

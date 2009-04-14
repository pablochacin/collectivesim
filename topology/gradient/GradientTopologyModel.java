package edu.upc.cnds.collectivesim.topology.gradient;

import edu.upc.cnds.collectives.node.NodeAttributeComparator;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.OrderedSelector;
import edu.upc.cnds.collectives.node.RandomSelector;
import edu.upc.cnds.collectives.topology.BasicTopology;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.Stream;
import edu.upc.cnds.collectivesim.topology.TopologyAgent;
import edu.upc.cnds.collectivesim.topology.TopologyModel;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * 
 * Factory for GradientTopologyAgents that maintains a gradient topology.
 * 
 * @author Pablo Chacin
 *
 */
public class GradientTopologyModel extends TopologyModel{

	/**
	 * size of the gradient topology
	 */
	protected int gradientTopologySize;
	
	/**
	 * size of the random topology
	 */
	protected int randomTopologySize;
			
	/**
	 * Constructor
	 * @param name name of the model
	 * @param experiment Experiment on which this model resides
	 * @param underlay UnderlayModel that gives access to underlay nodes
	 * @param gradientTopologySize maximum size of the gradient Topology
	 * @param randomTopologySize maximum size of the random topology
	 */
	public GradientTopologyModel(String name,Experiment experiment,UnderlayModel underlay, int gradientTopologySize,int randomTopologySize) {
		super(name,experiment,underlay);
		this.gradientTopologySize = gradientTopologySize;
		this.randomTopologySize = randomTopologySize;
	}



	/**
	 * Factory method to create the TopologyAgents. Allows the subclasses of TopologyModel to
	 * instantiate their own agent classes.
	 * 
	 * @param topology the Topology that represents a local view of the topology in a node
	 * 
	 * @return the TopologyAgent that handles this topology
	 * 
	 */
	protected TopologyAgent createAgent(UnderlayNode node){

		
		//create ordered topology using the utility as a attribute of comparison
		
		NodeSelector orderedSelector = new OrderedSelector(new NodeAttributeComparator("utility",new GradientComparator(node)));
		//NodeSelector orderedSelector = new RandomSelector();
		Topology gradientTopology = new BasicTopology(node, node.getKnownNodes(),orderedSelector,
				                                      gradientTopologySize,false);

		
		//create random topology
		NodeSelector randomSelector = new RandomSelector();
		Topology randomTopology = new BasicTopology(node,node.getKnownNodes(),randomSelector,
				                                    randomTopologySize,false);
		
		//create topology agent
		GradientTopologyAgent agent = new GradientTopologyAgent(this,gradientTopology,randomTopology);
		return agent;

	}



	@Override
	protected void terminate() {
		// Do nothing
		
	}


}

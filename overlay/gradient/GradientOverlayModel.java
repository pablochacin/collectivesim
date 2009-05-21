package edu.upc.cnds.collectivesim.overlay.gradient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.base.BasicNode;
import edu.upc.cnds.collectives.node.base.NodeAttributeComparator;
import edu.upc.cnds.collectives.node.base.OrderedSelector;
import edu.upc.cnds.collectives.node.base.RandomSelector;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.epidemic.EpidemicOverlay;
import edu.upc.cnds.collectives.overlay.gradient.GradientOverlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.base.DummyMatch;
import edu.upc.cnds.collectives.routing.base.GenericRouter;
import edu.upc.cnds.collectives.routing.base.GreedyRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectives.routing.base.Router;
import edu.upc.cnds.collectives.routing.base.RoutingAlgorithm;
import edu.upc.cnds.collectives.routing.epidemic.EpidemicRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.kbr.KbrRouter;
import edu.upc.cnds.collectives.routing.kbr.KeyDistanceMatchFunction;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.base.BasicTopology;
import edu.upc.cnds.collectives.topology.epidemic.EpidemicTopology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.routing.kbr.RoutingModelEvent;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * 
 * Factory for GradientTopologyAgents that maintains a gradient topology.
 * 
 * @author Pablo Chacin
 *
 */
public class GradientOverlayModel extends OverlayModel{

			
	protected int viewSize;
	
	/**
	 * Fraction of neighbors to propagate the updates
	 */
	protected int exchangeSet;
	
	protected int randomViewSize; 
	
	protected int randomExchangeSet;
	
	protected Integer ttl;
	
	protected Double tolerance;
	
	protected Stream<String>roles;
	
	/**
	 * Constructor
	 * @param name name of the model
	 * @param experiment Experiment on which this model resides
	 * @param underlay UnderlayModel that gives access to underlay nodes
	 * @param gradientTopologySize maximum size of the gradient Topology
	 * @param randomTopologySize maximum size of the random topology
	 */
	public GradientOverlayModel(String name,Experiment experiment,UnderlayModel underlay, 
			                    Integer viewSize,Integer exchangeSet,Integer randomViewSize,
			                    Integer randomExchangeSet,Double tolerance,Integer ttl,
			                    Stream<String>roles) {
		super(name,experiment,underlay);
		this.viewSize =viewSize;
		this.exchangeSet= exchangeSet;
		this.randomViewSize =randomViewSize;
		this.randomExchangeSet =randomExchangeSet;
		this.tolerance = tolerance;
		this.ttl = ttl;
		this.roles = roles;
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
	protected OverlayAgent createAgent(UnderlayNode node){

	
		//use known nodes from underly as seeds
		List<Node>seeds = new ArrayList<Node>();
		for(Node n : node.getKnownNodes()){
			seeds.add(n.getReference());
			
		}
		
		//create a selector to order the nodes based on their key's distance to the local node's key
		//TODO: find a way to create the comparator without calling space.getDistanceComparator
		//      to allow creating the OrderedSelector from model parameters

		NodeSelector orderedSelector = new OrderedSelector(new NodeAttributeComparator("utility",new GradientComparator(node)));
			
		Topology topology = new BasicTopology(node,seeds,orderedSelector,viewSize,false);
		RoutingAlgorithm epidemicRouting = new EpidemicRoutingAlgorithm(topology,exchangeSet);
		Routing updateRouter = new GenericRouter("TopologyUpdateRouter",node,epidemicRouting,node.getTransport());
				
		
		//TODO create a UtilityMatchFunction to use in the greedy utility routing		
		RoutingAlgorithm utilityRouting = new GreedyRoutingAlgorithm(topology,new UtilityMatchFunction("utility"));

		Routing utilityRouter = new GenericRouter("gradient router",topology.getLocalNode(),utilityRouting,node.getTransport());
				
		
		Topology randomTopology = new BasicTopology(node,seeds,new RandomSelector(),randomViewSize,false);
		RoutingAlgorithm randomEpidemic = new EpidemicRoutingAlgorithm(randomTopology,randomExchangeSet);
		Routing randomUpdater =new GenericRouter("random updater",node,randomEpidemic,node.getTransport());
		Overlay overlay = new GradientOverlay(node,topology,utilityRouter,updateRouter,new Destination(new HashMap(),false),1,
				                             randomTopology, randomUpdater, new Destination(new HashMap(),false), exchangeSet);
					
		return new GradientOverlayAgent(this,overlay, utilityRouter, randomTopology,roles.getValue(),tolerance,ttl);

	}



	@Override
	protected void terminate() {
		// Do nothing
		
	}


}
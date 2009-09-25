package edu.upc.cnds.collectivesim.overlay.utility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.base.NodeAttributeComparator;
import edu.upc.cnds.collectives.node.base.OrderedSelector;
import edu.upc.cnds.collectives.node.base.RandomSelector;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.gradient.GradientComparator;
import edu.upc.cnds.collectives.overlay.gradient.GradientOverlay;
import edu.upc.cnds.collectives.overlay.gradient.UtilityMatchFunction;
import edu.upc.cnds.collectives.overlay.gradient.decay.AgeComparator;
import edu.upc.cnds.collectives.overlay.gradient.decay.DecayMatchFunction;
import edu.upc.cnds.collectives.overlay.gradient.decay.DecayUtilityMatchFunction;
import edu.upc.cnds.collectives.overlay.gradient.decay.GradientDecayComparator;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.MatchFunction;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.base.DummyMatch;
import edu.upc.cnds.collectives.routing.base.GenericRouter;
import edu.upc.cnds.collectives.routing.base.GreedyRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.base.RoutingAlgorithm;
import edu.upc.cnds.collectives.routing.epidemic.EpidemicRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.gradient.GradientNeighborRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.gradient.GradientRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.gradient.GradientRoutingAlgorithmWithMemory;
import edu.upc.cnds.collectives.routing.probabilistic.ProbabilisticRoutingAlgorithm;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.base.BasicTopology;
import edu.upc.cnds.collectives.topology.base.IncrementalUpdateTopology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * 
 * Factory for GradientTopologyAgents that maintains a gradient topology.
 * 
 * @author Pablo Chacin
 *
 */
public class UtilityOverlayModel extends OverlayModel{

			
	protected int viewSize;
	
	/**
	 * Fraction of neighbors to propagate the updates
	 */
	protected int exchangeSet;
	
	protected int randomViewSize; 
	
	protected int randomExchangeSet;
	
	protected Double alpha;
	
	protected Long cycleLength;
	
	/**
	 * Constructor
	 * @param name name of the model
	 * @param experiment Experiment on which this model resides
	 * @param underlay UnderlayModel that gives access to underlay nodes
	 * @param gradientTopologySize maximum size of the gradient Topology
	 * @param randomTopologySize maximum size of the random topology
	 */
	public UtilityOverlayModel(String name,Experiment experiment,UnderlayModel underlay, 
			                    Integer viewSize,Integer exchangeSet,long cycleLength,Double alpha,
			                    Integer randomViewSize,Integer randomExchangeSet) {
		super(name,experiment,underlay);
		this.viewSize =viewSize;
		this.exchangeSet= exchangeSet;
		this.randomViewSize =randomViewSize;
		this.randomExchangeSet =randomExchangeSet;
		this.alpha = alpha;
		this.cycleLength = cycleLength;
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
	protected OverlayAgent createAgent(UnderlayNode node) throws ModelException{

	
		//use known nodes from underly as seeds
		List<Node>seeds = new ArrayList<Node>();
		for(Node n : node.getKnownNodes()){
			seeds.add(n.getReference());
			
		}
		
		//create a selector to order the nodes based on their key's distance to the local node's key
		//TODO: find a way to create the comparator without calling space.getDistanceComparator
		//      to allow creating the OrderedSelector from model parameters


			
		//Topology topology = new BasicTopology(node,seeds,new OrderedSelector(new GradientDecayComparator(node,cycleLength,alpha)),viewSize,false);
		//Topology topology = new BasicTopology(node,seeds,new OrderedSelector(new GradientComparator(node)),viewSize,false);
		//Topology topology = new BasicTopology(node,seeds,new RandomSelector(),viewSize,false);
		Topology topology = new BasicTopology(node,seeds,new OrderedSelector(new AgeComparator(node,cycleLength)),viewSize,false);
		
		RoutingAlgorithm epidemicRouting = new EpidemicRoutingAlgorithm(topology,exchangeSet);
		MatchFunction epidemicMatchFunction = new DummyMatch(1.0);
		Routing updateRouter = new GenericRouter("TopologyUpdateRouter",node,epidemicMatchFunction,epidemicRouting,node.getTransport());
				
				
		Topology randomTopology = new BasicTopology(node,seeds,new RandomSelector(),randomViewSize,false);
		//Topology randomTopology = new BasicTopology(node,seeds,new OrderedSelector(new AgeComparator(node,cycleLength)),randomViewSize,false);
		RoutingAlgorithm randomEpidemic = new EpidemicRoutingAlgorithm(randomTopology,randomExchangeSet);
		Routing randomUpdater =new GenericRouter("topology.updater",node,epidemicMatchFunction,randomEpidemic,node.getTransport());

				
		
		//RoutingAlgorithm utilityRouting = new GradientNeighborRoutingAlgorithm(topology,randomTopology,routingFuction,
		//		                                                                 exchangeSet,overlaySelector);
		//RoutingAlgorithm utilityRouting = new GradientRoutingAlgorithmWithMemory(topology,randomTopology,routingFuction,
		//		                                                                 exchangeSet,overlaySelector);
		//RoutingAlgorithm utilityRouting = new GradientRoutingAlgorithm(topology,randomTopology,new DecayUtilityMatchFunction(node,"utility",cycleLength,alpha));
		//RoutingAlgorithm utilityRouting = new GradientRoutingAlgorithm(topology,randomTopology,new UtilityMatchFunction("utility"));
		//RoutingAlgorithm utilityRouting = new ProbabilisticRoutingAlgorithm(topology,routingFuction);
		//RoutingAlgorithm utilityRouting = new EpidemicRoutingAlgorithm(topology,1);
		//RoutingAlgorithm utilityRouting = new GreedyRoutingAlgorithm(topology,new DecayUtilityMatchFunction(node,"utility",cycleLength,alpha));
		RoutingAlgorithm utilityRouting = new GreedyRoutingAlgorithm(topology,new UtilityMatchFunction("utility"));


		Routing utilityRouter = new GenericRouter("gradient.router",topology.getLocalNode(),new UtilityMatchFunction("utility"),utilityRouting,node.getTransport());

		Overlay overlay = new GradientOverlay(node,topology,utilityRouter,updateRouter,new Destination(new HashMap(),false),1,
				                             randomTopology, randomUpdater, new Destination(new HashMap(),false), exchangeSet);
					
		Double utility = (Double)node.getAttributes().get("utility");
		return createAgent(overlay,utilityRouter,randomTopology,utility);

	}

	
	/**
	 * Creates an OverlayAgent from the overlay components
	 * 
	 * @param overlay
	 * @param router
	 * @param randomTopology
	 * @return
	 * @throws ModelException 
	 */
	protected OverlayAgent createAgent(Overlay overlay,Routing router,Topology randomTopology,Double utility) throws ModelException{
		return new UtilityOverlayAgent(this,overlay, router, randomTopology,utility);
	}
	
	@Override
	protected void terminate() {
		// Do nothing
		
	}


}

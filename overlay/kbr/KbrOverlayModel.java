package edu.upc.cnds.collectivesim.overlay.kbr;

import java.util.HashMap;
import java.util.List;

import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.base.NodeAttributeComparator;
import edu.upc.cnds.collectives.node.base.OrderedSelector;
import edu.upc.cnds.collectives.node.base.RandomSelector;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.epidemic.EpidemicOverlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.MatchFunction;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.base.DummyMatch;
import edu.upc.cnds.collectives.routing.base.GenericRouter;
import edu.upc.cnds.collectives.routing.base.GreedyRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.base.RoutingAlgorithm;
import edu.upc.cnds.collectives.routing.epidemic.EpidemicRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.kbr.KbrRouter;
import edu.upc.cnds.collectives.routing.kbr.KeyDistanceMatchFunction;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.base.BasicTopology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * Creates a Random Topology out of a list of UnderlayNodes. Assumes that
 * all nodes in the underlay are connected (that is, that there is a routing
 * mechanism that allows communicating not adjacent nodes.
 * 
 * @author Pablo Chacin
 *
 */
public class KbrOverlayModel extends OverlayModel{

	protected int viewSize;
		
	private IdSpace space;
	
	public KbrOverlayModel(String name,Experiment experiment,UnderlayModel underlay,int viewSize,IdSpace space) {
		super(name,experiment,underlay);
		this.viewSize = viewSize;
		this.space = space;
	}

	
	/**
	 * Creates the selector used to construct the topology
	 * 
	 * @param node
	 * @return
	 */
	@Override
	protected OverlayAgent createAgent(UnderlayNode node){
		//TODO: find a way to create the comparator without calling space.getDistanceComparator
		//      to allow creating the OrderedSelector from model parameters

		//create a selector to order the nodes based on their key's distance to the local node's key
		NodeSelector selector = new OrderedSelector(new NodeAttributeComparator("id",space.getDistanceComparator(node.getId())));
		
		//use known nodes from underly as seeds
		List<Node>seeds = node.getKnownNodes();
		
		//create a basic topology 
		Topology topology = new BasicTopology(node,seeds,selector,viewSize,false);
		
		RoutingAlgorithm greedyKey = new GreedyRoutingAlgorithm(topology,new KeyDistanceMatchFunction(space,"id"));
		
		Routing kbrRouter = new KbrRouter("kbr",topology, new KeyDistanceMatchFunction(space,"id"),greedyKey,node.getTransport());
		
		RoutingAlgorithm epidemic = new EpidemicRoutingAlgorithm(topology,new DummyMatch(1.0),new RandomSelector(),1);
		
		Routing epidemicRouter = new GenericRouter("epidemic",topology,new DummyMatch(1.0),epidemic,node.getTransport());
		
		//define the destination of updates as any node and deliver as multicast
		Destination updateDestination = new Destination(new HashMap(),false);
		
		Overlay overlay =  new EpidemicOverlay(node, topology,kbrRouter,epidemicRouter, updateDestination, 1);
		
		return new KbrOverlayAgent(this,overlay);
	}
	


}

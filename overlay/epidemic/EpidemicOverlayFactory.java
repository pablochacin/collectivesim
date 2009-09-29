package edu.upc.cnds.collectivesim.overlay.epidemic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.gradient.GradientOverlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.MatchFunction;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.base.DummyMatch;
import edu.upc.cnds.collectives.routing.base.GenericRouter;
import edu.upc.cnds.collectives.routing.base.GreedyRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.base.Router;
import edu.upc.cnds.collectives.routing.base.RoutingAlgorithm;
import edu.upc.cnds.collectives.routing.epidemic.EpidemicRoutingAlgorithm;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.base.RandomTopology;
import edu.upc.cnds.collectives.topology.distance.DistanceSpace;
import edu.upc.cnds.collectives.topology.distance.DistanceTopology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.overlay.OverlayFactory;
/**
 * Encapsulates the general algorithm to create an Epidemic Overlay formed by a Distance based topology
 * and a random topology. The subclasses must provide the construction of a routing
 * 
 * @author Pablo Chacin
 *
 */
public class EpidemicOverlayFactory implements OverlayFactory {

	int distanceViewSize;
	
	int randomViewSize;
	
	int distanceViewExchangeSize;
	
	int radomViewExchangeSize;
	
	int ttl;
	
	DistanceSpace space;
	
	MatchFunction function;
	
	public EpidemicOverlayFactory(int distanceViewSize, int distanceViewExchangeSize, 
			int randomViewSize,int radomViewExchangeSize, int ttl,
			DistanceSpace space,MatchFunction function) {
		super();
		this.distanceViewSize = distanceViewSize;
		this.randomViewSize = randomViewSize;
		this.distanceViewExchangeSize = distanceViewExchangeSize;
		this.radomViewExchangeSize = radomViewExchangeSize;
		this.ttl = ttl;
		this.space = space;
		this.function = function;

	}



	@Override
	public Overlay getOverlay(UnderlayNode node) {
		
		
				
		Topology topology = new DistanceTopology(node,null,distanceViewSize,false,space);
		
		RoutingAlgorithm epidemicRouting = new EpidemicRoutingAlgorithm(topology,distanceViewExchangeSize);
		MatchFunction epidemicMatchFunction = new DummyMatch(1.0);
		Routing updateRouter = new GenericRouter("TopologyUpdateRouter",node,epidemicMatchFunction,epidemicRouting,node.getTransport());
				
				
		Topology randomTopology = new RandomTopology(node,randomViewSize,false);
		//Topology randomTopology = new BasicTopology(node,seeds,new OrderedSelector(new AgeComparator(node,cycleLength)),randomViewSize,false);
		RoutingAlgorithm randomEpidemic = new EpidemicRoutingAlgorithm(randomTopology,distanceViewExchangeSize);
		Routing randomUpdater =new GenericRouter("topology.updater",node,epidemicMatchFunction,randomEpidemic,node.getTransport());

		
		//RoutingAlgorithm utilityRouting = new GradientNeighborRoutingAlgorithm(topology,randomTopology,routingFuction,
		//		                                                                 exchangeSet,overlaySelector);
		//RoutingAlgorithm utilityRouting = new GradientRoutingAlgorithmWithMemory(topology,randomTopology,routingFuction,
		//		                                                                 exchangeSet,overlaySelector);
		//RoutingAlgorithm utilityRouting = new GradientRoutingAlgorithm(topology,randomTopology,new DecayUtilityMatchFunction(node,"utility",cycleLength,alpha));
		//RoutingAlgorithm utilityRouting = new GradientRoutingAlgorithm(topology,randomTopology,new UtilityMatchFunction("utility"));
		//RoutingAlgorithm utilityRouting = new ProbabilisticRoutingAlgorithm(topology,routingFuction);
		//RoutingAlgorithm utilityRouting = new EpidemicRoutingAlgorithm(topology,1);
		//RoutingAlgorithm utilityRouting = new GreedyRoutingAlgorithm(topology,new DecayUtilityMatchFunction("utility",cycleLength,alpha));
		//RoutingAlgorithm utilityRouting = new GreedyRoutingAlgorithm(topology,new UtilityMatchFunction("utility"));

		RoutingAlgorithm algorithm = new GreedyRoutingAlgorithm(topology.getTopology(),function);
		
		Router router = new GenericRouter("overlay.router",node,function,algorithm,node.getTransport());
		
		Overlay overlay = new GradientOverlay(node,topology,router,updateRouter,new Destination(new HashMap(),false),1,
				                             randomTopology, randomUpdater, new Destination(new HashMap(),false), distanceViewExchangeSize);
					
		return overlay;

	}
	
	
}

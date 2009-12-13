package edu.upc.cnds.collectivesim.overlay.epidemic;

import java.util.HashMap;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.gradient.GradientOverlay;
import edu.upc.cnds.collectives.overlay.gradient.UtilityMatchFunction;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.MatchFunction;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.base.DummyMatch;
import edu.upc.cnds.collectives.routing.base.GenericRouter;
import edu.upc.cnds.collectives.routing.base.GreedyRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.base.Router;
import edu.upc.cnds.collectives.routing.base.RoutingAlgorithm;
import edu.upc.cnds.collectives.routing.epidemic.EpidemicRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.gradient.GradientRoutingAlgorithm;
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
		Routing updateRouter = new GenericRouter("TopologyUpdateRouter",node,new DummyMatch(1.0),epidemicRouting,node.getTransport());
				
				
		Topology randomTopology = new RandomTopology(node,randomViewSize,false);
		RoutingAlgorithm randomEpidemic = new EpidemicRoutingAlgorithm(randomTopology,radomViewExchangeSize);
		Routing randomUpdater =new GenericRouter("topology.updater",node,new DummyMatch(1.0),randomEpidemic,node.getTransport());

		
		//RoutingAlgorithm utilityRouting = new GradientNeighborRoutingAlgorithm(topology,randomTopology,routingFuction,
		//		                                                                 exchangeSet,overlaySelector);
		//RoutingAlgorithm utilityRouting = new GradientRoutingAlgorithmWithMemory(topology,randomTopology,routingFuction,
		//		                                                                 exchangeSet,overlaySelector);
		//RoutingAlgorithm utilityRouting = new GradientRoutingAlgorithm(topology,randomTopology,new UtilityMatchFunction("utility"));
		//RoutingAlgorithm utilityRouting = new ProbabilisticRoutingAlgorithm(topology,routingFuction);
		//RoutingAlgorithm utilityRouting = new EpidemicRoutingAlgorithm(topology,1);
		//RoutingAlgorithm utilityRouting = new GreedyRoutingAlgorithm(topology,new DecayUtilityMatchFunction("utility",cycleLength,alpha));
		//RoutingAlgorithm utilityRouting = new GreedyRoutingAlgorithm(topology,new UtilityMatchFunction("utility"));

		
		RoutingAlgorithm algorithm = new GreedyRoutingAlgorithm(topology,function);
		//RoutingAlgorithm algorithm = new GradientRoutingAlgorithm(topology,randomTopology,function);

		
		Router router = new GenericRouter("overlay.router",node,new UtilityMatchFunction("utility"),algorithm,node.getTransport(),false,ttl);
		
		Overlay overlay = new GradientOverlay(node,topology,router,updateRouter,new Destination(new HashMap(),false),1,
				                             randomTopology, randomUpdater, new Destination(new HashMap(),false), distanceViewExchangeSize);
					
		return overlay;

	}
	
	
}

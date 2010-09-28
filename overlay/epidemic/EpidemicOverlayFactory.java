package edu.upc.cnds.collectivesim.overlay.epidemic;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.epidemic.EpidemicOverlay;
import edu.upc.cnds.collectives.routing.AdmissionFunction;
import edu.upc.cnds.collectives.routing.RankFunction;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.base.GenericRouter;
import edu.upc.cnds.collectives.routing.base.GreedyRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.base.ProbabilisticRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.base.RoundRobinRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.base.Router;
import edu.upc.cnds.collectives.routing.base.RoutingAlgorithm;
import edu.upc.cnds.collectives.routing.base.TwoChoicesRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.epidemic.EpidemicRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.utility.AcceptanceRatioRankFunction;
import edu.upc.cnds.collectives.routing.utility.AttributeRankFunction;
import edu.upc.cnds.collectives.routing.utility.CapacityRankFunction;
import edu.upc.cnds.collectives.routing.utility.RealCapacityRankFunction;
import edu.upc.cnds.collectives.routing.utility.ToleranceRestrictedAdmissionFunction;
import edu.upc.cnds.collectives.topology.Topology;
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
		
	int distanceViewExchangeSize;
	
	int ttl;
	
	DistanceSpace space;
	
	
	public EpidemicOverlayFactory(int distanceViewSize, int distanceViewExchangeSize, int ttl,
			DistanceSpace space) {
		super();
		this.distanceViewSize = distanceViewSize;
		this.distanceViewExchangeSize = distanceViewExchangeSize;
		this.ttl = ttl;
		this.space = space;

	}



	@Override
	public Overlay getOverlay(UnderlayNode node) {
		
		
				
		Topology topology = new DistanceTopology(node,null,distanceViewSize,false,space);	
		//Topology topology = new AdaptiveDistanceTopology(node,null,distanceViewSize,false,space);
		
		
		RoutingAlgorithm epidemicRouting = new EpidemicRoutingAlgorithm(topology,distanceViewExchangeSize);
		Routing updateRouter = new GenericRouter("TopologyUpdateRouter",node,epidemicRouting,node.getTransport(),1);
				
		AdmissionFunction admission = new ToleranceRestrictedAdmissionFunction();
		
		//RankFunction ranking = new UtilityDistanceRankFunction();
		//RankFunction ranking = new UtilityRankFunction();	
		//RankFunction ranking = new AttributeRankFunction("service.acceptance.rate");
		//RankFunction ranking = new RealCapacityRankFunction();
		RankFunction ranking = new CapacityRankFunction();
		//RankFunction ranking = new AcceptanceRatioRankFunction();
		
		//RoutingAlgorithm algorithm = new GreedyRoutingAlgorithm(topology,ranking);
		//RoutingAlgorithm algorithm = new ProbabilisticRoutingAlgorithm(topology,ranking);
		//RoutingAlgorithm algorithm = new TwoChoicesRoutingAlgorithm(topology,ranking);
		RoutingAlgorithm algorithm = new RoundRobinRoutingAlgorithm(topology);
		//RoutingAlgorithm algorithm = new RandomRoutingAlgorithm(topology);

		Router router = new GenericRouter("overlay.router",node,admission,algorithm,node.getTransport(),false,ttl);
		//Router router = new AdativeRouter("overlay.router",node,admission,algorithm,node.getTransport(),false,ttl,(AdaptiveTopology) topology);
		
		Overlay overlay = new EpidemicOverlay(node,topology,updateRouter,router);
					
		return overlay;

	}
	
	
}

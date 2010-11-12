package edu.upc.cnds.collectivesim.overlay.epidemic;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayException;
import edu.upc.cnds.collectives.overlay.OverlayHandler;
import edu.upc.cnds.collectives.overlay.base.BasicDiscoveryProtocol;
import edu.upc.cnds.collectives.overlay.base.DiscoveryProtocol;
import edu.upc.cnds.collectives.overlay.base.OverlayNode;
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
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayException;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectives.util.RandomSelector;
import edu.upc.cnds.collectivesim.overlay.OverlayFactory;
/**
 * Encapsulates the general algorithm to create an Epidemic Overlay formed by a Distance based topology
 * and a random topology. The subclasses must provide the construction of a routing
 * 
 * @author Pablo Chacin
 *
 */
public class EpidemicOverlayFactory implements OverlayFactory {

	int viewSize;
		
	int exchangeSize;
	
	int ttl;
	
	DistanceSpace space;
	
	String name;
	
	Underlay underlay;
	
	public EpidemicOverlayFactory(String name,Underlay underlay, int viewSize, int exchangeSize, int ttl,
			DistanceSpace space) {
		super();
		this.name = name;
		this.viewSize = viewSize;
		this.exchangeSize = exchangeSize;
		this.ttl = ttl;
		this.space = space;
		this.underlay =underlay;

	}



	@Override
	public Overlay getOverlay(Identifier id) throws OverlayException {
		
		
		UnderlayNode underlayNode;
		try {
			underlayNode = underlay.createNode();
		} catch (UnderlayException e) {
			throw new OverlayException("Unable to create Underlay node",e);
		}
		
		OverlayNode node = new OverlayNode(id,underlayNode);
		
		Topology topology = new DistanceTopology(node,null,viewSize,false,space);	
		//Topology topology = new AdaptiveDistanceTopology(node,null,distanceViewSize,false,space);
		
		
		RoutingAlgorithm epidemicRouting = new EpidemicRoutingAlgorithm(topology,exchangeSize);
		Routing updateRouter = new GenericRouter("TopologyUpdateRouter",node,epidemicRouting,1);
				
		AdmissionFunction admission = new ToleranceRestrictedAdmissionFunction();
		
		//RankFunction ranking = new UtilityDistanceRankFunction();
		//RankFunction ranking = new UtilityRankFunction();	
		RankFunction ranking = new AttributeRankFunction("service.acceptance.rate");
		//RankFunction ranking = new RealCapacityRankFunction();
		//RankFunction ranking = new CapacityRankFunction();
		//RankFunction ranking = new AcceptanceRatioRankFunction();
		
		//RoutingAlgorithm algorithm = new GreedyRoutingAlgorithm(topology,ranking);
		RoutingAlgorithm algorithm = new ProbabilisticRoutingAlgorithm(topology,ranking);
		//RoutingAlgorithm algorithm = new TwoChoicesRoutingAlgorithm(topology,ranking);
		//RoutingAlgorithm algorithm = new RoundRobinRoutingAlgorithm(topology);
		//RoutingAlgorithm algorithm = new RandomRoutingAlgorithm(topology);

		Router router = new GenericRouter(name+".router",node,admission,algorithm,false,ttl);
		//Router router = new AdativeRouter("overlay.router",node,admission,algorithm,node.getTransport(),false,ttl,(AdaptiveTopology) topology);
		
		DiscoveryProtocol discovery = new BasicDiscoveryProtocol(name+".discovery", topology, new RandomSelector());
		
		Overlay overlay = new EpidemicOverlay(name, node,topology,updateRouter,discovery, router);
					
		return overlay;

	}



	@Override
	public Overlay getOverlay(Identifier id,OverlayHandler handler) throws OverlayException {
		Overlay overlay = getOverlay(id);
		overlay.setHandler(handler);
		return overlay;
	}
	
	
}

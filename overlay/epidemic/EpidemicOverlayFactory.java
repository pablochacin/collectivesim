package edu.upc.cnds.collectivesim.overlay.epidemic;

import java.util.HashMap;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.epidemic.EpidemicOverlay;
import edu.upc.cnds.collectives.routing.AdmissionFunction;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.RankFunction;
import edu.upc.cnds.collectives.routing.Routing;
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
	
	AdmissionFunction admission;
	
	RankFunction ranking;
	
	public EpidemicOverlayFactory(int distanceViewSize, int distanceViewExchangeSize, 
			int randomViewSize,int radomViewExchangeSize, int ttl,
			DistanceSpace space,AdmissionFunction admission,RankFunction ranking) {
		super();
		this.distanceViewSize = distanceViewSize;
		this.randomViewSize = randomViewSize;
		this.distanceViewExchangeSize = distanceViewExchangeSize;
		this.radomViewExchangeSize = radomViewExchangeSize;
		this.ttl = ttl;
		this.space = space;
		this.admission= admission;
		this.ranking = ranking;

	}



	@Override
	public Overlay getOverlay(UnderlayNode node) {
		
		
				
		Topology topology = new DistanceTopology(node,null,distanceViewSize,false,space);	
		//Topology topology = new AdaptiveDistanceTopology(node,null,distanceViewSize,false,space);
		
		
		RoutingAlgorithm epidemicRouting = new EpidemicRoutingAlgorithm(topology,distanceViewExchangeSize);
		Routing updateRouter = new GenericRouter("TopologyUpdateRouter",node,epidemicRouting,node.getTransport(),1);
				
						
		RoutingAlgorithm algorithm = new GreedyRoutingAlgorithm(topology,ranking);
		//RoutingAlgorithm algorithm = new EpidemicRoutingAlgorithm(topology,1);

		
		Router router = new GenericRouter("overlay.router",node,admission,algorithm,node.getTransport(),false,ttl);
		//Router router = new AdativeRouter("overlay.router",node,function,algorithm,node.getTransport(),false,ttl,(AdaptiveTopology)topology);
		
		Overlay overlay = new EpidemicOverlay(node,topology,router,updateRouter);
					
		return overlay;

	}
	
	
}

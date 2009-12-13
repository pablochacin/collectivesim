package edu.upc.cnds.collectivesim.overlay.random;

import java.util.HashMap;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.base.BasicOverlay;
import edu.upc.cnds.collectives.overlay.epidemic.EpidemicOverlay;
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
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.base.RandomTopology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.overlay.OverlayFactory;
/**
 * Encapsulates the general algorithm to create an Epidemic Overlay formed by a Distance based topology
 * and a random topology. The subclasses must provide the construction of a routing
 * 
 * @author Pablo Chacin
 *
 */
public class RandomOverlayFactory implements OverlayFactory {

	int randomViewSize;
	
	int radomViewExchangeSize;
	
	int ttl;
		
	MatchFunction function;
	
	public RandomOverlayFactory(int randomViewSize,int radomViewExchangeSize, int ttl,
								MatchFunction function) {
		super();
		this.randomViewSize = randomViewSize;
		this.radomViewExchangeSize = radomViewExchangeSize;
		this.ttl = ttl;
		this.function = function;

	}



	@Override
	public Overlay getOverlay(UnderlayNode node) {
		
		
						
		Topology randomTopology = new RandomTopology(node,randomViewSize,false);
		RoutingAlgorithm randomEpidemic = new EpidemicRoutingAlgorithm(randomTopology,radomViewExchangeSize);
		Router updateRouter = new GenericRouter("random.updater",node,new DummyMatch(1.0),randomEpidemic,node.getTransport());
	
		
		RoutingAlgorithm algorithm = new EpidemicRoutingAlgorithm(randomTopology,1);
		//RoutingAlgorithm algorithm = new GreedyRoutingAlgorithm(randomTopology,new UtilityMatchFunction("utility"));
		
		Router router = new GenericRouter("overlay.router",node,function,algorithm,node.getTransport(),false,ttl);
		
		Overlay overlay = new EpidemicOverlay(node,randomTopology,router,updateRouter,new Destination(new HashMap()),1);
					
		return overlay;

	}
	
	
	
	
}

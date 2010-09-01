package edu.upc.cnds.collectivesim.overlay.random;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.epidemic.EpidemicOverlay;
import edu.upc.cnds.collectives.routing.AdmissionFunction;
import edu.upc.cnds.collectives.routing.base.GenericRouter;
import edu.upc.cnds.collectives.routing.base.Router;
import edu.upc.cnds.collectives.routing.base.RoutingAlgorithm;
import edu.upc.cnds.collectives.routing.epidemic.EpidemicRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.epidemic.RandomRoutingAlgorithm;
import edu.upc.cnds.collectives.routing.utility.ToleranceRestrictedAdmissionFunction;
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
		
	public RandomOverlayFactory(int randomViewSize,int radomViewExchangeSize, int ttl){
		super();
		this.randomViewSize = randomViewSize;
		this.radomViewExchangeSize = radomViewExchangeSize;
		this.ttl = ttl;

	}



	@Override
	public Overlay getOverlay(UnderlayNode node) {
		
		AdmissionFunction admission = new ToleranceRestrictedAdmissionFunction();
		
						
		Topology randomTopology = new RandomTopology(node,randomViewSize,false);
		RoutingAlgorithm randomEpidemic = new RandomRoutingAlgorithm(randomTopology);
		Router updateRouter = new GenericRouter("random.updater",node,randomEpidemic,node.getTransport(),1);
	
		
		RoutingAlgorithm algorithm = new EpidemicRoutingAlgorithm(randomTopology,1);
		//RoutingAlgorithm algorithm = new GreedyRoutingAlgorithm(randomTopology,new UtilityMatchFunction("utility"));
		
		Router router = new GenericRouter("overlay.router",node,admission,algorithm,node.getTransport(),false,ttl);
		
		Overlay overlay = new EpidemicOverlay(node,randomTopology,updateRouter,router);
					
		return overlay;

	}
	
	
	
	
}

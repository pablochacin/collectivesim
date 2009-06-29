package edu.upc.cnds.collectivesim.overlay.gradient;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingHandler;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;

public class GradientOverlayProviderAgent extends GradientOverlayServiceAgent  implements RoutingHandler {

	
	UtilityFunction function;
	
	protected Vector<Double>requests;

	protected Vector<Double>loadHistory;
	
	public GradientOverlayProviderAgent(OverlayModel model, Overlay overlay,
			Routing router, Topology randomTopology, Double utility,String role,
			UtilityFunction function) {
			
			super(model, overlay, router, randomTopology, utility,role);

			this.requests = new Vector<Double>();
			
			this.loadHistory = new Vector<Double>(10);
					
			this.overlay.addRoutingHandler(this);
			
			this.function = function;
			
			overlay.getLocalNode().getAttributes().put("load",new Double(0.0));
			overlay.getLocalNode().getAttributes().put("capacity",new Double(utility));
	}
	
		

	public void updateUtility(){
		
		Collections.sort(requests);
		Iterator<Double> iter = requests.iterator();
		
		//remove all the expired requests
		while(iter.hasNext() && (iter.next() <= (double)model.getCurrentTime())){
			iter.remove();
		}
							

		Double currentLoad = (double)requests.size();
				
		loadHistory.add(currentLoad);
		if(loadHistory.size() > 10){
			loadHistory.remove(0);
		}

		Double avgLoad = 0.0;
		for(Double u: loadHistory){
			avgLoad =+ u;
		}
		
		avgLoad = avgLoad/loadHistory.size();
		
		overlay.getLocalNode().getAttributes().put("load",avgLoad);

		
		setUtility(function.getUtility(overlay.getLocalNode()));

	}

	

	@Override
	public void deliver(Routing router, Destination destination, Node source,
			Serializable... args) {
		
		Double duration = (Double) args[0];
		
		requests.add(model.getCurrentTime()+duration);
				
		updateUtility();
	}

	
	public Double getRequests() {
		return new Double(requests.size());
	}
	
	
	


	

}

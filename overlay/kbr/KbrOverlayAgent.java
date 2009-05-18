package edu.upc.cnds.collectivesim.overlay.kbr;

import java.io.Serializable;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.state.Counter;

public class KbrOverlayAgent extends OverlayAgent {

	protected Counter routedCounter;
	
	protected Counter droppedCounter;
	
	public KbrOverlayAgent(OverlayModel model, Overlay overlay) {
		super(model, overlay);
		this.routedCounter = model.getExperiment().getCounter("routing.routed");		
		this.droppedCounter = model.getExperiment().getCounter("routing.dropped");
	}

	
	@Override
	public void routed(Routing router, Node node, Destination destination,
			Route route, Serializable... args) {
		
		super.routed(router, node, destination, route, args);
		routedCounter.increment();
	}


	@Override
	public void dropped(Routing router, Destination destination,
			Exception cause, Serializable... args) {
		
		super.dropped(router, destination, cause, args);
		
		droppedCounter.increment();
	}
}

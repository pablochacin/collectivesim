package edu.upc.cnds.collectivesim.overlay.gradient;

import java.io.Serializable;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.MatchFunction;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;

public class GradientOverlayServiceAgent extends GradientOverlayAgent {

	
	public GradientOverlayServiceAgent(OverlayModel model, Overlay overlay,
			Routing router, Topology randomTopology,Double utility,String role) {
		
		super(model, overlay, router, randomTopology,utility);
		

	}

	public String getRole(){
		return (String)overlay.getLocalNode().getAttributes().get("role");
	}
	
	
	@Override
	public void dropped(Routing router, Destination destination, Route route,
			Exception cause, Serializable... args) {
		super.dropped(router, destination, route, cause, args);
		
		/*
		System.out.println("------------");
		System.out.println("Destination: " + FormattingUtils.mapToString(destination.getAttributes()));
		System.out.println("Tolerance: " +destination.getTolerance());
		for(Node n: route.getHops()){
			System.out.println(FormattingUtils.mapToString(n.getAttributes()));
		}
		*/
	}
}

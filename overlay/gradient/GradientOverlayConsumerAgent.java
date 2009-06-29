package edu.upc.cnds.collectivesim.overlay.gradient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingException;
import edu.upc.cnds.collectives.routing.base.GenericRouter;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;


public class GradientOverlayConsumerAgent extends GradientOverlayServiceAgent {

	protected Integer ttl;
	
	protected Double tolerance;
	

	
	/*
	 * Role of the Agent
	 */
	protected String role; 
	
	public GradientOverlayConsumerAgent(OverlayModel model, Overlay overlay,
			Routing router, Topology randomTopology, Double utility,String role,
			Double tolerance, Integer ttl) {

			super(model, overlay, router, randomTopology,utility,role);
			
			this.tolerance = tolerance;
			this.ttl = ttl;

	}

	
	/**
	 * Make a request for service with a given duration
	 * 
	 * Forwards a a requests with the consumer's minimum required utility
	 * tolerance and ttl.
	 * 
	 * @param duration
	 */
	public void makeRequest(Double duration,Object...args){
		makeRequest(getUtility(),tolerance,ttl,duration,args);
	}
	
	
	public void makeRequest(Double duration){
		makeRequest(getUtility(),tolerance,ttl,duration);
	}
	
	/**
	 * Makes a request for service with a minimum utility. 
	 * 
	 * @param utility required utility
	 * @param tolerance tolerance (above)
	 * @param ttl maximum ttl before dropping the request
	 * @param args args for the request
	 */
	public void makeRequest(Double utility, Double tolerance,
			Integer ttl,Serializable...args) {

		Map attributes = new HashMap();
		attributes.put("utility", utility);
		Destination destination = new Destination(attributes,tolerance);
		try {
			overlay.route(destination, ttl,args);
		} catch (RoutingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Double getUtility(){
		return utility;
	}
	
	
	public void updateUtility(){
		overlay.getLocalNode().touch(model.getCurrentTime());
	}


	@Override
	public void delivered(Routing router, Destination destination, Route route,
			Serializable... args) {		
		
		//forward the request
		route.consumers++;
		((GenericRouter)router).route(destination, overlay.getLocalNode(), route, args);

		//dropped(router,destination,route,new Exception("routed to a entry point"), args);
	}
	
	
}

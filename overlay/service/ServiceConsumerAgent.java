package edu.upc.cnds.collectivesim.overlay.service;

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


public class ServiceConsumerAgent extends ServiceOverlayAgent {

	
	/**
	 * request tolerance
	 */
	protected Double tolerance;
	
	/**
	 * Preferred target utility
	 */
	protected Double preference;
	
	/**
	 * Default duration for requests
	 */
	protected Long duration;
	
	/**
	 * 
	 * @param model
	 * @param overlay
	 * @param router
	 * @param randomTopology
	 * @param utility
	 * @param role
	 * @param tolerance
	 * @param ttl
	 * @param preference
	 */
	
	
	public ServiceConsumerAgent(OverlayModel model, Overlay overlay,
			Routing router, Topology randomTopology, Double utility,String role,
			Double tolerance, Integer ttl,Double preference,long duration) {

			super(model, overlay, router, ttl,randomTopology,utility,role);
			
			this.tolerance = tolerance;	
			
			this.preference = preference;
			
			this.duration = duration;

	}


	/**
	 * Convenience constructor, initializes the agent with a default request utility (its preference)
	 * equals as its initial utility and a request duration of 1 (one cycle)
	 * 
	 * @param model
	 * @param overlay
	 * @param router
	 * @param randomTopology
	 * @param utility
	 * @param role
	 * @param tolerance
	 * @param ttl
	 */
	public ServiceConsumerAgent(OverlayModel model, Overlay overlay,
			Routing router, Topology randomTopology, Double utility,String role,
			Double tolerance, Integer ttl) {

		
		 	this(model,overlay,router,randomTopology,utility,role,tolerance,ttl,utility,1);
	}	

	/**
	 * Make a request with the agent's preferred target utility
	 */
	public void makeRequest(){
		makeRequest(preference,tolerance,duration);
	}
		

	@Override
	/**
	 * as the consumer doesn't process requests, re-route any incoming request
	 */
	public boolean delivered(Routing router, Destination destination, Route route,
			Serializable message) {		
		
			return false;
	}
	
	
	/**
	 * Set the preferred utility for this consumer requests
	 * 
	 * @param preference
	 */
	public void setPreference(Double preference){
		this.preference = preference;
	}
	
}

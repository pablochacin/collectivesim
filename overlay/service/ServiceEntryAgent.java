package edu.upc.cnds.collectivesim.overlay.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingException;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityOverlayAgent;

/**
 * A service agent that only generates requests
 * 
 * @author Pablo Chacin
 *
 */
public class ServiceEntryAgent extends UtilityOverlayAgent {

	
	
	/**
	 * Preferred target utility
	 */
	protected Double preference;
			
	/**
	 * 
	 * @param model
	 * @param overlay
	 * @param utility
	 * @param role
	 * @param preference
	 */
	public ServiceEntryAgent(OverlayModel model, Overlay overlay,Identifier id,Double utility,Double preference) {

			super(model, overlay,id,utility);
						
			this.preference = preference;
			
	}

	
	/**
	 * Make a request with the agent's preferred target utility
	 */
	public void makeRequest(Double tolerance,Long duration){
		makeRequest(preference,tolerance,duration);
	}
		
	/**
	 * Makes a request for service with a minimum utility. 
	 * 
	 * @param utility required utility
	 * @param tolerance tolerance (above)
	 * @param duration duration of the request execution
	 */
	public void makeRequest(Double utility,Double tolerance,Long duration) {

		Map attributes = new HashMap();
		attributes.put("utility", utility);
		Destination destination = new Destination(attributes,tolerance);
		
		
		ServiceRequest request = new ServiceRequest(utility,duration);
		
		try {
			overlay.route(destination, request);
		} catch (RoutingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	
	
	public Double getUtility(){
		return preference;
	}
	

	public void update(){
		super.update();
	}
	
	
	public void join(){
		super.join();
	}
}

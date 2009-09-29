package edu.upc.cnds.collectivesim.overlay.service;

import java.io.Serializable;
import java.util.Map;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;

/**
 * A service agent that only generates requests
 * 
 * @author Pablo Chacin
 *
 */
public class ServiceEntryAgent extends ServiceOverlayAgent {

	
	
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
	public ServiceEntryAgent(OverlayModel model, Overlay overlay,Map attributes) {

			super(model, overlay,attributes);
						
			Double preference = (Double)attributes.get("preference");
			if(preference == null){
				throw new IllegalArgumentException("Preference attribute not specified");
			}
			
			this.preference = preference;
			
	}

	
	/**
	 * Make a request with the agent's preferred target utility
	 */
	public void makeRequest(Double tolerance,Long duration){
		super.makeRequest(preference,tolerance,duration);
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
}

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
import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.utility.FixedUtilityFunction;
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
	
	protected Double tolerance;
				
	/**
	 * 
	 * @param model
	 * @param overlay
	 * @param utility
	 * @param role
	 * @param preference
	 */
	public ServiceEntryAgent(OverlayModel model, Overlay overlay,Identifier id,Double preference,Double tolerance) {

			super(model, overlay,id, new FixedUtilityFunction(preference));
						
			this.preference = preference;
			
			this.tolerance = tolerance;
	}

		
	/**
	 * Makes a request for service with a minimum utility. 
	 * 
	 * @param utility required utility
	 * @param tolerance tolerance (above)
	 * @param duration duration of the request execution
	 */
	public void makeRequest(Double serviceDemand) {

		Map attributes = new HashMap();
		attributes.put("utility", preference);
		attributes.put("tolerance", tolerance);		
		attributes.put("role", "entry");
		
		Destination destination = new Destination(attributes);
		
		if(preference == 0.4) {
			System.out.print("");
		}
		
		ServiceRequest request = new ServiceRequest(preference,serviceDemand);
		
		try {
			overlay.route(destination, request);
		} catch (RoutingException e) {
			log.warning("unable to route request " + FormattingUtils.getStackTrace(e));
		}

	}
	
	public void startDebug() {
		System.out.print("");
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

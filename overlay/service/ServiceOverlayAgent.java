package edu.upc.cnds.collectivesim.overlay.service;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.RoutingException;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityOverlayAgent;
import edu.upc.cnds.collectivesim.state.Counter;

public class ServiceOverlayAgent extends UtilityOverlayAgent {
	
	/**
	 * Role of the agent
	 */
	protected String role;
	
	/**
	 * Counts the number of requests received by this agent
	 */
	protected Counter requests; 
	
	/**
	 * 
	 * @param model
	 * @param overlay
	 * @param attributes
	 */
	public ServiceOverlayAgent(OverlayModel model, Overlay overlay,Map attributes) {
		
		super(model, overlay,attributes);
				
		this.role = (String)attributes.get("role");
		
		overlay.getLocalNode().getAttributes().put("role",role);
		
		requests = model.getExperiment().getCounter("service.requests");
	}


	public String getRole(){
		return (String)overlay.getLocalNode().getAttributes().get("role");
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
	
	
	/**
	 * Processes a service request
	 * 
	 * Default implementation does nothing
	 * 
	 * @param request
	 */
	protected void processRequest(ServiceRequest request){
		
		requests.increment();
	}
	
	
	public Double getRequests(){
		return requests.getValue();
	}
	
}

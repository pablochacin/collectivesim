package edu.upc.cnds.collectivesim.overlay.service;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingException;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityOverlayAgent;
import edu.upc.cnds.collectivesim.state.Counter;

public class ServiceOverlayAgent extends UtilityOverlayAgent {

	/**
	 * default request ttl
	 */
	protected Integer ttl;

	
	/**
	 * Counts the number of requests received by this agent
	 */
	protected Counter requests; 
	
	public ServiceOverlayAgent(OverlayModel model, Overlay overlay,
			Routing router, int ttl,Topology randomTopology,Double utility,String role) {
		
		super(model, overlay, router, randomTopology,utility);
		
		this.ttl = ttl;
		
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
	 * @param ttl maximum ttl before dropping the request
	 * @param duration duration of the request execution
	 */
	public void makeRequest(Double utility,Double tolerance,Long duration) {

		Map attributes = new HashMap();
		attributes.put("utility", utility);
		Destination destination = new Destination(attributes,tolerance);
		
		ServiceRequest request = new ServiceRequest(utility,duration);
		
		try {
			overlay.route(destination, ttl,request);
		} catch (RoutingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	/**
	 * Processes a servcie request
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

package edu.upc.cnds.collectivesim.overlay.service;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityOverlayAgent;
import edu.upc.cnds.collectivesim.state.Counter;

public class ServiceProviderAgent extends UtilityOverlayAgent {
	
	
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
	public ServiceProviderAgent(OverlayModel model, Overlay overlay,
							   Identifier id,Double utility) {
		
		super(model, overlay,id,utility);
						
		requests = model.getExperiment().getCounter("service.requests").getChild();
	}
	
	
	/**
	 * Processes a service request
	 * 
	 * Default implementation does nothing
	 * 
	 * @param request
	 */
	protected void processRequest(ServiceRequest request){
		
		if(!active){
			log.severe("Processing request in an inactive node");
		}
		requests.increment();
	}
	
	
	public Double getRequests(){
		return requests.getValue();
	}
	
}

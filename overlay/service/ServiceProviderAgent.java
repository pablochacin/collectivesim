package edu.upc.cnds.collectivesim.overlay.service;

import java.io.Serializable;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingHandler;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityOverlayAgent;
import edu.upc.cnds.collectivesim.state.Counter;

public class ServiceProviderAgent extends UtilityOverlayAgent implements RoutingHandler{
	
	
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
							   Identifier id,UtilityFunction function) {
		
		super(model, overlay,id,function);
						
		overlay.addRoutingHandler(this);
		
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


	@Override
	public void deliver(Routing router, Destination destination, Node source,
			Serializable message) {
		
		processRequest((ServiceRequest)message);
		
	}
	
}

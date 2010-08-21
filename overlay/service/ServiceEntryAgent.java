package edu.upc.cnds.collectivesim.overlay.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingException;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
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
			
			overlay.getLocalNode().getAttributes().put("role", "entry");
			
			setUtility(0.0);
	}

		
	/**
	 * Makes a request for service with a minimum utility. 
	 * 
	 * @param utility required utility
	 * @param demand duration of the request execution
	 */
	public void makeRequest(Double utility,Double serviceDemand) {

		Map attributes = new HashMap();
		attributes.put("utility", utility);
		attributes.put("tolerance", tolerance);		
		
		Destination destination = new Destination(attributes);
		
		
		ServiceRequest request = new ServiceRequest(utility,tolerance,serviceDemand);
		
		try {
			overlay.route(destination, request);
		} catch (RoutingException e) {
			log.warning("unable to route request " + FormattingUtils.getStackTrace(e));
		}

	}
	
	
	public void makeRequest(Double demand) {
		makeRequest(preference,demand);
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
	
	
	

	public void update(){

//		System.out.println(model.getCurrentTime() + "------- " + overlay.getLocalNode().getId().toString());
//		System.out.println("Neighbors " + FormattingUtils.collectionToString(overlay.getTopology().getCandidates()));
//		System.out.println("Candidates " + FormattingUtils.collectionToString(overlay.getTopology().getCandidates()));
		super.update();
//		System.out.println("Neighbors " + FormattingUtils.collectionToString(overlay.getTopology().getCandidates()));

	}
	
	
	public void join(){
		super.join();
	}
	
public Double getStaleness(){

	
		Model services = model.getExperiment().getModel("services");
		
		Double error = 0.0;
		for(Node n: overlay.getNodes()){

			//get the actual utility of the node,not the local value 
			ModelAgent neighbor = services.getAgent(n.getId().toString());
			if(neighbor != null){
				Double neighborUtility = (Double)n.getAttributes().get("service.capacity");
				Double difference;
				try {
					difference = + Math.abs(neighborUtility-(Double)(neighbor.getAttribute("Capacity")));
					error+= difference;
				} catch (ModelException e) {;}

			}
		}
				
		return error/(double)overlay.getTopology().getSize();

	}
}

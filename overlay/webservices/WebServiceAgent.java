package edu.upc.cnds.collectivesim.overlay.webservices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingEvent;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.service.ServiceProviderAgent;
import edu.upc.cnds.collectivesim.overlay.service.ServiceRequest;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;

/**
 * 
 * Simulates a web server that process service requests according to a M/G/1/k*PS model, that is
 * -Poisson arrival distribution
 * -General distribution of service time
 * - 1 server station
 * - a limit of k concurrent requests
 * - a Processor Sharing dispatch discipline
 * 
 * The model considers a background load that influences the actual service time of the requests.
 * This load can vary on each cycle.
 *
 * This model makes the following assumptions:
 *  - the system is in steady state, that is, the service rate is greater or equal than the arrival 
 *    rate
 *  - On each cycle at least one request is processed
 * 
 * @author Pablo Chacin
 *
 */
public class WebServiceAgent extends ServiceProviderAgent {

	
	private List<ServiceRequest> requests;
	
	private Double backgroundLoad;
	
	private Double averageServiceRequest;
	
	private Double arrivals = 0.0;
	
	private UtilityFunction function;
	
	private Integer queueLimit;
	
	
	public WebServiceAgent(OverlayModel model, Overlay overlay, Identifier id,
			Double utility, UtilityFunction function,Double averageServiceRequest,Integer requestLimit) {
		super(model, overlay, id, function);	
		
		this.averageServiceRequest = averageServiceRequest;
		this.queueLimit = requestLimit;
		this.function = function;
		
		requests = new ArrayList<ServiceRequest>(requestLimit);
		
	}


	@Override
	/**
	 * Count the requests received in the current dispatch cycle
	 */
	protected void processRequest(ServiceRequest request) {
		//count request
		super.processRequest(request);
		
		requests.add(request);
		arrivals++;

	}
	
	
	
	
	@Override
	/**
	 * Check if the request must be rejected as the server has a fixed capacity
	 * 
	 */
	public boolean delivered(Routing router, Destination destination,
			Route route, Serializable message) {
		if(requests.size() < queueLimit){
			return super.delivered(router, destination, route, message);
		}
		else{
			return false;
		}
	}


	public void setBackgroundLoad(Double load){
		backgroundLoad = load;
	}
	
	
	public Double getBackgroundLoad(){
		return backgroundLoad;
	}
	
	/**
	 * Attend requests in the queue
	 */
	public void dispatchRequests(){
		
		//number of requests attended in the current dispatch cycle
		int servicedRequests = (int)Math.ceil(getServiceRate());
		
		Double serviceTime = getServiceTime();
		
		//update node's attribute
		overlay.getLocalNode().getAttributes().put("serviceTime",serviceTime);
		
		for(int r =0;r < servicedRequests;r++){
			ServiceRequest request = requests.remove(0);
			
			Map attributes = new HashMap();
			attributes.put("qos",request.getUtility());
			attributes.put("utility",getUtility());
			attributes.put("serviceTime",serviceTime);
			
			Event event = new ServiceEvent(overlay.getLocalNode(),model.getCurrentTime(),
					attributes);

			model.getExperiment().reportEvent(event);

		
		}
		
		//reset counter
		arrivals = 0.0;
	}

		
	/**
	 * @return the average service time based on the current arrival rate and the average service 
	 *         demand per request
	 */
	public Double getServiceTime(){
            
		Double serviceRate = getServiceRate();
		Double serviceTime = (Math.pow(serviceRate, queueLimit+1.0) * (queueLimit*serviceRate-queueLimit-1) + serviceRate)/
		                     (arrivals*(1-Math.pow(serviceRate, queueLimit))*(1-serviceRate));
		
		return serviceTime;
	}

	
	/**
	 * Calculates the current service rate based on the arrivals and the average service rate 
	 * adjusted with the background load.
	 * 
	 * @return
	 */
	Double getServiceRate(){
	   return arrivals*averageServiceRequest*(1-backgroundLoad);	
	}
	
	
	public Double getUtility(){
		
		return function.getUtility(overlay.getLocalNode());
		
	}
}

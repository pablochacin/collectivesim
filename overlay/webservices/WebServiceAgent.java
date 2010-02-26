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
	
	private Double backgroundLoad = 0.0;
		
	private Double arrivals = 0.0;
	
	private UtilityFunction function;
	
	private Integer queueLimit;
	
	private Double serviceRate;
	
	
	public WebServiceAgent(OverlayModel model, Overlay overlay, Identifier id,
			UtilityFunction function,Integer requestLimit,Double serviceRate) {
		super(model, overlay, id, function);	
		
		this.queueLimit = requestLimit;
		this.serviceRate = serviceRate;
		this.function = function;
		
		requests = new ArrayList<ServiceRequest>(requestLimit);
		
		overlay.getLocalNode().getAttributes().put("service.time", 0.0);
		
	}


	@Override
	/**
	 * Count the requests received in the current dispatch cycle
	 */
	protected void processRequest(ServiceRequest request) {

		if(getName().equals("0ccccccccccccccc")) {
			System.out.print("");
		}
		
		//count request
		super.processRequest(request);
		
		requests.add(request);
		arrivals++;
		
		//update utility to be sure the current utility is reflected
		//when routing
		Double serviceTime = getServiceTime();
		overlay.getLocalNode().getAttributes().put("service.time", serviceTime);
		updateUtility();

	}
	
	
	
	public Double getQueueLength() {
		return new Double(requests.size());
	}
	
	
	public Double getArrivals() {
		return arrivals;
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
	
	public void updateBackgroundLoad(Double variation) {
		if(variation >= 0) 
			backgroundLoad = Math.min(backgroundLoad +variation,1.0);
		else
			backgroundLoad = Math.max(backgroundLoad +variation,0.0);
		
		
	}
	
	public Double getBackgroundLoad(){
		return backgroundLoad;
	}
	
	/**
	 * Attend requests in the queue
	 */
	public void dispatchRequests(){
	
		if(getName().equals("0ccccccccccccccc")) {
			System.out.print("");
		}
		
		//number of requests attended in the current dispatch cycle
		int servicedRequests = Math.min((int)Math.ceil(1.0/serviceRate),requests.size());
		
		Double serviceTime = getServiceTime();
		
		//update node's attribute
		overlay.getLocalNode().getAttributes().put("service.time",serviceTime);
		
		for(int r=0;r < servicedRequests;r++){
			try {
			ServiceRequest request = requests.remove(0);
	
			Map attributes = new HashMap();
			attributes.put("qos",request.getQoS());
			attributes.put("serviceDemand",request.getServiceDemand());			
			attributes.put("utility",getUtility());
			attributes.put("serviceTime",serviceTime);
			
			Event event = new ServiceEvent(overlay.getLocalNode(),model.getCurrentTime(),
					attributes);

			model.getExperiment().reportEvent(event);
			} catch(IndexOutOfBoundsException e) {
				System.out.println();
			}
		
		}
		
		//reset counter
		arrivals = 0.0;
	}

		
	/**
	 * @return the average service time based on the current arrival rate and the average service 
	 *         demand per request
	 */
	public Double getServiceTime(){
            
		if(arrivals == 0) {
			return Double.NaN;
		}
		
		Double offeredDemand = getOfferedDemand();
		Double serviceTime = (Math.pow(offeredDemand, queueLimit+1.0) * (queueLimit*offeredDemand-queueLimit-1) + offeredDemand)/
		                     (arrivals*(1-Math.pow(offeredDemand, queueLimit))*(1-offeredDemand));
		
		return serviceTime;
	}

	
	/**
	 * Calculates the current service rate based on the arrivals and the average service rate 
	 * adjusted with the background load.
	 * 
	 * @return
	 */
	public Double getOfferedDemand(){
	   //return serviceRate*(1-backgroundLoad)*arrivals;
		return Math.min(serviceRate*(double)requests.size()+ backgroundLoad,1.0);
	}
	
	

	
	public Double getUtility(){
	
		return function.getUtility(overlay.getLocalNode());
		
	}
}

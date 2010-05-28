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
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.service.ServiceProviderAgent;
import edu.upc.cnds.collectivesim.overlay.service.ServiceRequest;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.state.Counter;

/**
 * 
 * Simulates a web server that process service requests using a PS discipline.
 * The simulation is based on the model for a M/G/k/PS, adapted to consider the
 * effect of a background load. 
 * 
 * The server operates in cycles, triggered by the dispatchRequests method. 
 * 
 * The requests received during a cycle are not directly processed, but are placed in a
 * waiting queue (entry queue). 
 * 
 * On each cycle, the server process the requests in its run queue using a PS discipline.
 * At the end, the requests from the entry queue are moved to the run queue.
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

	
	/**
	 * Requests being processed in the current simulation cycle
	 */
	private List<ServiceRequest> runQueue;

	/**
	 * Requests arrived in the current simulation cycle and waiting to enter the server
	 */
	private List<ServiceRequest> entryQueue;

	/**
	 * Percentage of the server's capacity occupied by an "external" workload 
	 */
	private Double backgroundLoad = 0.0;
			
	/**
	 * Function used to calculate the utility offered by the server
	 */
	private UtilityFunction function;
	
	/**
	 * Maximun number of requests accepted by the server on each cycle
	 */
	private Integer queueLimit;
	
	/**
	 * Nominal service capacity of the server (the inverse of its maximum throughput)
	 */
	private Double serviceRate;
	
	
	private Counter notAllocated;
	
	public WebServiceAgent(OverlayModel model, Overlay overlay, Identifier id,
			UtilityFunction function,Integer requestLimit,Double serviceRate) {
		super(model, overlay, id, function);	
		
		this.queueLimit = requestLimit;
		this.serviceRate = serviceRate;
		this.function = function;
		this.notAllocated = model.getExperiment().getCounter("service.not.allocated");
		
		entryQueue = new ArrayList<ServiceRequest>(requestLimit);

		runQueue = new ArrayList<ServiceRequest>(requestLimit);

		overlay.getLocalNode().getAttributes().put("service.time", 0.0);
		
	}

	
	

	@Override
	public void dropped(Routing router, Destination destination, Route route, Exception cause, Serializable message) {
		
		super.dropped(router, destination, route, cause, message);
		
		ServiceRequest request = (ServiceRequest)message;

		Integer count = 0;

		for(OverlayAgent a: model.getAgents()) {
			try {
				Double agentUtility = (Double)a.getAttribute("Utility");
				if(agentUtility >= request.getQoS()) {
					count++;
				}
			} catch (ModelException e) {
				
			}
			
		}
		
		if(count > 0){
			notAllocated.increment();
		}
	}


	

	@Override
	public void forwarded(Routing router, Destination destination, Route route,
			Serializable message) {
		super.forwarded(router, destination, route, message);
		
		Double tolerance = (Double)destination.getAttributes().get("tolerance");
		tolerance = 0.1+Math.pow((double)route.getNumHops()/12.0, 5.0);
		destination.getAttributes().put("tolerance",tolerance);
	}


   

	@Override
	/**
	 * Count the requests received in the current dispatch cycle
	 */
	protected void processRequest(ServiceRequest request) {

		
		//count request
		super.processRequest(request);
		
		entryQueue.add(request);
			
		overlay.getLocalNode().setAttribute("entryQueue", entryQueue.size());

	}
	
	
	
	public Double getQueueLength() {
		return new Double(entryQueue.size());
	}
	
	
	public Double getArrivals() {
		return (double)entryQueue.size();
	}
	
	@Override
	/**
	 * Check if the request must be rejected as the server has a fixed capacity
	 * 
	 */
	public boolean delivered(Routing router, Destination destination,
			Route route, Serializable message) {
		if(entryQueue.size() < queueLimit){
			return super.delivered(router, destination, route, message);
		}
		else{
			return false;
		}
	}


	
	public void setBackgroundLoad(Double load){
		
		Double availableCapacity = 1.0-getOfferedDemand();
		backgroundLoad = Math.min(availableCapacity,load);
					
	}
	
	public void updateBackgroundLoad(Double variation) {
				
		if(variation >= 0) 
			setBackgroundLoad(Math.min(backgroundLoad +variation,1.0));
		else
			setBackgroundLoad(Math.max(backgroundLoad +variation,0.0));
		
		
	}
	
	
	public Double getWorkload() {
		return serviceRate*(double)runQueue.size() + backgroundLoad;
	}
	
	public Double getBackgroundLoad(){
		return backgroundLoad;
	}
	
	/**
	 * Attend requests in the queue
	 */
	public void dispatchRequests(){
			
		//number of requests attended in the current dispatch cycle
		int servicedRequests = Math.min((int)Math.ceil(1.0/serviceRate),runQueue.size());
		
		Double responseTime = getResponseTime();
		
		//update node's attribute
		overlay.getLocalNode().getAttributes().put("service.time",responseTime);
	
		Double currentUtility = getUtility();
		
		for(int r=0;r < servicedRequests;r++){
			try {
			ServiceRequest request = runQueue.remove(0);
	
			Map attributes = new HashMap();
			attributes.put("request.qos",request.getQoS());
			attributes.put("request.demand",request.getServiceDemand());			
			attributes.put("node.utility",currentUtility);
			attributes.put("service.time",responseTime);	
			attributes.put("service.ratio",utility/request.getQoS());	
			
			
			Event event = new ServiceEvent(overlay.getLocalNode(),model.getCurrentTime(),
					attributes);

			model.getExperiment().reportEvent(event);
			} catch(IndexOutOfBoundsException e) {
				System.out.println();
			}
		

		}
		
		runQueue.addAll(entryQueue);
		entryQueue.clear();
		
		overlay.getLocalNode().setAttribute("runQueue", (double)runQueue.size());
		overlay.getLocalNode().setAttribute("entryQueue", (double)entryQueue.size());

	}

	
	/**
	 * Calculates the current service rate based on the arrivals and the average service rate 
	 * adjusted with the background load.
	 * 
	 * @return
	 */
	public Double getUtilization(){
	   
		return  Math.min(getOfferedDemand()+getBackgroundLoad(),1.0);

	}
	
	
	/**
	 * Returns the processor's utilization demanded for the tasks in the runQueue,
	 * independently of any other background load.
	 * 
	 * @return
	 */
	public Double getOfferedDemand() {
		return getAverageServiceDemand()*(double)runQueue.size();
	}
		
	/**
	 * 
	 * Used the analytical formula for the response time, but substitutes the utilization
	 * factor (offered demand) adding also the background load.
	 * 
	 * Returns the average response time using little's law
	 * 
	 * @return 
	 */
	public Double getResponseTime(){
            
		if(runQueue.size() == 0) {
			return 0.0;
		}
				
		Double u = getUtilization();
		Double responseTime = (Math.pow(u,queueLimit+1)*(queueLimit*u-queueLimit-1)+u)/ 
		                      ((double)runQueue.size()*(1.0-Math.pow(u, queueLimit))*(1.0-u));
				
				
		return responseTime;
	}

	
	/**
	 * Returns the throughput considering the backgroud load, using the
	 * operational utilization law  U = X*S  and then
	 *                              X = U/S
	 * @return
	 */
	public Double getThroughput() {
		return Math.min(1.0/serviceRate, getUtilization()/getAverageServiceDemand());
	}
	
	
	/**
	 * Returns the total service demand for the requests currently executing
	 * 
	 * @return
	 */
	public Double getServiceDemand() {
				
		Double serviceDemand = 0.0;
		for(ServiceRequest r: runQueue) {
			serviceDemand += r.getServiceDemand();
		}
		
		return serviceDemand;
	}
	
		
	/**
	 * Returns the total service demand from the requests currently in execution
	 * 
	 * @return
	 */
	public Double getAverageServiceDemand() {

		if(runQueue.size() == 0) {
			return 0.0;
		}

		return getServiceDemand()/(double)runQueue.size();
	}
	
	
	public Double getUtility(){
	
		return function.getUtility(overlay.getLocalNode());
		
	}
}

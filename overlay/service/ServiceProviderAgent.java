package edu.upc.cnds.collectivesim.overlay.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import sun.security.action.GetBooleanAction;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingHandler;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityOverlayAgent;
import edu.upc.cnds.collectivesim.state.Counter;
import edu.upc.cnds.collectivesim.stream.Stream;

public class ServiceProviderAgent extends UtilityOverlayAgent implements RoutingHandler,ServiceContainer{
	
		
	
	/**
	 * Counts the number of requests received by this agent
	 */
	private Counter requests; 
	
	/**
	 * 
	 */
	private ServiceDispatcher dispatcher;
	
	/**
	 * Percentage of the server's capacity occupied by an "external" workload 
	 */
	private Stream<Double> loadStream;


	/**
	 * Load not related to the service requests
	 */
	private Double backgroundLoad;
	
	/**
	 * Maximum number of requests accepted by the server on each cycle
	 */
	protected Integer capacity;
	
	/**
	 * 
	 * @param model
	 * @param overlay
	 * @param attributes
	 */
	public ServiceProviderAgent(OverlayModel model, Overlay overlay,UtilityFunction function,
								Integer capacity,ServiceDispatcher dispatcher,Stream<Double> loadStream) {
		
		super(model, overlay,function);
		
		this.capacity = capacity;
		this.dispatcher = dispatcher;
		this.loadStream= loadStream;
		this.backgroundLoad = loadStream.nextElement();		
		
		
		dispatcher.setContainer(this);
						
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
	
	
	@Override
	public boolean delivered(Routing router, Destination destination,
			Route route, Serializable message) {

		if((getLoad() < capacity) && super.delivered(router, destination, route, message)){
			dispatcher.processRequest((ServiceRequest)message);
			return true;
		}

		return false;
	}
	
	
	public void setCapacity(Integer capacity){
		this.capacity = capacity;
	}
	
	public Double getCapacity(){
		return new Double(capacity);
	}
	public Double getRequests(){
		return requests.getValue();
	}


	public Double getResponseTime(){
		return dispatcher.getResponseTime();
	}
	
	@Override
	public void deliver(Routing router, Destination destination, Node source,
			Serializable message) {
		
		processRequest((ServiceRequest)message);
		
	}

	/**
	 * Calculates the current service rate based on the arrivals and the average service rate 
	 * adjusted with the background load.
	 * 
	 * @return
	 */
	public Double getUtilization(){

		return dispatcher.getOfferedDemand()+backgroundLoad;

	}

	
	/**
	 * Attend requests in the queue. 
	 * 
	 * On each cycle, calculates the metrics for the next cycle.
	 * 
	 *  This method is assumed to be executed at the beginning of the cycle!
	 */
	public void dispatchRequests(){
		dispatcher.dispatchRequests();
	}

	
	public Double getBackgroundLoad(){
		return backgroundLoad;
	}
	
	
	@Override
	public Double getAvailableCpu() {
		return 1.0-getBackgroundLoad();
	}


	@Override
	public Long getCurrentTime() {
		return model.getCurrentTime();
	}


	@Override
	public void handleCompletion(ServiceRequest request) {
		reportRequestTermination(request);
		
	}
	
	public Double getLoad() {
		return new Double(dispatcher.getLoad());		

	}

	/**
	 * 
	 * @return the rate of arrivals in the last interval
	 */
	public Double getArrivals(){
		return dispatcher.getArrivals();
	}


	public void setBackgroundLoad(Double load){
		backgroundLoad = load;
	}
	
	
	protected void reportRequestTermination(ServiceRequest request){
		
		Double utility = getUtility();
		
		Map attributes = new HashMap();
		attributes.put("QoS",request.getQoS());
		attributes.put("Utility",utility);
		attributes.put("UtilityRatio",utility/request.getQoS());	
		attributes.putAll(request.getAttributes());
		
		Event event = new ServiceExecutionEvent(this,model.getCurrentTime(),
				attributes);

		model.getExperiment().reportEvent(event);

	}
}

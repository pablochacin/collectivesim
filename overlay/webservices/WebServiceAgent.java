package edu.upc.cnds.collectivesim.overlay.webservices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import edu.upc.cnds.collectives.adaptation.AdaptationFunction;
import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingEvent;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.CollectiveSim;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.service.ServiceProviderAgent;
import edu.upc.cnds.collectivesim.overlay.service.ServiceRequest;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityOverlayAgent;
import edu.upc.cnds.collectivesim.random.MersenneRandom;
import edu.upc.cnds.collectivesim.state.Counter;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * 
 * Skeleton for a WebService provider 
 *
 */
public abstract class WebServiceAgent extends ServiceProviderAgent {


	/**
	 * Requests being processed in the current simulation cycle
	 */
	protected List<ServiceRequest> runQueue;


	/**
	 * Percentage of the server's capacity occupied by an "external" workload 
	 */
	protected Stream<Double> loadStream;


	/**
	 * Load not related to the service requests
	 */
	protected Double backgroundLoad;

	/**
	 * Function used to calculate the utility offered by the server
	 */
	protected UtilityFunction utilityFuction;

	protected AdaptationFunction adaptationFunction;

	/**
	 * Maximum number of requests accepted by the server on each cycle
	 */
	protected Integer capacity;


	/**
	 * Maximum capacity of the server
	 */
	protected Integer maxCapacity;

	/**
	 * ratio of effective capacity (capacity/maxCapacity)
	 */
	protected Double capacityRatio = 0.75;


	/**
	 * Target utility for this service
	 */
	protected Double targetUtility;

	
	public WebServiceAgent(OverlayModel model, Overlay overlay, Identifier id,
			UtilityFunction utilityFunction,Double targetUtility,AdaptationFunction adaptationFunction,Integer maxCapacity,Stream<Double> loadStream) {
		super(model, overlay, id, utilityFunction);	

		this.targetUtility = targetUtility;
		this.maxCapacity = maxCapacity;
		this.capacity = maxCapacity;
		this.loadStream= loadStream;
		this.backgroundLoad = loadStream.nextElement();		
		this.utilityFuction = utilityFunction;
		this.adaptationFunction = adaptationFunction;

		runQueue = new ArrayList<ServiceRequest>(maxCapacity);

		adjustCapacity();

		overlay.getLocalNode().getAttributes().put("service.time", 0.0);
		overlay.getLocalNode().getAttributes().put("service.capacity", new Double(capacity));
		overlay.getLocalNode().getAttributes().put("service.capacity.ratio", new Double(capacityRatio));		
		overlay.getLocalNode().getAttributes().put("service.acceptance.rate", new Double(acceptanceRate));


	}



	@Override
	/**
	 * process incoming requests
	 * 
	 */
	protected void processRequest(ServiceRequest request){
		super.processRequest(request);
	}

	public Double getCapacity(){
		return new Double(capacity);
	}

	public Double getLoad() {
		return new Double(runQueue.size());		

	}

	/**
	 * 
	 * @return the rate of arrivals in the last interval
	 */
	public abstract Double getArrivals();

	@Override
	/**
	 * Check if the request must be rejected as the server has a fixed capacity
	 * 
	 */	
	public boolean delivered(Routing router, Destination destination,
			Route route, Serializable message) {

			return  super.delivered(router, destination, route, message);

	}


	public void setBackgroundLoad(Double load){
		backgroundLoad = load;
	}

	/**
	 * Update backgroundLoad. To maintain the same total load distribution across the simulation, 
	 * each node exchanges its load with another randomly choosen node
	 */
	public void updateBackgroundLoad() {

		for(ModelAgent n: model.getAgents()){
			if(!n.getName().equals(getName())){
				try {
					Double load = (Double)n.getAttribute("BackgroundLoad");
					if(Math.abs(getBackgroundLoad() - load) <= 0.1){
						n.execute("setBackgroundLoad",new Object[]{getBackgroundLoad()});
						setBackgroundLoad(load);
						break;
					}
				} catch (ModelException e) {}
			}
		}

	}

	public Double getBackgroundLoad(){
		return backgroundLoad;
	}

	/**
	 * Attend requests in the queue. 
	 * 
	 * On each cycle, calculates the metrics for the next cycle.
	 * 
	 *  This method is assumed to be executed at the beginning of the cycle!
	 */
	public abstract void dispatchRequests();
	
	protected  void adjustCapacityRatio(){

		updateUtility();

		capacityRatio = adaptationFunction.adapt(capacityRatio,getUtility(),targetUtility);

		overlay.getLocalNode().setAttribute("service.capacity.ratio", new Double(capacityRatio));

		adjustCapacity();
	}		




	protected  void adjustCapacity() {

		capacity = (int)Math.ceil(maxCapacity*capacityRatio);

		overlay.getLocalNode().setAttribute("service.capacity", new Double(capacity));


	}


	protected void reportRequestTermination(ServiceRequest request){
	
		Double utility = getUtility();
		
		Map attributes = new HashMap();
		attributes.put("request.qos",request.getQoS());
		attributes.put("node.utility",utility);
		attributes.put("service.utility.ratio",utility/request.getQoS());	
		attributes.putAll(request.getAttributes());
		
		Event event = new ServiceExecutionEvent(overlay.getLocalNode(),model.getCurrentTime(),
				attributes);

		model.getExperiment().reportEvent(event);

	}

	Double lastReceived=0.0;
	Double lastDelivered=0.0;
	Double acceptanceRate = 1.0;

	public void updateAcceptanceRate(){

		Double deliveredDelta = getDelivered()-lastDelivered;
		Double recievedDelta =getReceived()-lastReceived;
		if(recievedDelta > 0.0){
			acceptanceRate = deliveredDelta/recievedDelta;
		}

		lastDelivered = getDelivered();
		lastReceived = getReceived();

		overlay.getLocalNode().getAttributes().put("service.acceptance.rate", new Double(acceptanceRate));
	}

	public Double getAcceptanceRate(){
		return acceptanceRate;
	}

	/**
	 * Calculates the current service rate based on the arrivals and the average service rate 
	 * adjusted with the background load.
	 * 
	 * @return
	 */
	/**
	 * Calculates the current service rate based on the arrivals and the average service rate 
	 * adjusted with the background load.
	 * 
	 * @return
	 */
	public Double getUtilization(){

		return getOfferedDemand()+backgroundLoad;

	}

	/**
	 * Returns the processor's utilization demanded for the tasks in the runQueue,
	 * independently of any other background load.
	 * 
	 * @return
	 */
	public abstract Double getOfferedDemand();

	/**
	 * Returns the (average) response time 
	 * @return
	 */
	public abstract Double getResponseTime();



	public Double getCapacityRatio(){
		return capacityRatio;

	}

	public void updateCapacityRatio(){
		adjustCapacityRatio();
	}
}

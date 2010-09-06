package edu.upc.cnds.collectivesim.overlay.webservices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


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
	private Stream<Double> loadStream;
	
	
	/**
	 * Load not related to the service requests
	 */
	private Double backgroundLoad;
			
	/**
	 * Function used to calculate the utility offered by the server
	 */
	private UtilityFunction function;
	
	
	
	/**
	 * Maximum number of requests accepted by the server on each cycle
	 */
	private Integer capacity;
	
	
	/**
	 * Maximum capacity of the server
	 */
	private Integer maxCapacity;
	
	/**
	 * Nominal service capacity of the server (the inverse of its maximum throughput)
	 */
	private Double serviceRate;
	
		
	/**
	 * Response time offered during this cycle
	 */
	private Double responseTime = 0.0;
	
	/**
	 * Throughput during the current cycle
	 */
	private Double throughput = 0.0;
	
	private Double serviceDemand = 0.0;
	
	private Double offeredDemand = 0.0;
	
	
	public WebServiceAgent(OverlayModel model, Overlay overlay, Identifier id,
			UtilityFunction function,Integer maxCapacity,Double serviceRate,Stream<Double> loadStream) {
		super(model, overlay, id, function);	
	
		this.maxCapacity = maxCapacity;
		this.capacity = maxCapacity;
		this.serviceRate = serviceRate;
		this.loadStream= loadStream;
		this.backgroundLoad = loadStream.nextElement();		
		this.function = function;
		
		entryQueue = new ArrayList<ServiceRequest>(maxCapacity);

		runQueue = new ArrayList<ServiceRequest>(maxCapacity);

		adjustCapacity();
		
		overlay.getLocalNode().getAttributes().put("service.time", 0.0);
		overlay.getLocalNode().getAttributes().put("service.capacity", new Double(capacity));
		overlay.getLocalNode().setAttribute("service.acceptanceP", new Double(acceptanceP));
		
		
	}

	
	

	@Override
	public void forwarded(Routing router, Destination destination, Route route,
				Serializable message) {
			super.forwarded(router, destination, route, message);
		
//		Double tolerance = (Double)destination.getAttributes().get("tolerance");
//		tolerance = Math.pow((double)route.getNumHops()/8.0, 2.0);
//		destination.getAttributes().put("tolerance",tolerance);
//		
//		ServiceRequest r = (ServiceRequest)message;
//		r.setTolerance(tolerance);
//		
	}

   

	@Override
	/**
	 * Count the requests received in the current dispatch cycle
	 */
	protected void processRequest(ServiceRequest request) {

		
		//count request
		super.processRequest(request);
		
		entryQueue.add(request);
			

	}
	
	
	public Double getCapacity(){
		return new Double(capacity);
		//return new Double(acceptanceP);
	}
	
	public Double getLoad() {
		return new Double(runQueue.size());		
		
	}
	
	
	public Double getArrivals() {
		return (double)entryQueue.size();
	}
	
	
	Random rand = new MersenneRandom();
	
	@Override
	/**
	 * Check if the request must be rejected as the server has a fixed capacity
	 * 
	 */	
	public boolean delivered(Routing router, Destination destination,
			Route route, Serializable message) {
		
		

		
//	if(entryQueue.size() < capacity){
	   if(entryQueue.size() < capacity){

//			if(rand.nextDouble() <= acceptanceP){				
				return super.delivered(router, destination, route, message);
//			}
		}		
		return false;
		
	}

		
	public void setBackgroundLoad(Double load){
		backgroundLoad = load;
	}
	
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
		
		//backgroundLoad = loadStream.nextElement();
		
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
	public void dispatchRequests(){
			
		//report terminations from the previous cycle
		reportTerminations();
			
		runQueue.clear();
		
		//updateBackgroundLoad();
		
		if(entryQueue.size() == 0) {
			responseTime = 0.0;
			offeredDemand = 0.0;
			serviceDemand = 0.0;
			throughput = 0.0;
		}

		
		//number of requests attended in the current dispatch cycle
		serviceDemand = getAverageServiceDemand();
		
		//calculate throughput considering background load
		throughput = (1.0-backgroundLoad)/serviceDemand;
			
		//int servedRequests = (int) Math.min(Math.floor(throughput),entryQueue.size());
		
		int servedRequests = entryQueue.size();
		
		offeredDemand = serviceDemand*servedRequests;
		
		for(int i=0;i<servedRequests;i++) {
			runQueue.add(entryQueue.remove(0));
		}

		responseTime = calculatetResponseTime();
		
		
		overlay.getLocalNode().setAttribute("service.load", new Double(runQueue.size()));
		overlay.getLocalNode().setAttribute("service.arrivals",new Double(entryQueue.size()));
		overlay.getLocalNode().setAttribute("service.response",getResponseTime());
		overlay.getLocalNode().setAttribute("utility",getUtility());

		//adjustAcceptanceP();
		//overlay.getLocalNode().setAttribute("service.acceptanceP", new Double(acceptanceP));
	
		adjustCapacity();
		overlay.getLocalNode().setAttribute("service.capacity", new Double(capacity));

		
	}

	Double acceptanceP = 1.0;
 
	boolean initilized = false;
	
	private void adjustAcceptanceP(){

//		Double newAcceptanceP = Math.min(1.0,Math.max(0,acceptanceP *(1.0+ (getUtility()-0.7))));
//			acceptanceP = 0.75*acceptanceP + 0.25*newAcceptanceP;

		
		if(!initilized) {
			initAdaptation(acceptanceP, getUtility(), 0.7);
			initilized = true;
		}
		else{
			acceptanceP = adaptation(acceptanceP,getUtility());
		}
	}		
		
	
	private void initAdaptation(Double X,Double F, Double A){
		f[t] = F;
		x[t] = X;
		a[t+1] = A;
		
		
	}
	
	static int t=1;
	
	Double beta = 0.4;
	Double sigma = 0.3;
	Double phi = 0.4;
	Double gamma = 3*phi;
	
	Double[] f = {0.0,0.0,0.0};
	Double[] f_aprox = {0.0,0.0,0.0};
	Double[] x = {0.0,0.0,0.0};	
	Double[] r = {0.0,0.0,0.0};
	Double[] a = {0.0,0.0,0.0};
	
	private Double adaptation(Double X,Double F){	
		f[t-1] = f[t];
		x[t-1] = x[t];
		
		f[t] = F;
		x[t] = X;
		
		r[t] = r[t+1];
		a[t] = a[t+1];
		
		f_aprox[t] = f_aprox[t+1];
					
		f_aprox[t+1] = (1.0-beta)*f_aprox[t] + beta*f[t];
	
		r[t+1] = (1.0-sigma)*r[t] *sigma*(f[t]-f[t-1]);
		
		a[t+1] = (1.0-phi)*a[t] + phi*f_aprox[t+1] + gamma*r[t+1];
		
		Double c = (f[t]-f[t-1])/(x[t]-x[t-1]);
		
		Double d = c/Math.abs(c);
				
		Double delta_x = d*((a[t+1]-f[t])/Math.abs(c));
		
		x[t+1] = x[t] + delta_x;
		
		return x[t+1];
	}
	
	
	private void adjustCapacity() {
		
	  capacity = (int)Math.ceil(maxCapacity*acceptanceP);
	

	}
	
	private void reportTerminations() {
		
		Double utility = getUtility();
		for(ServiceRequest request: runQueue) {
			try {
			
				//ServiceRequest request = runQueue.remove(0);
	
			Map attributes = new HashMap();
			attributes.put("request.qos",request.getQoS());
			attributes.put("request.demand",request.getServiceDemand());			
			attributes.put("node.utility",utility);
			attributes.put("service.response",responseTime);	
			attributes.put("service.utility.ratio",utility/request.getQoS());	
			
			
			Event event = new ServiceEvent(overlay.getLocalNode(),model.getCurrentTime(),
					attributes);

			model.getExperiment().reportEvent(event);
			} catch(IndexOutOfBoundsException e) {
				log.severe("Error calculating the throughput in the service: IndexOutofbounds");
			}
		

		}
	}
	
	


	/**
	 * Calculates the current service rate based on the arrivals and the average service rate 
	 * adjusted with the background load.
	 * 
	 * @return
	 */
	public Double getUtilization(){
	   
		return offeredDemand+backgroundLoad;

	}
	
	
	/**
	 * Returns the processor's utilization demanded for the tasks in the runQueue,
	 * independently of any other background load.
	 * 
	 * @return
	 */
	public Double getOfferedDemand() {
		
		return offeredDemand;
	}
		
	public Double getResponseTime() {
		return responseTime;
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
	public Double calculatetResponseTime(){
            
		if(runQueue.size() == 0) {
			return 0.0;
		}
		
		//return runQueue.size()/getThroughput();
		
		Double u = getUtilization();
		Double responseTime = (Math.pow(u,capacity+1)*(capacity*u-capacity-1)+u)/ 
		((double)runQueue.size()*(1.0-Math.pow(u, capacity))*(1.0-u));		

		if(responseTime < 0.0){
			System.out.print("");
		}
 		return responseTime;
	}

	
	/**
	 * Returns the throughput considering the backgroud load, using the
	 * operational utilization law  U = X*S  and then
	 *                              X = U/S
	 * @return
	 */
	public Double getThroughput() {
		return throughput;
	}
	
	
	/**
	 * Returns the total service demand from the requests currently in execution
	 * 
	 * @return
	 */
	public Double getAverageServiceDemand() {

		if(entryQueue.size() == 0) {
			return 0.0;
		}

		Double serviceDemand = 0.0;
		for(ServiceRequest r: entryQueue) {
			serviceDemand += r.getServiceDemand();
		}
		
		return serviceDemand/(double)entryQueue.size();
		
	}
	

	
	public Double getUtility(){
	
		return function.getUtility(overlay.getLocalNode());
		
	}
	
	
	public void startDebug() {
		System.out.println();
	}
	
		
	
	public Double getAcceptanceRate(){
		return acceptanceP;
		
	}
	
	public void updateAcceptanceRate(){
		adjustAcceptanceP();
		overlay.getLocalNode().setAttribute("service.acceptanceP", new Double(acceptanceP));
	}
}

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
public class DiscreteTimeWebServiceAgent extends WebServiceAgent {


	/**
	 * Requests arrived in the current simulation cycle and waiting to enter the server
	 */
	protected List<ServiceRequest> entryQueue;

	/**
	 * Nominal service capacity of the server (the inverse of its maximum throughput)
	 */
	protected Double serviceRate;
	
	/**
	 * Response time offered during this cycle
	 */
	protected Double responseTime = 0.0;

	/**
	 * Throughput during the current cycle
	 */
	protected Double throughput = 0.0;

	protected Double serviceDemand = 0.0;

	protected Double offeredDemand = 0.0;

	
	public DiscreteTimeWebServiceAgent(OverlayModel model, Overlay overlay, UtilityFunction utilityFunction,Double targetUtility,AdaptationFunction adaptationFunction,Integer maxCapacity,Stream<Double> loadStream,Double serviceRate) {
		super(model, overlay, utilityFunction,targetUtility,adaptationFunction,maxCapacity,loadStream);	

		this.serviceRate = serviceRate;
		entryQueue = new ArrayList<ServiceRequest>(maxCapacity);


	}




	@Override
	/**
	 * Count the requests received in the current dispatch cycle
	 */
	protected void processRequest(ServiceRequest request) {


		//count request
		super.processRequest(request);

		//initialize the waiting time for this request
		request.getAttributes().put("dispatcher.waiting",0.0);
		
		entryQueue.add(request);

	}



	public Double getArrivals() {
		return new Double(entryQueue.size());
	}


	@Override
	/**
	 * Check if the request must be rejected as the server has a fixed capacity
	 * 
	 */	
	public boolean delivered(Routing router, Destination destination,
			Route route, Serializable message) {



		if(entryQueue.size() < capacity){
		//if(entryQueue.size() < maxCapacity){

			return  super.delivered(router, destination, route, message);
		}		

		return false;

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
		
		if(entryQueue.size() == 0) {
			responseTime = 0.0;
			offeredDemand = 0.0;
			serviceDemand = 0.0;
			throughput = 0.0;
		}
		else{

			//number of requests attended in the current dispatch cycle
			serviceDemand = getAverageServiceDemand();

			//calculate throughput considering background load
			throughput = (1.0-backgroundLoad)/serviceDemand;

			int servedRequests = (int) Math.min(Math.floor(throughput),entryQueue.size());

			//int servedRequests = entryQueue.size();

			offeredDemand = serviceDemand*servedRequests;

			for(int i=0;i<servedRequests;i++) {
				runQueue.add(entryQueue.remove(0));
			}

			responseTime = calculatetResponseTime();
			
			//increase waiting time of requests in the queue;
			for(ServiceRequest r: entryQueue){
				Double wt = (Double)r.getAttributes().get("dispatcher.waiting");
				r.getAttributes().put("waiting",wt+1.0);
			}
			
			adjustCapacity();

		}

	}


	protected  void reportTerminations() {


		for(ServiceRequest request: runQueue) {

			reportRequestTermination(request);
		}
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
			serviceDemand += (Double)r.getAttributes().get("service.demand");
		}

		return serviceDemand/(double)entryQueue.size();

	}



}

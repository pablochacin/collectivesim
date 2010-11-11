package edu.upc.cnds.collectivesim.overlay.webservices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import edu.upc.cnds.collectives.adaptation.AdaptationFunction;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.service.ServiceRequest;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * Implements a web server using a discrete event simulation of the dispatching
 * 
 * @author Pablo Chacin
 *
 */
public class DiscreteEventWebServiceAgent extends WebServiceAgent {

	/**
	 * List of requests received in the last interval
	 */
	protected List<ServiceRequest> arrivals;

	protected List<ServiceRequest> departures;

	/**
	 * interval used to calculate performance statistics, in milliseconds
	 */
	protected long interval;

	/**
	 * Dispatch quantum to be distributed among requests in the run queue
	 */
	protected Double quantum;

	/**
	 * Historic offered demand per dispatch queue 
	 */
	protected Double[] offeredDemand;

	public DiscreteEventWebServiceAgent(OverlayModel model, Overlay overlay,
			Identifier id, UtilityFunction utilityFunction,
			Double targetUtility, AdaptationFunction adaptationFunction,
			Integer maxCapacity, Stream<Double> loadStream,long interval,Double quantum) {

		super(model, overlay, id, utilityFunction, targetUtility,
				adaptationFunction, maxCapacity, loadStream);

		this.interval = interval;
		this.quantum = quantum;
		this.arrivals = new ArrayList<ServiceRequest>();
		this.departures= new ArrayList<ServiceRequest>();
		this.offeredDemand = new Double[(int)Math.ceil(interval/quantum)];
		for(int i = 0; i< offeredDemand.length;i++){
			offeredDemand[i] = new Double(0.0);
		}
		
	}




	@Override
	public boolean delivered(Routing router, Destination destination,
			Route route, Serializable message) {

	//	if(runQueue.size() < maxCapacity){
		if(runQueue.size() < capacity){
			return super.delivered(router, destination, route, message);
		}

		return false;
	}




	@Override
	protected void processRequest(ServiceRequest request) {

		super.processRequest(request);

		request.getAttributes().put("dispatcher.arrival", model.getCurrentTime()*quantum);

		arrivals.add(request);

		request.getAttributes().put("dispatcher.remaining", (Double)request.getAttributes().get("service.demand")*interval);
		runQueue.add(request);
	}


	/**
	 * Removes expired items of a list of requests
	 * 
	 * @param list
	 */
	protected void updateList(List<ServiceRequest> list,String attribute){
		//purge arrivals that belong to previous period		
		Iterator<ServiceRequest> requests = list.iterator();
		while(requests.hasNext()){
			ServiceRequest r = requests.next();
			Double time = (Double)r.getAttributes().get(attribute);
			if((model.getCurrentTime()*quantum-time)>interval){
				requests.remove();
			}
		}
	}

	@Override
	public void dispatchRequests() {


		updateList(arrivals,"dispatcher.arrival");

		Double rho = 0.0;

		if(!runQueue.isEmpty()){

			//adjust quantum assigned to requests considering the background load
			Double q = (quantum*(1-getBackgroundLoad()))/(double)runQueue.size();

			Iterator<ServiceRequest> requests = runQueue.iterator();
			while(requests.hasNext()){
				ServiceRequest r = requests.next();
				Double remaining = (Double)r.getAttributes().get("dispatcher.remaining");
				rho += Math.min(remaining, q);
				
				remaining = Math.max(0.0, remaining -q);
				if(remaining == 0){
					requests.remove();
					Double departure = (double)model.getCurrentTime()*quantum;
					r.getAttributes().put("dispatcher.departure", departure);
					Double response =  (departure- (Double)r.getAttributes().get("dispatcher.arrival"))/(double)interval;				
					r.getAttributes().put("service.response",response);
					departures.add(r);
					reportRequestTermination(r);
				}

				r.getAttributes().put("dispatcher.remaining",remaining);

			}
		}
		
		updateList(departures,"dispatcher.departure");
		
		offeredDemand[(int) (model.getCurrentTime() % offeredDemand.length)] = rho/quantum;
		
		overlay.getLocalNode().getAttributes().put("service.response",getResponseTime());
		overlay.getLocalNode().setAttribute("service.load", new Double(runQueue.size()));
		overlay.getLocalNode().setAttribute("utility",getUtility());
	}

	@Override
	public Double getArrivals() {
		return new Double(arrivals.size());
	}

	@Override
	public Double getOfferedDemand() {
		
		Double demand = 0.0;

	//	for(ServiceRequest r: runQueue){
	//		demand += (Double)r.getAttributes().get("dispatcher.remaining");
	//	}
		
		for(Double d: offeredDemand){
			demand+= d;
		}

		return Math.min(1.0, demand/(double)offeredDemand.length);
	}


	@Override
	public Double getResponseTime() {

		if(departures.isEmpty()){
			return Double.NaN;
		}

		Double responseTime = 0.0;
		for(ServiceRequest r: departures){
			responseTime += (Double)r.getAttributes().get("service.response");
		}

		return responseTime/(double)departures.size();

	}



}

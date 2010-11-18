package edu.upc.cnds.collectivesim.overlay.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.upc.cnds.collectivesim.overlay.service.ServiceContainer;
import edu.upc.cnds.collectivesim.overlay.service.ServiceDispatcher;
import edu.upc.cnds.collectivesim.overlay.service.ServiceRequest;

/**
 * Implements a web server using a discrete event simulation of the dispatching
 * 
 * @author Pablo Chacin
 *
 */
public class DiscreteEventServiceDispatcher implements ServiceDispatcher {
	
	protected ServiceContainer container;
	
	/**
	 * Requests being processed in the current simulation cycle
	 */
	protected List<ServiceRequest> runQueue;

	/**
	 * List of requests received in the last interval
	 */
	protected List<ServiceRequest> arrivals;

	/**
	 * List of requests finished during the last interval
	 */
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

	public DiscreteEventServiceDispatcher(ServiceContainer container,long interval,Double quantum) {

		this.container = container;
		this.interval = interval;
		this.quantum = quantum;
		this.runQueue = new ArrayList<ServiceRequest>();
		this.arrivals = new ArrayList<ServiceRequest>();
		this.departures= new ArrayList<ServiceRequest>();
		this.offeredDemand = new Double[(int)Math.ceil(interval/quantum)];
		for(int i = 0; i< offeredDemand.length;i++){
			offeredDemand[i] = new Double(0.0);
		}
		
	}

	public DiscreteEventServiceDispatcher(long interval,Double quantum) {
		this(null,interval,quantum);
	}



	@Override
	public void processRequest(ServiceRequest request) {


		request.getAttributes().put("dispatcher.arrival", container.getCurrentTime()*quantum);

		arrivals.add(request);

		request.getAttributes().put("dispatcher.remaining", (Double)request.getAttributes().get("ServiceDemand")*interval);
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
			if((container.getCurrentTime()*quantum-time)>interval){
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
			Double q = (quantum*(container.getAvailableCpu()))/(double)runQueue.size();

			Iterator<ServiceRequest> requests = runQueue.iterator();
			while(requests.hasNext()){
				ServiceRequest r = requests.next();
				Double remaining = (Double)r.getAttributes().get("dispatcher.remaining");
				rho += Math.min(remaining, q);
				
				remaining = Math.max(0.0, remaining -q);
				if(remaining == 0){
					requests.remove();
					Double departure = (double)container.getCurrentTime()*quantum;
					r.getAttributes().put("dispatcher.departure", departure);
					Double response =  (departure- (Double)r.getAttributes().get("dispatcher.arrival"))/(double)interval;				
					r.getAttributes().put("service.response",response);
					departures.add(r);
					container.handleCompletion(r);
				}

				r.getAttributes().put("dispatcher.remaining",remaining);

			}
		}
		
		updateList(departures,"dispatcher.departure");
		
		offeredDemand[(int) (container.getCurrentTime() % offeredDemand.length)] = rho/quantum;
		
	}

	@Override
	public Double getArrivals() {
		return new Double(arrivals.size());
	}

	@Override
	public Double getOfferedDemand() {
		
		Double demand = 0.0;
		
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


	@Override
	public void setContainer(ServiceContainer container){
		this.container = container;
	}


	@Override
	public Double getLoad() {
		return new Double(runQueue.size());
	}
}

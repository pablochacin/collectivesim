package edu.upc.cnds.collectivesim.overlay.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingHandler;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;

public class ServiceProviderOverlayAgent extends ServiceOverlayAgent  implements RoutingHandler {

	/**
	 * Stores the information to process a service requests
	 * 
	 * @author Pablo Chacin
	 *
	 */
	private class ServiceTask implements Comparable<ServiceTask> {
		
		protected Long endTime;
		
		protected Double utility;
		
		protected Long duration;

		public ServiceTask(Long endTime, Double utility, Long duration) {
			super();
			this.endTime = endTime;
			this.utility = utility;
			this.duration = duration;
		}

		public Long getEndTime() {
			return endTime;
		}

		public void setEndTime(Long endTime) {
			this.endTime = endTime;
		}

		public Double getUtility() {
			return utility;
		}

		public Long getDuration() {
			return duration;
		}

		@Override
		public int compareTo(ServiceTask o) {
			 return endTime.compareTo(o.getEndTime());
		}
		
		
		
	}
	
	
	/**
	 * Function used to calculate the agent's offered utility
	 */
	protected UtilityFunction function;
	
	/**
	 *  Keeps a recent history of the provider's load
	 */
	protected Vector<Double>loadHistory;
	
	/**
	 * Currently active service requests
	 */
	protected Vector<ServiceTask>tasks;
	
	
	public ServiceProviderOverlayAgent(OverlayModel model, Overlay overlay,
			UtilityFunction function,Map attributes) {
			
			super(model, overlay,attributes);
	
			this.tasks = new Vector<ServiceTask>();
			
			this.loadHistory = new Vector<Double>(10);
					
			this.overlay.addRoutingHandler(this);
			
			this.function = function;
			
	}
	
		

	/**
	 * Process the requests in the queue and update utility based on its
	 * current workload
	 */
	public void processRequests(){
		
		Collections.sort(tasks);
		
		Iterator<ServiceTask> iter = tasks.iterator();
		
		//remove all the expired requests
		while(iter.hasNext() && (iter.next().getEndTime() <= (double)model.getCurrentTime())){
			iter.remove();
		}
							
		Double currentLoad = (double)tasks.size();
				
		loadHistory.add(currentLoad);
		if(loadHistory.size() > 10){
			loadHistory.remove(0);
		}
		
		setUtility(function.getUtility(overlay.getLocalNode()));

	}


	@Override
	protected void processRequest(ServiceRequest request){
		tasks.add(new ServiceTask(model.getCurrentTime()+request.getDuration(),				                  
				                  request.getUtility(),
				                  request.getDuration()));
	}

	
	@Override
	public void deliver(Routing router, Destination destination, Node source,
			Serializable message) {
				
		processRequest((ServiceRequest)message);
				
				
	}

	
	/**
	 * Gets the average load in the last cycles
	 * 
	 * @return the average load 
	 */
	 public Double getAverageLoad(){
			Double avgLoad = 0.0;
			for(Double u: loadHistory){
				avgLoad =+ u;
			}
			
			avgLoad = avgLoad/loadHistory.size();
			
			return avgLoad;			
	 }

	 
	 /**
	  * Gets the current load (number of active requests)
	  * 
	  * @return the current load
	  */
	 public Double getLoad(){
		return (double)tasks.size(); 
	 }
}

package edu.upc.cnds.collectivesim.overlay.webservices;

import java.util.ArrayList;
import java.util.List;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.service.ServiceProviderAgent;
import edu.upc.cnds.collectivesim.overlay.service.ServiceRequest;
import edu.upc.cnds.collectivesim.state.Counter;

public class WebServiceAgent extends ServiceProviderAgent {

	
	private List<ServiceRequest> requests;
	
	private Double backgroundLoad;
	
	private Double averageServiceRequest;
	
	private Double arrivals = 0.0;
	
	private Counter rejected;
	
	private Integer requestLimit;
	
	
	public WebServiceAgent(OverlayModel model, Overlay overlay, Identifier id,
			Double utility, Double averageServiceRequest,Integer requestLimit) {
		super(model, overlay, id, utility);	
		
		this.averageServiceRequest = averageServiceRequest;
		this.requestLimit = requestLimit;
		
		requests = new ArrayList<ServiceRequest>(requestLimit);
		
		rejected = model.getExperiment().getCounter("requests.rejected").getChild();
	}


	@Override
	protected void processRequest(ServiceRequest request) {
		//count request
		super.processRequest(request);
		
		if(requests.size() < requestLimit){
			requests.add(request);
			arrivals++;
		}
		else{
			rejected.increment();
		}
	}
	
	
	
	public void setBackgroundLoad(Double load){
		backgroundLoad = load;
	}
	
	
	public Double getBackgroundLoad(){
		return backgroundLoad;
	}
	
	public void dispatchRequests(){
		
		int servicedRequests = (int)Math.ceil(getServiceRate());
		
		for(int r =0;r < servicedRequests;r++){
			ServiceRequest request = requests.remove(0);
			
		}
		
		arrivals = 0.0;
	}

		
	/**
	 * @return the average service time based on the current arrival rate and the average service 
	 *         demand per request
	 */
	public Double getServiceTime(){
            
		Double serviceRate = getServiceRate();
		Double serviceTime = (Math.pow(serviceRate, requestLimit+1.0) * (requestLimit*serviceRate-requestLimit-1) + serviceRate)/
		                     (arrivals*(1-Math.pow(serviceRate, requestLimit))*(1-serviceRate));
		
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
}

package edu.upc.cnds.collectivesim.overlay.service;

import edu.upc.cnds.collectives.adaptation.AdaptationFunction;
import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * 
 *
 */
public class AdaptiveServiceProviderAgent extends ServiceProviderAgent {


	/**
	 * Function used to calculate the utility offered by the server
	 */
	protected UtilityFunction utilityFuction;

	protected AdaptationFunction adaptationFunction;


	/**
	 * ratio of effective capacity (capacity/maxCapacity)
	 */
	protected Double capacityRatio = 0.75;

	
	protected Integer maxCapacity;

	/**
	 * Target utility for this service
	 */
	protected Double targetUtility;

	
	public AdaptiveServiceProviderAgent(OverlayModel model, Overlay overlay, UtilityFunction utilityFunction,
			ServiceDispatcher dispatcher,Double targetUtility,AdaptationFunction adaptationFunction,Integer capacity,Stream<Double> loadStream) {
		super(model, overlay, utilityFunction,capacity,dispatcher,loadStream);	

		this.targetUtility = targetUtility;
		this.adaptationFunction = adaptationFunction;
		this.maxCapacity = capacity;
	}


		
	@Override
	/**
	 * process incoming requests
	 * 
	 */
	protected void processRequest(ServiceRequest request){
		super.processRequest(request);
		
		Event event = new ServiceReceptionEvent(this, model.getCurrentTime());

		model.getExperiment().reportEvent(event);
	}



	/**
	 * Update backgroundLoad. To maintain the same total load distribution across the simulation, 
	 * each node exchanges its load with another randomly choosen node
	 */
	public void updateBackgroundLoad() {

		for(ModelAgent n: model.getAgents()){
			if(!n.getName().equals(getName())){
				try {
					Double load = (Double)n.inquire("BackgroundLoad");
					if(Math.abs(getBackgroundLoad() - load) <= 0.1){
						n.execute("setBackgroundLoad",new Object[]{getBackgroundLoad()});
						setBackgroundLoad(load);
						break;
					}
				} catch (ModelException e) {}
			}
		}

	}


	
	protected  void adjustCapacityRatio(){

		updateUtility();

		capacityRatio = adaptationFunction.adapt(capacityRatio,getUtility(),targetUtility);

		super.setCapacity((int)Math.ceil(maxCapacity*capacityRatio));
	}		


	@Override
	public void setCapacity(Integer capacity){
		this.maxCapacity = capacity;
		super.setCapacity(capacity);
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

	}

	public Double getAcceptanceRate(){
		return acceptanceRate;
	}



	/**
	 * Returns the processor's utilization demanded for the tasks in the runQueue,
	 * independently of any other background load.
	 * 
	 * @return
	 */


	public Double getCapacityRatio(){
		return capacityRatio;

	}

	public void updateCapacityRatio(){
		adjustCapacityRatio();
	}
	
	
}

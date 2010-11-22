package edu.upc.cnds.collectivesim.overlay.service;

import java.util.HashMap;

import edu.upc.cnds.collectives.adaptation.action.AdaptationAction;
import edu.upc.cnds.collectives.adaptation.function.AdaptationFunction;
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

	/**
	 * Adaptation function used to adapt the capacity
	 */
	protected AdaptationFunction capacityAdaptation;


	/**
	 * ratio of effective capacity (capacity/maxCapacity)
	 */
	protected Double capacityRatio = 0.75;

	
	protected Integer maxCapacity;



	
	public AdaptiveServiceProviderAgent(Overlay overlay, UtilityFunction utilityFunction,
			ServiceDispatcher dispatcher,AdaptationFunction capacityAdaptation,
			Integer capacity,Stream<Double> loadStream) {
		super(overlay, utilityFunction,capacity,dispatcher,loadStream);	

		this.capacityAdaptation = capacityAdaptation;
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

	
	protected  void adjustCapacityRatio(){

		updateUtility();

		capacityRatio = capacityAdaptation.adapt(capacityRatio,getUtility());

		super.setCapacity((int)Math.ceil(maxCapacity*capacityRatio));
	}		


	@Override
	public void setCapacity(Integer capacity){
		this.maxCapacity = capacity;
		super.setCapacity(capacity);
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

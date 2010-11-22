package edu.upc.cnds.collectivesim.overlay.elastic;


import edu.upc.cnds.collectives.adaptation.AdaptationEnvironment;
import edu.upc.cnds.collectives.adaptation.function.AdaptationFunction;
import edu.upc.cnds.collectives.overlay.elastic.ElasticOverlay;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.overlay.service.AdaptiveServiceProviderAgent;
import edu.upc.cnds.collectivesim.overlay.service.ServiceDispatcher;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;

public class ElasticServiceProviderAgent extends AdaptiveServiceProviderAgent {

	/**
	 * Probability of joining the service overlay
	 */
	protected Double joinProbability;
	
	protected AdaptationEnvironment adaptation;
	
	public ElasticServiceProviderAgent(ElasticOverlay overlay,
			UtilityFunction utilityFunction, ServiceDispatcher dispatcher,
			Double targetUtility, AdaptationFunction adaptationFunction,
			Integer capacity, Stream<Double> loadStream,AdaptationEnvironment adaptation) {
	
		super(overlay, utilityFunction, dispatcher, 
				adaptationFunction, capacity, loadStream);
		
		this.adaptation = adaptation;
	}

	
	@Override
	public void init(Model model){
		super.init(model);	
		//TODO: join the serviceRouting overlay!
	}
	
	/**
	 * promote the agent to the service Overlay
	 */
	public void promote(){
		
		if(!isPromoted()){
			((ElasticOverlay)overlay).getServiceOverlay().join();
		}
	}
	
	/**
	 * Demote the agent from the service Overlay
	 */
	public void demote(){
		if(isPromoted()){
			((ElasticOverlay)overlay).getServiceOverlay().leave();			
		}
		
	}
	
	/**
	 * Indicates if the agent currently is promoted to the service routing overlay or not
	 * 
	 * @return a boolean indicating if the agent is promoted (true) or not (false)
	 */
	public boolean isPromoted(){
		return ((ElasticOverlay)overlay).getServiceOverlay().isActive();
	}
	
	
}

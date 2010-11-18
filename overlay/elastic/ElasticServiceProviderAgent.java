package edu.upc.cnds.collectivesim.overlay.elastic;

import edu.upc.cnds.collectives.adaptation.AdaptationFunction;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.elastic.ElasticOverlay;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.service.AdaptiveServiceProviderAgent;
import edu.upc.cnds.collectivesim.overlay.service.ServiceDispatcher;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;

public class ElasticServiceProviderAgent extends AdaptiveServiceProviderAgent {

	/**
	 * Probability of joining the service overlay
	 */
	protected Double joinProbability;
	
	public ElasticServiceProviderAgent(OverlayModel model, ElasticOverlay overlay,
			UtilityFunction utilityFunction, ServiceDispatcher dispatcher,
			Double targetUtility, AdaptationFunction adaptationFunction,
			Integer capacity, Stream<Double> loadStream,Double joinProbability) {
	
		super(model, overlay, utilityFunction, dispatcher, targetUtility,
				adaptationFunction, capacity, loadStream);
		
		this.joinProbability = joinProbability;
	}

	
	@Override
	public void init(){
		super.init();
	 
		if(model.getExperiment().getRandomGenerator().nextDouble() <= joinProbability) {
			((ElasticOverlay)overlay).getServiceOverlay().join();
		}
	}
	
	
	public void adaptOverlay(){
		
		
	}
}

package edu.upc.cnds.collectivesim.overlay.service;

import edu.upc.cnds.collectives.adaptation.action.AdaptationFunctionAction;
import edu.upc.cnds.collectives.adaptation.action.ProbabilisticAdaptationAction;
import edu.upc.cnds.collectives.adaptation.function.AdaptationFunction;
import edu.upc.cnds.collectives.adaptation.function.ProbabilisticAdaptationFunction;
import edu.upc.cnds.collectives.factory.Factory;
import edu.upc.cnds.collectives.factory.base.CloningFactory;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayAgentFactory;
import edu.upc.cnds.collectivesim.overlay.service.ServiceDispatcher;
import edu.upc.cnds.collectivesim.overlay.service.ServiceProviderAgent;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;

public class AdaptiveServiceProviderAgentFactory extends ServiceProviderAgentFactory {

	
	
	public AdaptiveServiceProviderAgentFactory(OverlayFactory overlayFactory,
			Factory<UtilityFunction> utilityFunctionFactory,
			Factory<ServiceDispatcher> serviceDispatcherFactory,
			Factory<Integer> capacityFactory, Factory<Double> windowFactory,
			Factory<Stream<Double>> loadStreamFactory) {
		
		
		super(overlayFactory, utilityFunctionFactory, serviceDispatcherFactory,
				capacityFactory, windowFactory,
				loadStreamFactory);
		
	}

	@Override
	protected ServiceProviderAgent createOverlayAgent(Overlay overlay) {		
					
		ServiceProviderAgent provider = super.createOverlayAgent(overlay);
		
		AdaptationFunction function = new ProbabilisticAdaptationFunction(0.0, 1.0, 0.5, 0.25, 0.7);
		
		provider.addAction("capacity.adaptation", new AdaptationFunctionAction("CapacityRatio", 
																			   "Utility", 
																			   function));
	
	return provider;
	}
}

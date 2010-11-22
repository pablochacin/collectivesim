package edu.upc.cnds.collectivesim.overlay.webservices;

import edu.upc.cnds.collectives.adaptation.function.AdaptationFunction;
import edu.upc.cnds.collectives.factory.CloningFactory;
import edu.upc.cnds.collectives.factory.Factory;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayAgentFactory;
import edu.upc.cnds.collectivesim.overlay.service.AdaptiveServiceProviderAgent;
import edu.upc.cnds.collectivesim.overlay.service.ServiceDispatcher;
import edu.upc.cnds.collectivesim.overlay.service.ServiceProviderAgent;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;

public class AdaptiveServiceProviderAgentFactory extends OverlayAgentFactory {

	protected Factory<UtilityFunction> utilityFunctionFactory;
	
	protected Factory<ServiceDispatcher> serviceDispatcherFactory;
	
	protected Factory<Stream<Double>>loadStreamFactory;
	
	protected Factory<Integer>capacityFactory;
	
    protected Factory<AdaptationFunction>adaptationFunctionFactory;
    
    
	public AdaptiveServiceProviderAgentFactory(OverlayFactory overlayFactory,
					Factory<UtilityFunction> utilityFunctionFactory, 
					Factory<ServiceDispatcher> serviceDispatcherFactory,
					Factory<AdaptationFunction>adaptationFunctionFactory,
					Factory<Integer>capacityFactory,
					Factory<Stream<Double>>loadStreamFactory) {		

		super(overlayFactory);
		
		this.utilityFunctionFactory = utilityFunctionFactory;
		this.serviceDispatcherFactory = serviceDispatcherFactory;
		this.loadStreamFactory = loadStreamFactory;
		this.capacityFactory = capacityFactory;
		this.adaptationFunctionFactory = adaptationFunctionFactory;

		
	}
	
	@Override
	protected OverlayAgent createOverlayAgent(Overlay overlay) {		
					
		return new AdaptiveServiceProviderAgent(overlay,
				utilityFunctionFactory.nextElement(),
                serviceDispatcherFactory.nextElement(),
                adaptationFunctionFactory.nextElement(),
                capacityFactory.nextElement(), 
                loadStreamFactory.nextElement());


	}
}

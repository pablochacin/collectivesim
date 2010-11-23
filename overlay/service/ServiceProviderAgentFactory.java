package edu.upc.cnds.collectivesim.overlay.service;

import edu.upc.cnds.collectives.adaptation.function.AdaptationFunction;
import edu.upc.cnds.collectives.factory.Factory;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayAgentFactory;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;

public class ServiceProviderAgentFactory extends OverlayAgentFactory {

	protected Factory<UtilityFunction> utilityFunctionFactory;
	
	protected Factory<ServiceDispatcher> serviceDispatcherFactory;
	
	protected Factory<Stream<Double>>loadStreamFactory;
	
	protected Factory<Integer>capacityFactory;
	
	private Factory<Double> windowFactory;
    
    
	public ServiceProviderAgentFactory(OverlayFactory overlayFactory,
					Factory<UtilityFunction> utilityFunctionFactory, 
					Factory<ServiceDispatcher> serviceDispatcherFactory,
					Factory<Integer>capacityFactory,
					Factory<Double>windowFactory,
					Factory<Stream<Double>>loadStreamFactory) {		

		super(overlayFactory);
		
		this.utilityFunctionFactory = utilityFunctionFactory;
		this.serviceDispatcherFactory = serviceDispatcherFactory;
		this.loadStreamFactory = loadStreamFactory;
		this.capacityFactory = capacityFactory;
		this.windowFactory = windowFactory;
		
	}
	
	@Override
	protected ServiceProviderAgent createOverlayAgent(Overlay overlay) {		
					
		return new ServiceProviderAgent(overlay,
				utilityFunctionFactory.nextElement(),
                serviceDispatcherFactory.nextElement(),
                capacityFactory.nextElement(), 
                windowFactory.nextElement(),
                loadStreamFactory.nextElement());

		
		//ADD adaptation actions
	}
}

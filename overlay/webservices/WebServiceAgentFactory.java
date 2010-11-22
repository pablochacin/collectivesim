package edu.upc.cnds.collectivesim.overlay.webservices;

import edu.upc.cnds.collectives.adaptation.function.AdaptationFunction;
import edu.upc.cnds.collectives.factory.Factory;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayAgentFactory;
import edu.upc.cnds.collectivesim.overlay.service.ServiceDispatcher;
import edu.upc.cnds.collectivesim.overlay.service.ServiceProviderAgent;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;

public class WebServiceAgentFactory extends OverlayAgentFactory {

	protected Factory<UtilityFunction> utilityFunctionFactory;
	
	protected Factory<ServiceDispatcher> serviceDispatcherFactory;
	
	protected Factory<Stream<Double>>loadStreamFactory;
	
	protected Stream<Integer>capacityStream;
	
    protected Factory<AdaptationFunction>adaptationFunctionFactory;
    
	public WebServiceAgentFactory(OverlayFactory overlayFactory,
            Factory<UtilityFunction> utilityFunctionFactory,
            Factory<ServiceDispatcher> serviceDispatcherFactory,
            Factory<Stream<Double>>loadStreamFactory,
            Stream<Integer>capacityStream,
            Factory<AdaptationFunction>adaptationFunctionFactory) {		

		super(overlayFactory);
		
		this.utilityFunctionFactory = utilityFunctionFactory;
		this.serviceDispatcherFactory = serviceDispatcherFactory;
		this.loadStreamFactory = loadStreamFactory;
		this.capacityStream = capacityStream;
	}
	
	@Override
	protected OverlayAgent createOverlayAgent(Overlay overlay) {		
					
		return new ServiceProviderAgent(overlay,utilityFunctionFactory.nextElement(),
                capacityStream.nextElement(), 
                serviceDispatcherFactory.nextElement(),
                loadStreamFactory.nextElement());
	}



}

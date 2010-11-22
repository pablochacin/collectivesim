package edu.upc.cnds.collectivesim.overlay.service;

import edu.upc.cnds.collectives.factory.CloningFactory;
import edu.upc.cnds.collectives.factory.Factory;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayAgentFactory;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityAgentFactory;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * 
 * Factory for ServiceProviderAgent.
 * 
 * 
 * @author Pablo Chacin
 *
 */
public class ServiceProviderAgentFactory extends OverlayAgentFactory{
	
	protected Factory<UtilityFunction> utilityFunctionFactory;
					
	protected Factory<ServiceDispatcher> serviceDispatcherFactory;
	
	protected Factory<Stream<Double>> loadStreamFactory;
	
	protected Stream<Integer>capacityStream;
	
	/**
	 * 
	 * @param overlayFactory
	 * @param utilityFunctionFactory
	 * @param serviceDispatcherFactory
	 * @param loadStream Stream to generate the background load. Is clonned to give a copy
	 *        to each agent
	 * @param capacityStream
	 */
	public ServiceProviderAgentFactory(OverlayFactory overlayFactory,
			                           Factory<UtilityFunction> utilityFunctionFactory,
			                           Factory<ServiceDispatcher> serviceDispatcherFactory,
			                           Factory<Stream<Double>> loadStreamFactory,
			                           Stream<Integer>capacityStream) {
		
		super(overlayFactory);
		
		this.utilityFunctionFactory = utilityFunctionFactory;
		this.serviceDispatcherFactory = serviceDispatcherFactory;
		this.capacityStream = capacityStream;
		this.loadStreamFactory = loadStreamFactory;
	}

	
	/**
	 * Creates a ServiceProviderAgent from the overlay components
	 */
	@Override	
	protected OverlayAgent createOverlayAgent(Overlay overlay) {		
						
		return new ServiceProviderAgent(overlay,utilityFunctionFactory.nextElement(),
				                        capacityStream.nextElement(), 
				                        serviceDispatcherFactory.nextElement(),
				                        loadStreamFactory.nextElement());
				
	}



}

package edu.upc.cnds.collectivesim.overlay.service;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityAgentFactory;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * 
 * Factory for GradientTopologyAgents that maintains a gradient topology.
 * 
 * @author Pablo Chacin
 *
 */
public class ServiceProviderAgentFactory extends UtilityAgentFactory {
					
	
	public ServiceProviderAgentFactory(OverlayFactory factory, Underlay underlay,
			                   Stream<Identifier>ids,
			                   UtilityFunction function) {
		super(factory, underlay,ids,function);
	}

	
	/**
	 * Creates a ServiceProviderAgent from the overlay components
	 */
	@Override	
	protected OverlayAgent createOverlayAgent(OverlayModel model, Overlay overlay) {		
		
				
		return new ServiceProviderAgent(model,overlay,overlay.getLocalNode().getId(),function);
				
	}



}

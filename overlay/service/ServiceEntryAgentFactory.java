package edu.upc.cnds.collectivesim.overlay.service;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayAgentFactory;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * 
 * Factory for GradientTopologyAgents that maintains a gradient topology.
 * 
 * @author Pablo Chacin
 *
 */
public class ServiceEntryAgentFactory extends OverlayAgentFactory {
					
	
	protected Stream<Double>preference;
	
	protected Stream<Double>tolerance;

	public ServiceEntryAgentFactory(OverlayFactory factory, 
			                   Stream<Double> preference,
			                   Stream<Double> tolerance) {
		
			super(factory);
							   
			this.preference = preference;
			this.tolerance = tolerance;
	}

	/**
	 * Creates an OverlayAgent from the overlay components
	 * 
	 * @param overlay
	 * @param router
	 * @param randomTopology
	 * @return
	 * @throws ModelException 
	 */
	@Override
	protected OverlayAgent createOverlayAgent(Overlay overlay) {		
		
		    String[] attributes = {"Id","Utility"};
		    	
			return new ServiceEntryAgent(overlay,preference.nextElement(),tolerance.nextElement());
				
	}



}

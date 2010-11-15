package edu.upc.cnds.collectivesim.overlay.service;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayAgentFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
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
			                   Stream<Identifier>ids,
			                   Stream<Double> preference,
			                   Stream<Double> tolerance) {
		
			super(factory, ids);
							   
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
	protected OverlayAgent createOverlayAgent(OverlayModel model, Overlay overlay) {		
		
		    String[] attributes = {"Id","Utility"};
		    	
			return new ServiceEntryAgent(model,overlay,preference.nextElement(),tolerance.nextElement());
				
	}



}

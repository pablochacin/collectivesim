package edu.upc.cnds.collectivesim.overlay.service;

import java.util.Map;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.service.ServiceOverlayAgent;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityAgentFactory;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * 
 * Factory for GradientTopologyAgents that maintains a gradient topology.
 * 
 * @author Pablo Chacin
 *
 */
public class ServiceAgentFactory extends UtilityAgentFactory {
					
	

	public ServiceAgentFactory(OverlayFactory factory, Underlay underlay) {
		super(factory, underlay);
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
	protected OverlayAgent createOverlayAgent(OverlayModel model, Overlay overlay, Map attributes) {		
		
		String role = (String)attributes.get("role");	
		
		if(role.equals("entry")){
			return new ServiceEntryAgent(model,overlay, attributes);
		}

		return new ServiceOverlayAgent(model,overlay,attributes);
		
		
	}



}

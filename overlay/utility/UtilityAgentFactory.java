package edu.upc.cnds.collectivesim.overlay.utility;

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
public class UtilityAgentFactory extends OverlayAgentFactory {

	protected UtilityFunction function;
	
	public UtilityAgentFactory(OverlayFactory factory, Underlay underlay,
			                   Stream<Identifier>ids,UtilityFunction function) {
		super(factory, underlay,ids);
		
		this.function = function;
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
	protected OverlayAgent createOverlayAgent(OverlayModel model,Overlay overlay) {	
		
		return new UtilityOverlayAgent(model,overlay,overlay.getLocalNode().getId(),function);

	}
	





}

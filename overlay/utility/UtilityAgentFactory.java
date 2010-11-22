package edu.upc.cnds.collectivesim.overlay.utility;

import edu.upc.cnds.collectives.factory.Factory;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayAgentFactory;

/**
 * 
 * Factory for UtilityOverlayAgent.
 * 
 * @author Pablo Chacin
 *
 */
public class UtilityAgentFactory extends OverlayAgentFactory {



	protected Factory<UtilityFunction> utilityFunctionFactory;

	/**
	 * Constructor 
	 * 
	 * @param overlayFactory
	 * @param utilityFunctionFactory
	 */
	public UtilityAgentFactory(OverlayFactory overlayFactory,
			Factory<UtilityFunction> utilityFunctionFactory){		

		super(overlayFactory);

		this.utilityFunctionFactory = utilityFunctionFactory;
	}

	@Override
	/**
	 * Creates an OverlayAgent from the overlay components
	 */
	protected OverlayAgent createOverlayAgent(Overlay overlay) {			

		return new UtilityOverlayAgent(overlay,utilityFunctionFactory.nextElement());
	}



}

package edu.upc.cnds.collectivesim.overlay.utility;

import com.sun.org.apache.xpath.internal.functions.Function;

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
 * Factory for UtilityOverlayAgent.
 * 
 * @author Pablo Chacin
 *
 */
public class UtilityAgentFactory extends OverlayAgentFactory {

	
	
	protected Stream<Double>utility;
	
	protected Stream<Double>initialTrend;
    
	protected Stream<Double> drift;
	
	protected Stream<Double>variation;
	
	protected Stream<Double>trend;
	
	
	public UtilityAgentFactory(OverlayFactory factory, Underlay underlay,
			                   Stream<Identifier>ids,Stream<Double>utility,Stream<Double>initialTrend,
			                   Stream<Double>drift,Stream<Double>variation,Stream<Double>trend) {
		
		super(factory, underlay,ids);
		
		this.utility = utility;
		this.initialTrend = initialTrend;
		this.drift =drift;
		this.variation = variation;
		this.trend = trend;
		
	}

	@Override
	/**
	 * Creates an OverlayAgent from the overlay components
	 */
	protected OverlayAgent createOverlayAgent(OverlayModel model,Overlay overlay) {			
		
			
			
		return new UtilityOverlayAgent(model,overlay,overlay.getLocalNode().getId(),getUtilityFunction());
	}
	

	protected UtilityFunction getUtilityFunction() {
		
		UtilityFunction function = new RandomUtilityFunction(utility.nextElement(), 
                initialTrend.nextElement(),
                variation, drift.nextElement(), 
                trend);

		return function;
	}
}

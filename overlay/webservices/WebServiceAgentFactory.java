package edu.upc.cnds.collectivesim.overlay.webservices;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayAgentFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.utility.ResponseTimeUtilityFunction;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;

public class WebServiceAgentFactory extends OverlayAgentFactory {

	
	protected Integer requestLimit;
	
	protected Double serviceRate;
	
	protected Double targetServiceTime;
	
	protected Double alpha;
	
	protected Stream<Double> load;
	
	public WebServiceAgentFactory(OverlayFactory factory, Underlay underlay,
			Stream<Identifier> ids,Integer requestLimit,Double serviceRate,
			Double targetServiceTime,Double alpha,Stream<Double>load) {
		
		super(factory, underlay, ids);
		this.requestLimit = requestLimit;
		this.serviceRate = serviceRate;
		this.targetServiceTime = targetServiceTime;
		this.alpha = alpha;
		this.load = load;
	}

	
	@Override
	protected OverlayAgent createOverlayAgent(OverlayModel model, Overlay overlay) {		
		
			return new WebServiceAgent(model,overlay,overlay.getLocalNode().getId(),
					                   getUtilityFunction(),requestLimit,serviceRate,load.nextElement());
				
	}


	private UtilityFunction getUtilityFunction() {
	
		return new ResponseTimeUtilityFunction(targetServiceTime,alpha);
	}
}

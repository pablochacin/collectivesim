package edu.upc.cnds.collectivesim.overlay.webservices;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectivesim.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.service.ServiceProviderAgentFactory;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;

public class WebServiceAgentFactory extends ServiceProviderAgentFactory {

	public WebServiceAgentFactory(OverlayFactory factory, Underlay underlay,
			Stream<Identifier> ids, UtilityFunction function) {
		
		super(factory, underlay, ids, function);
		
	}

}

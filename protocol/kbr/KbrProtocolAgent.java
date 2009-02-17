package edu.upc.cnds.collectivesim.protocol.kbr;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.protocol.Destination;
import edu.upc.cnds.collectives.protocol.ProtocolException;
import edu.upc.cnds.collectives.routing.kbr.KbrProtocolImp;
import edu.upc.cnds.collectivesim.model.Stream;
import edu.upc.cnds.collectivesim.protocol.ProtocolModelAgent;

public class KbrProtocolAgent extends ProtocolModelAgent {

	public KbrProtocolAgent(KbrProtocolImp protocol) {
		super(protocol);
	}

	public void route(Stream<Identifier>identifiers){
		Identifier key = identifiers.getValue();
		Map destinationKey = new HashMap();
		destinationKey.put("key",key);
		Destination destination = new Destination(destinationKey);
		
		try {
			protocol.propagate(destination);
		} catch (ProtocolException e) {
			log.warning("Exception " +e.getClass().getName() +" routing key "+key.toString());
		}
	}
}

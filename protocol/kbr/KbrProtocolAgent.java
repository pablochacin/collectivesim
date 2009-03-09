package edu.upc.cnds.collectivesim.protocol.kbr;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.protocol.Destination;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.protocol.ProtocolException;
import edu.upc.cnds.collectives.routing.Route;
import edu.upc.cnds.collectives.routing.RouteObserver;
import edu.upc.cnds.collectives.routing.RoutingProtocol;
import edu.upc.cnds.collectivesim.protocol.ProtocolModel;
import edu.upc.cnds.collectivesim.protocol.ProtocolModelAgent;

public class KbrProtocolAgent extends ProtocolModelAgent implements RouteObserver{

	private KbrProtocolModel kbrmodel; 
	
	public KbrProtocolAgent(RoutingProtocol protocol,KbrProtocolModel model) {
		super(protocol,model);
		this.kbrmodel = model;
		protocol.addRouteObserver(this);
	}

	
	/**
	 * Propagate a request to a destination identified by an identifier
	 * 
	 * @param identifiers
	 */
	public void route(Identifier key, Integer ttl){
		Map destinationKey = new HashMap();
		destinationKey.put("key",key);
		Destination destination = new Destination(destinationKey);
		
		try {
			propagate(destination);
		} catch (ProtocolException e) {
			log.warning("Exception " +e.getClass().getName() +" routing key "+key.toString());
		}
	}


	@Override
	public void forwarded(Protocol protocol, Node node,
			Destination destination, Route route, Serializable... args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void routed(Protocol protocol, Node node, Destination destination, Route route, Serializable... args) {
		kbrmodel.reportDelivered(node, protocol.getName(),destination,route);
	}
	
	


}

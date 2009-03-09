package edu.upc.cnds.collectivesim.protocol.kbr;

import java.util.Map;

import edu.upc.cnds.collectives.events.imp.BasicEvent;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.protocol.Destination;
import edu.upc.cnds.collectives.routing.Route;
import edu.upc.cnds.collectivesim.dataseries.DataItem;

/**
 * Informs of a Routing related event.
 * 
 * The event's attributes are 
 * <ul>
 * <li> protocol the name of the protocol delivering the request
 * <li> source: the node id of the originator
 * <li> target: the node id of the node receiving the request
 * <li> hops: the number of hops of the route
 * <li> the Destination's attributes, converted to String and with "destination.", appended to the name.
 *  
 * @author Pablo Chacin
 *
 */
public class RoutingModelEvent extends BasicEvent {

	public static String  REQUEST_ROUTED = "protocol.routing.routed";
	
	private Route route ;
	
	private Destination destination;
	
	public RoutingModelEvent(Node node, long timeStamp,Map attributes,String protocol, 
			                 Destination destination,Route route) {
		super(node, REQUEST_ROUTED, timeStamp,attributes);
		this.destination = destination;
		this.route = route;
		
	}
	
	
	public Destination getDestination(){
		return destination;
	}
	
	public Route getRoute(){
		return route;
	}

}

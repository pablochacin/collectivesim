package edu.upc.cnds.collectivesim.overlay.webservices;

import java.util.Map;

import edu.upc.cnds.collectives.events.imp.BasicEvent;
import edu.upc.cnds.collectives.node.Node;


public class ServiceEvent extends BasicEvent {

	
	public static String SERVICE_EVENT_SERVICED= "collectives.service.serviced";
	
	public ServiceEvent(Node node, long timeStamp, Map attributes, Object... data) {
		super(node, SERVICE_EVENT_SERVICED, timeStamp, attributes, data);
	}

	public ServiceEvent(Node node, long timeStamp, Object... data) {
		super(node, SERVICE_EVENT_SERVICED, timeStamp, data);
	}

	public ServiceEvent(Node node, long timeStamp) {
		super(node, SERVICE_EVENT_SERVICED, timeStamp);
	}

}

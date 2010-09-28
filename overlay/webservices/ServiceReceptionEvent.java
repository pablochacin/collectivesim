package edu.upc.cnds.collectivesim.overlay.webservices;

import java.util.Map;

import edu.upc.cnds.collectives.events.imp.BasicEvent;
import edu.upc.cnds.collectives.node.Node;


public class ServiceReceptionEvent extends BasicEvent {	
	
	public static String SERVICE_EVENT_RECIEVED= "collectives.service.recieved";
	
	public ServiceReceptionEvent(Node node, long timeStamp, Map attributes, Object... data) {
		super(node, SERVICE_EVENT_RECIEVED, timeStamp, attributes, data);
	}

	public ServiceReceptionEvent(Node node, long timeStamp, Object... data) {
		super(node, SERVICE_EVENT_RECIEVED, timeStamp, data);
	}

	public ServiceReceptionEvent(Node node, long timeStamp) {
		super(node, SERVICE_EVENT_RECIEVED, timeStamp);
	}

}

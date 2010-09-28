package edu.upc.cnds.collectivesim.overlay.webservices;

import java.util.Map;

import edu.upc.cnds.collectives.events.imp.BasicEvent;
import edu.upc.cnds.collectives.node.Node;


public class ServiceExecutionEvent extends BasicEvent {	
	
	public static String SERVICE_EVENT_SERVICED= "collectives.service.processed";
	
	public ServiceExecutionEvent(Node node, long timeStamp, Map attributes, Object... data) {
		super(node, SERVICE_EVENT_SERVICED, timeStamp, attributes, data);
	}

	public ServiceExecutionEvent(Node node, long timeStamp, Object... data) {
		super(node, SERVICE_EVENT_SERVICED, timeStamp, data);
	}

	public ServiceExecutionEvent(Node node, long timeStamp) {
		super(node, SERVICE_EVENT_SERVICED, timeStamp);
	}

}

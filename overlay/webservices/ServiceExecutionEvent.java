package edu.upc.cnds.collectivesim.overlay.webservices;

import java.util.Map;

import edu.upc.cnds.collectives.events.imp.BasicEvent;
import edu.upc.cnds.collectivesim.overlay.service.ServiceProviderAgent;


public class ServiceExecutionEvent extends BasicEvent {	
	
	public static String SERVICE_EVENT_SERVICED= "collectives.service.processed";
	
	public ServiceExecutionEvent(ServiceProviderAgent agent, long timeStamp, Map attributes ) {
		super(agent.getName(), timeStamp,SERVICE_EVENT_SERVICED,  attributes);
	}

}

package edu.upc.cnds.collectivesim.overlay.webservices;

import java.util.Map;

import edu.upc.cnds.collectives.events.imp.BasicEvent;
import edu.upc.cnds.collectivesim.overlay.service.ServiceOverlayAgent;


public class ServiceReceptionEvent extends BasicEvent {	
	
	public static String SERVICE_EVENT_RECIEVED= "collectives.service.recieved";
	
	public ServiceReceptionEvent(ServiceOverlayAgent agent, long timeStamp, Map attributes) {
		super(agent.getName(), timeStamp,SERVICE_EVENT_RECIEVED,  attributes);
	}


	public ServiceReceptionEvent(ServiceOverlayAgent agent, long timeStamp) {
		super(agent.getName(), timeStamp, SERVICE_EVENT_RECIEVED);
	}

}

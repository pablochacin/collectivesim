package edu.upc.cnds.collectivesim.overlay.webservices;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.events.imp.BasicEvent;
import edu.upc.cnds.collectivesim.overlay.service.ServiceProviderAgent;
import edu.upc.cnds.collectivesim.overlay.service.ServiceRequest;


public class ServiceReceptionEvent extends BasicEvent {	
	
	public static String SERVICE_EVENT_RECIEVED= "collectives.service.recieved";
	
	public ServiceReceptionEvent(ServiceProviderAgent agent, ServiceRequest request, Map attributes) {
		super(agent.getName(), agent.getModel().getCurrentTime(), SERVICE_EVENT_RECIEVED);
		
		Map serviceAttr = new HashMap();

		attributes.put("request.qos",request.getQoS());
		attributes.put("service.capacity",agent.getCapacity());	
		attributes.put("service.load",agent.getLoad());		
		attributes.put("service.node",agent.getLocalNode().getId().toString());
		attributes.put("service.utilty", agent.getUtility());
		
		this.setAttributes(attributes);
	}


	public ServiceReceptionEvent(ServiceProviderAgent agent, long timeStamp) {
		super(agent.getName(), timeStamp, SERVICE_EVENT_RECIEVED);
	}

}

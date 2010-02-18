package edu.upc.cnds.collectivesim.overlay.service;

import java.io.Serializable;

/**
 * Describes a service request
 * 
 * @author Pablo Chacin
 *
 */
public class ServiceRequest implements Serializable {

	/**
	 * Target utility for the request
	 */
	Double qos;


	Double serviceDemand;
	
	public ServiceRequest(Double qos,Double serviceDemand) {
		this.qos = qos;
		this.serviceDemand= serviceDemand;
		

	}

	
	public Double getServiceDemand() {
		return serviceDemand;
	}
	
	public Double getQoS() {
		return qos;
	}

	
	public String toString(){
		return "[QoS="+qos + "][Service Demand="+serviceDemand+"]";
	}
	
}

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


	public ServiceRequest(Double qos) {
		this.qos = qos;

	}

	public Double getQoS() {
		return qos;
	}

	
	public String toString(){
		return "[QoS="+qos + "]";
	}
	
}

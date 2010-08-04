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

	/**
	 * Accepted tolerance
	 */
	Double tolerance;

	Double serviceDemand;
	
	public ServiceRequest(Double qos,Double tolerance,Double serviceDemand) {
		this.qos = qos;
		this.tolerance = tolerance;
		this.serviceDemand= serviceDemand;
		

	}

	
	public Double getServiceDemand() {
		return serviceDemand;
	}
	
	public Double getQoS() {
		return qos;
	}

	public Double getTolerance(){
		return tolerance;
	}
	
	public String toString(){
		return "[QoS="+qos + "][Tolerance="+tolerance +"][Service Demand="+serviceDemand+"]";
	}
	
}

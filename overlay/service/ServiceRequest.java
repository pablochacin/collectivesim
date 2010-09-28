package edu.upc.cnds.collectivesim.overlay.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.util.FormattingUtils;

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
	 * Map of attributes intended to be used by dispatching policies
	 */
	Map attributes;
	
	public ServiceRequest(Double qos, Map attributes) {
		this.qos = qos;
		this.attributes = attributes;

	}

	/**
	 * Convenience constructor without attributes
	 * @param qos
	 * @param attributes
	 */
	public ServiceRequest(Double qos) {
		this(qos,new HashMap());
	}	
	
		
	public Double getQoS() {
		return qos;
	}
	
	public Map getAttributes(){
		return attributes;
	}
	
	public String toString(){
		return "[QoS="+qos + "][attributes = " +
		       FormattingUtils.mapToString(attributes) + "]";
	}


}

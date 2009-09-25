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
	Double utility;

	/**
	 * Duration of the request
	 */
	Long duration;

	public ServiceRequest(Double utility, Long duration) {
		super();
		this.utility = utility;
		this.duration = duration;
	}

	public Double getUtility() {
		return utility;
	}

	public Long getDuration() {
		return duration;
	}
	

	public String toString(){
		return "[utility="+utility + "][duration="+duration+"]";
	}
	
}

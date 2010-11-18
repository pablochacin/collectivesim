package edu.upc.cnds.collectivesim.overlay.service;

/**
 * Handles the execution of ServiceRequests and tracks their performance
 * 
 * @author Pablo Chacin
 *
 */
public interface ServiceDispatcher {

	public void processRequest(ServiceRequest request);
	
	public void dispatchRequests();
	
	public void setContainer(ServiceContainer container);
	
	public Double getLoad();
	
	public Double getArrivals();
	
	public Double getResponseTime();
	
	public Double getOfferedDemand();
}

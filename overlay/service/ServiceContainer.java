package edu.upc.cnds.collectivesim.overlay.service;

/**
 * Container for a Service ServiceDispatcher
 * 
 * @author Pablo Chacin
 *
 */
public interface ServiceContainer{

	/**
	 * Handle the completion of a ServiceRequest
	 * 
	 * @param request
	 */
	public void handleCompletion(ServiceRequest request);
	
	/**
	 * Returns the available CPU for the ServiceDispatcher
	 * @return
	 */
	public Double getAvailableCpu();

	public Long getCurrentTime();
	
}

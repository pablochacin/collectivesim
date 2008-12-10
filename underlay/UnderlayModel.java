package edu.upc.cnds.collectivesim.underlay;

import java.util.Set;


import edu.upc.cnds.collectives.underlay.Metric;
import edu.upc.cnds.collectives.underlay.MetricType;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayAddress;

/**
 * Implements a simulated UnderlayNode. Delegates most functions to the UnderlayModel
 * 
 * @author Pablo Chacin
 *
 */
public interface UnderlayModel {

		
	/**
	 * 
	 * @param node
	 * @return a proxy for the given address.
	 */
	public Underlay getUnderlayModelProxy(UnderlayAddress address);
	
	/**
	 * 
	 * @return a set with the supported metrics
	 */
	Set<MetricType> getSupportedMetrics();
	
	
	/**
	 * Returns a list of metrics for the conecction between two given addresses
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public Metric probe(UnderlayAddress source,UnderlayAddress target,MetricType metrics);
}

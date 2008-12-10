package edu.upc.cnds.collectivesim.underlay;

import java.util.Set;

import edu.upc.cnds.collectives.underlay.Metric;
import edu.upc.cnds.collectives.underlay.MetricType;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayAddress;

/**
 * Simulates an underlay
 * 
 * @author Pablo Chacin
 *
 */
public class UnderlayModelProxy implements Underlay {

	private UnderlayModel underlay;
	private UnderlayAddress localAddress;
	
	public Set<MetricType> getSupportedMetrics() {
		return underlay.getSupportedMetrics();
	}

	public Metric probe(UnderlayAddress address, MetricType metric) {
		return underlay.probe(localAddress, address, metric);
	}
	

	


}

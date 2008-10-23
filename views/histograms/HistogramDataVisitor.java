package edu.upc.cnds.collectivesim.views.histograms;

import uchicago.src.sim.analysis.BinDataSource;

/**
 * Provides the information of an agent
 * 
 * @author Pablo Chacin
 *
 */
 public class HistogramDataVisitor implements BinDataSource {

	/**
	 * @param value and Object reference to the agent whose
	 *        value must be calculated
	 */
	public double getBinValue(Object value){
	   
	    return  ((Double)value).doubleValue();
    }

}

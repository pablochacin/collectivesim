package edu.upc.cnds.collectivesim.visualization.charts.ptplot;

import java.util.Map;

import ptolemy.plot.PlotBox;
import edu.upc.cnds.collectivesim.dataseries.DataSeriesObserver;
import edu.upc.cnds.collectivesim.visualization.charts.UnsoportedChartProperty;

/**
 * A sequence displayed in a Plot. Adds the new data items to the corresponding
 * plot. 
 * 
 * If the plot is in auto-update mode, it receives the updates from 
 * the DataSeries.
 * 
 * @author Pablo Chacin
 *
 */
public interface PtPlotSequence extends DataSeriesObserver{

	/**
	 * Refresh the display of the sequence in the plot
	 */
	void update();
	
	
	/**
	 * Set the display properties of the sequence.
	 * 
	 * @param plot
	 * @param sequence
	 * @param properties
	 * @throws UnsoportedChartProperty
	 */
	void setSequenceProperties(PlotBox plot, int sequence, Map properties) throws UnsoportedChartProperty;
		
}

package collectivesim.visualization.charts.ptplot;

import java.util.Map;

import ptolemy.plot.Plot;
import ptolemy.plot.PlotBox;
import collectivesim.dataseries.DataItem;
import collectivesim.dataseries.DataSequence;
import collectivesim.dataseries.DataSeries;
import collectivesim.visualization.charts.UnsoportedChartProperty;

	/**
	 * Display the values of a sequence in the plot. 
	 * 
	 * @author Pablo Chacin
	 *
	 */
	public abstract class AbstractPtPlotSequence implements PtPlotSequence {


		/**
		 * Source of items for the Plot
		 */
		protected DataSeries series;
		
		/**
		 * Attribute used in this sequence
		 */
		protected String valueAttribute;
		
		/**
		 * Indicates the position of the item in the plot
		 */
		protected String sequenceAttribute;
		
		/**
		 * Number used in the Plot to identify this sequence
		 */
		protected int sequenceNumber;
		
		/**
		 * Number of items in the sequence
		 */
		protected int itemNum = 0;
		
		protected AbstractPtPlotChart chart;
		

							
		public AbstractPtPlotSequence(AbstractPtPlotChart chart,String name, DataSeries series, String sequenceAttribute,String valueAttribute,int sequenceNumber) throws UnsoportedChartProperty {
			this.chart = chart;
			this.series = series;
			this.valueAttribute = valueAttribute;
			this.sequenceAttribute = sequenceAttribute;
			this.sequenceNumber = sequenceNumber; 
		}

		
		/**
		 * Refreshes the plot with all the available elements in the 
		 */
		public void update() {
			DataSequence sequence = series.getSequence();
			
			while(sequence.hasItems()){
				addPoint(sequence.getItem());
			}
		}


		@Override
		public void update(DataItem item) {
			addPoint(item);
			
			//call the refresh at the plot to refresh all other series and
			//repaint the plot.
			chart.refresh();
		}


		@Override
		public abstract void setSequenceProperties(PlotBox plot, int sequence,Map properties) throws UnsoportedChartProperty;

		
		/**
		 * Adds a point to the plot
		 * @param item
		 */
		protected abstract void addPoint(DataItem item);
	}
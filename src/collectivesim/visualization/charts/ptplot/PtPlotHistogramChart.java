package collectivesim.visualization.charts.ptplot;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ptolemy.plot.Histogram;
import ptolemy.plot.PlotBox;
import collectivesim.parameters.Parameter;
import collectivesim.parameters.ParameterList;
import collectivesim.dataseries.DataItem;
import collectivesim.dataseries.DataSeries;
import collectivesim.visualization.charts.UnsoportedChartProperty;

public class PtPlotHistogramChart extends AbstractPtPlotChart {

 	protected static Parameter<Double> BIN_WIDTH = new Parameter<Double>("plot.histogram.binWidth",false,new Double(0.0));


	/**
	 * Displays a sequence in a Histogram. 
	 *  
	 * @author Pablo Chacin
	 *
	 */
	private class PtPlotHistogramSequence extends AbstractPtPlotSequence {
		
		protected Histogram histogram;

		public PtPlotHistogramSequence(AbstractPtPlotChart chart, ptolemy.plot.Histogram histogram, String name,
				DataSeries series, String sequenceAttribute,String valueAttribute,int sequenceNumber) 	throws UnsoportedChartProperty {
			super(chart, name, series, sequenceAttribute,valueAttribute, sequenceNumber);
			
			this.histogram = histogram;
			this.histogram.addLegend(sequenceNumber, name);
		}

		
		@Override
		protected void addPoint(DataItem item) {
			
			histogram.addPoint(sequenceNumber,++itemNum,item.getDouble(valueAttribute),true);

		}

		@Override
		public void setSequenceProperties(PlotBox plot, int sequence,
				Map properties) throws UnsoportedChartProperty {
						
		}
		
	}
	
	
	protected ptolemy.plot.Histogram histogram;
	
	public PtPlotHistogramChart() throws UnsoportedChartProperty {
		this("","",new HashMap(),false,false,false);
	}

	public PtPlotHistogramChart(boolean autoupdate) throws UnsoportedChartProperty {
		this("","",new HashMap(),autoupdate,false,false);
	}

	public PtPlotHistogramChart(String name,String title, Map properties, boolean autoupdate, boolean autoclean,boolean autoresize) throws UnsoportedChartProperty {
		super(name,title, properties,autoupdate,autoclean,true);
	}

	public PtPlotHistogramChart(String name,String title, Map properties, boolean autoupdate) throws UnsoportedChartProperty {
		this(name,title, properties, autoupdate,false,false);
	}

	
	@Override
	protected PlotBox createPtPlotBox() {
		histogram = new Histogram();
		return histogram;
	}


	@Override
	protected PtPlotSequence createSequence(AbstractPtPlotChart plot,
			String name, DataSeries series, String sequenceAttribute,
			String valueAttribute, int seqNum) throws UnsoportedChartProperty {
		
		return new PtPlotHistogramSequence(plot, histogram, name, series,sequenceAttribute,valueAttribute, seqNum);
	}

	@Override
	public boolean needRefresh() {

		//TODO: optimize by checking if the chart needs refresh
		return true;
	}


	public void setProperties(Map properties) throws UnsoportedChartProperty {
		
		//let the parent process the properties
		super.setProperties(properties);
		
		Parameter<Double> binWidth = new Parameter(BIN_WIDTH);

		ParameterList  parameters = new ParameterList();
		parameters.addParameter(binWidth);
		
		parameters.load(properties);
		
		//If a with is specified for the bins, also set the offset so that
		//the n-th bin covers the values [with*n,with*(n+1))
		if(binWidth.getValue() > 0){
			histogram.setBinWidth(binWidth.getValue());
			histogram.setBinOffset(binWidth.getValue()/2);
		}
			
	}

}

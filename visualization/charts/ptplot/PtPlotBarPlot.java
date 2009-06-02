package edu.upc.cnds.collectivesim.visualization.charts.ptplot;


import java.util.HashMap;
import java.util.Map;

import ptolemy.plot.PlotBox;
import ptolemy.plot.Plot;

import edu.upc.cnds.collectives.util.Parameter;
import edu.upc.cnds.collectives.util.ParameterList;
import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.visualization.charts.UnsoportedChartProperty;


/**
 * Draws a sequence of data items as a bar.
 * 
 *  
 * @author Pablo Chacin
 *
 */
public class PtPlotBarPlot extends AbstractPtPlotChart {

	/* *************************************************************
	 * Parameters
	 * **************************************************************/
	
	/**
	 * Size of the displayed sequence. If a non zero N value is given, only the N latest
	 * values added to the plot are kept
	 */
 	protected static Parameter<Double> BAR_WIDTH= new Parameter<Double>("plot.bar.width",false,new Double(1.0));

 	protected static Parameter<Double> BAR_OFFSET= new Parameter<Double>("plot.bar.offset",false,new Double(0.0));
 

	/**
	 * Displays a sequence in a LinePlot
	 * 
	 * @author Pablo Chacin
	 *
	 */
	private class PtPlotBarSequence extends AbstractPtPlotSequence {
				
		protected Plot ptplot;
		
		
		public PtPlotBarSequence(AbstractPtPlotChart chart, Plot ptplot, String name,
				DataSeries series, String sequenceAttribute,String valueAttribute,int sequenceNumber) 	throws UnsoportedChartProperty {
			super(chart, name, series, valueAttribute,sequenceAttribute,sequenceNumber);
			
			this.ptplot = ptplot;
			this.ptplot.addLegend(sequenceNumber, name);

		}

		@Override
		protected void addPoint(DataItem item) {
			
			double bar = item.getDouble(sequenceAttribute);
			double value = item.getDouble(valueAttribute);
		
			
			ptplot.addPoint(sequenceNumber,bar,value,false);
			
		}

		@Override
		public void setSequenceProperties(PlotBox plot, int sequence,
				Map properties) throws UnsoportedChartProperty {
					

		}
		
	}
	
	/**
	 * PtPlot used to display data point
	 */
	protected Plot ptplot;
	
	
	/**
	 * 
	 * @param title
	 * @param properties
	 * @param autoupdate
	 * @param autoclean
	 * @param autoResize
	 * @throws UnsoportedChartProperty
	 */
	public PtPlotBarPlot(String title,Map properties,boolean autoupdate,boolean autoclean,boolean autoResize) throws UnsoportedChartProperty {

		super(title,properties,autoupdate,autoclean,autoResize);
				
	}

	public PtPlotBarPlot(String title,boolean autoupdate,boolean autoclean,boolean autoResize) throws UnsoportedChartProperty {
		super(title,autoupdate,autoclean,autoResize);
	}
	
		
	public PtPlotBarPlot() throws UnsoportedChartProperty {
		this("",new HashMap(),false,false,true);
	}

	public PtPlotBarPlot(boolean autoupdate) throws UnsoportedChartProperty{
		this("",new HashMap(),autoupdate,false,true);
	}
	

	/**
	 * Sets the properties of the sequence, depending on the type of plot
	 */
	protected void setSequenceProperties(Plot plot,int sequence,Map properties) throws UnsoportedChartProperty {
		//a line plot is by definition, connected
		plot.setConnected(true, sequence);
	}
	
	public void setProperties(Map properties)throws UnsoportedChartProperty{
		super.setProperties(properties);
		
		
		Parameter<Double> barWidth= new Parameter(BAR_WIDTH);
		Parameter<Double> barOffset= new Parameter(BAR_OFFSET);
		
		ParameterList  parameters = new ParameterList();
		parameters.addParameter(barWidth);
		parameters.addParameter(barOffset);

		parameters.load(properties);
		
		ptplot.setBars(barWidth.getValue(), barOffset.getValue());
		
	}

	@Override
	protected PtPlotSequence createSequence(AbstractPtPlotChart chart, String name, DataSeries series, 
			String sequenceAttribute,String valueAttribute,int seqNum) throws UnsoportedChartProperty {
		
			return new PtPlotBarSequence(chart, ptplot, name, series,valueAttribute,sequenceAttribute,seqNum);
		
	}

	@Override
	protected PlotBox createPtPlotBox() {
		ptplot = new Plot();
		ptplot.setBars(true);
		return ptplot;
	}

	@Override
	public boolean needRefresh() {

		//TODO: optimize by checking if the chart needs refresh
		return true;
	}




	

}

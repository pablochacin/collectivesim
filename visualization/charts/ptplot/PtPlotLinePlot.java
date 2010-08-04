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
 * Draws sequence of data items as series of connected points.
 * In current implementation, the X value for each point is 
 * its correlative number in the series. 
 * 
 * TODO: Allows a timestamp for the DataItem. However, this requires the items
 * to be ordered
 *  
 * @author Pablo Chacin
 *
 */
public class PtPlotLinePlot extends AbstractPtPlotChart {

	/* *************************************************************
	 * Parameters
	 * **************************************************************/
	
	/**
	 * Size of the displayed sequence. If a non zero N value is given, only the N latest
	 * values added to the plot are kept
	 */
 	protected static Parameter<Integer> SCROLL_WINDOW = new Parameter<Integer>("plot.line.scrollwindow",false,new Integer(0));

 	/**
 	 * indicates if the points must be ploted with markers
 	 */
 	protected static Parameter<Boolean> USER_MARKERS = new Parameter<Boolean>("plot.line.markers",false, new Boolean(true));
 	
    /**  	
     * indicates the name of the attribute for the lower error value
     */
 	protected static Parameter<String> ERROR_LOW= new Parameter<String>("plot.error.low",false,String.class);

 	/**
 	 *  indicates the name of the attribute for the upper error value
 	 */
 	protected static Parameter<String> ERROR_HIGH = new Parameter<String>("plot.error.high",false,String.class);

	/**
 	 *  indicates if points of the sequence must be connected by a line (false) or not (true)
 	 */
 	protected static Parameter<Boolean> NO_LINE = new Parameter<Boolean>("plot.noline.",false, new Boolean(false));

	/**
	 * Displays a sequence in a LinePlot
	 * 
	 * @author Pablo Chacin
	 *
	 */
	private class PtPlotLineSequence extends AbstractPtPlotSequence {
				
		protected Plot ptplot;
		
		protected String errorLowAttribute;
		
		protected String errorHighAttribute;
		
		protected boolean withErrors;
		
		protected boolean connected;
		
		public PtPlotLineSequence(AbstractPtPlotChart chart, Plot ptplot, String name,
				DataSeries series, String sequenceAttribute,String valueAttribute,int sequenceNumber) 	throws UnsoportedChartProperty {
			super(chart, name, series, sequenceAttribute,valueAttribute,sequenceNumber);
			
			this.ptplot = ptplot;
			this.ptplot.addLegend(sequenceNumber, name);

		}

		@Override
		protected void addPoint(DataItem item) {
			
			itemNum++;
			
			Double sequence = item.getDouble(sequenceAttribute);
			double value = item.getDouble(valueAttribute);
			if(withErrors){
				double errorHigh = item.getDouble(errorHighAttribute);
				double errorLow = item.getDouble(errorLowAttribute);				
				ptplot.addPointWithErrorBars(sequenceNumber,sequence,value,errorLow,errorHigh,connected);				
			}
			else{
				ptplot.addPoint(sequenceNumber,sequence,value,connected);
			}
			
		}

		@Override
		public void setSequenceProperties(PlotBox plot, int sequence,
				Map properties) throws UnsoportedChartProperty {
					
			Parameter<String> errorHigh = new Parameter(ERROR_HIGH);
			Parameter<String> errorLow = new Parameter(ERROR_LOW);			
			Parameter<Boolean> noline = new Parameter(NO_LINE);
			
			ParameterList  parameters = new ParameterList();
			parameters.addParameter(errorHigh);
			parameters.addParameter(errorLow);
			parameters.addParameter(noline);
			parameters.load(properties);
	
		    errorLowAttribute = errorLow.getValue();
		    errorHighAttribute = errorHigh.getValue();
		    if((errorLowAttribute != null) && (errorHighAttribute != null)){
		    	withErrors = true;
		    }
		    else{
		    	withErrors = false;
		    }

		    
		    connected = !noline.getValue();

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
	public PtPlotLinePlot(String name,String title,Map properties,boolean autoupdate,boolean autoclean,boolean autoResize) throws UnsoportedChartProperty {

		super(name,title,properties,autoupdate,autoclean,autoResize);
				
	}

	public PtPlotLinePlot(String name,String title,boolean autoupdate,boolean autoclean,boolean autoResize) throws UnsoportedChartProperty {
		super(name,title,autoupdate,autoclean,autoResize);
	}
	
		
	public PtPlotLinePlot() throws UnsoportedChartProperty {
		this("","",new HashMap(),false,false,true);
	}

	public PtPlotLinePlot(boolean autoupdate) throws UnsoportedChartProperty{
		this("","",new HashMap(),autoupdate,false,true);
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
		
		Parameter<Integer> scrollWindow = new Parameter(SCROLL_WINDOW);
		Parameter<Boolean> markers = new Parameter(USER_MARKERS);


		ParameterList  parameters = new ParameterList();
		parameters.addParameter(scrollWindow);
		parameters.addParameter(markers);	


		parameters.load(properties);
		
	    ptplot.setPointsPersistence(scrollWindow.getValue());
		 
	    if(markers.getValue()){
	    	ptplot.setMarksStyle("various");
	    }


	    
	}

	@Override
	protected PtPlotSequence createSequence(AbstractPtPlotChart chart, String name, DataSeries series, 
			String sequenceAttribute,String valueAttribute,int seqNum) throws UnsoportedChartProperty {
		
			return new PtPlotLineSequence(chart, ptplot, name, series,sequenceAttribute,valueAttribute,seqNum);
		
	}

	@Override
	protected PlotBox createPtPlotBox() {
		ptplot = new Plot();
		ptplot.setYRange(0.0, 1.0);
		return ptplot;
	}

	@Override
	public boolean needRefresh() {

		//TODO: optimize by checking if the chart needs refresh
		return true;
	}



	

}

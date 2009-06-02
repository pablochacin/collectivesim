package edu.upc.cnds.collectivesim.visualization.charts.ptplot;


import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JPanel;

import ptolemy.plot.PlotBox;

import edu.upc.cnds.collectives.util.Parameter;
import edu.upc.cnds.collectives.util.ParameterList;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.base.BaseDataSeries;
import edu.upc.cnds.collectivesim.visualization.charts.Chart;
import edu.upc.cnds.collectivesim.visualization.charts.UnsoportedChartProperty;

/**
 * Base implementation of a PtPlot based Plot. Subclasses must configure the
 * underlying PtPlot according with the type of plot (e.g. bars, scatter, line, stack)
 * and other characteristics.
 * 
 * The actual implementation needs to handle a particularity of PtPlot: plot types like Plot 
 * and Histogram don't have a common generic plot super class neither they implement a common
 * interface to add points. The common superclass they share, called PtPlotBox only handle the
 * generic display options like title, visibility, etc. 
 * 
 * 
 * This class supports the basic attributes of a Plot, like 
 * <ul>
 * <li>the title, size and background of the plot
 * <li>the color and label for each series, .
 * </ul>
 *  
 * @author Pablo Chacin
 *
 */
public abstract class AbstractPtPlotChart implements Chart {

	private static Map EMPTY_PROPERTIES_MAP = new HashMap(); 
	
 	protected static Parameter<Integer> CHART_HEIGHT = new Parameter<Integer>("plot.height",false,new Integer(400));

 	protected static Parameter<Integer> CHART_WIDTH = new Parameter<Integer>("plot.width",false,new Integer(600));


	/**
	 * Datasets are stored as traces
	 */
	protected List<PtPlotSequence> sequences;
			
	/**
	 * PtPlot that displays the plot's data 
	 */
	protected PlotBox ptplotBox;
	
	/**
	 * Indicates if the plot must update series automatically when data is available
	 */
	protected boolean autoupdate;
	

	/**
	 * When in autoupdate mode off (that is, the plot must be explicitly refreshed), indicates if the plot 
	 * must be cleaned before each refresh (this is useful when showing snapshots of data to avoid 
	 * a accumulating previous data)
	 */
	protected boolean autoclean;
	
	
	/**
	 * Indicates if the plot must resize to fit current points
	 */
	protected boolean autoResize;
	
	protected String name;
	/**
	 * Constructor used by subclasses.
	 * 
	 * @param title
	 * @param size
	 * @throws UnsoportedChartProperty 
	 */
	protected AbstractPtPlotChart(String name,Map properties,boolean autoupdate, boolean autoclean,boolean autoResize) throws UnsoportedChartProperty {
		
		this.name = name;
		this.autoupdate = autoupdate;
		this.autoclean = autoclean;
		this.autoResize = autoResize;
		sequences = new ArrayList<PtPlotSequence>();
		
		this.ptplotBox = createPtPlotBox();	
		setProperties(properties);
		
	}

	protected AbstractPtPlotChart(String name,boolean autoupdate, boolean autoclean,boolean autoResize) throws UnsoportedChartProperty {
		this(name,EMPTY_PROPERTIES_MAP,autoupdate,autoclean,autoResize);
	}

	/**
	 * 
	 * @return the PtPlotBox (graphical environment) for the plot
	 */
	protected abstract PlotBox createPtPlotBox();


	
	/**
	 * Convenience constructor without arguments used to configure the plot using
	 * configuration parameters in the setProperties method.
	 * 
	 * @throws UnsoportedChartProperty
	 */
	public AbstractPtPlotChart() throws UnsoportedChartProperty {
		this("",new HashMap(),false,false,true);
	}

	/**
	 * Convenience constructor. Only the autoupdate option is used.
	 * 
	 * @param autoupdate
	 * @throws UnsoportedChartProperty
	 */
	public AbstractPtPlotChart(boolean autoupdate) throws UnsoportedChartProperty{
		this("",new HashMap(),autoupdate,false,true);
	}
	
	
	/**
	 * Updates the data
	 */
	protected void update(){
		//if in auto-refresh
		for(PtPlotSequence s: sequences) {
			s.update();
		}
	}
	
	/**
	 * Refresh the visualization
	 */
	public void refresh() {
		
		if(autoclean){
		  clear();
		}
		
		//if in auto-refresh
		if(!autoupdate){
			update();
		}
		
		//to resize, call fillPlot.
		if(autoResize){
			ptplotBox.fillPlot();
		}
		else{
			ptplotBox.setXRange(ptplotBox.getXAutoRange()[0],ptplotBox.getXAutoRange()[1]);
			ptplotBox.repaint();
		}
	}


	@Override
	public void addSequence(String name,DataSeries series,String sequenceAttribute,String valueAttribute,Map properties) throws UnsoportedChartProperty  {
		
		int seqNum = sequences.size()+1;
		
		PtPlotSequence sequence = createSequence(this,name,series,sequenceAttribute,valueAttribute,seqNum);
		
		//Set the properties for this sequence
		sequence.setSequenceProperties(ptplotBox, seqNum, properties);

		sequences.add(sequence);
		
			
		//if autoupdate, add the sequence as an observer to detect new items and display them
		if(autoupdate){
			series.addObserver(sequence);
		}
	}

	/**
	 * Creates a new sequence in the plot.
	 * 
	 * Subclasses can overwrite to use a different implementation of the sequence
	 * 
	 * @param plot
	 * @param name
	 * @param sequence
	 * @param seqNum
	 * @return
	 * @throws UnsoportedChartProperty
	 */
	protected abstract PtPlotSequence createSequence(AbstractPtPlotChart plot,String name,DataSeries series,
			                             String sequenceAttribute,String valueAttribute,int seqNum) throws UnsoportedChartProperty;
	
	
	public void addSequence(String name,DataSeries series,String sequenceAttribute,String valueAttribute) throws UnsoportedChartProperty  {
		addSequence(name,series, sequenceAttribute,valueAttribute,new HashMap());
	}

	public void setProperties(Map properties) throws UnsoportedChartProperty {

		Parameter<Integer> height = new Parameter(CHART_HEIGHT);
		
		Parameter<Integer> width = new Parameter(CHART_WIDTH);

		ParameterList  parameters = new ParameterList();
		parameters.addParameter(height);
		parameters.addParameter(width);
		
		parameters.load(properties);
		
	    ptplotBox.setSize(new Dimension(width.getValue(),height.getValue())); 
			
	}

	
	
	/**
	 * Clear all the data displayed in the plot
	 */
	public void clear(){
		ptplotBox.clear(false);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getHeight() {
		return ptplotBox.getWidth();
	}

	@Override
	public int getWidth() {
		return ptplotBox.getHeight();
	}

	@Override
	public JPanel getViewableContent() {
		return ptplotBox;
	}

	@Override
	public void print(Graphics g) {
		ptplotBox.paint(g);
		
	}

	@Override
	public void sampleChart() throws UnsoportedChartProperty{

		Random rnd = new Random();
		
    	DataSeries series = new BaseDataSeries("Test");
    	
    	for(int i = 0;i<10;i++){
    		series.addItem("value",new Double(rnd.nextDouble()));
    	}
    	
		addSequence("Test", series,"sequence","value");
	}


}

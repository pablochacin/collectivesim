package collectivesim.visualization.charts.jchart2d;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyHighestValues;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.util.Range;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;

import collectives.util.GraphicUtils;
import collectivesim.dataseries.DataItem;
import collectivesim.dataseries.DataSequence;
import collectivesim.dataseries.DataSeries;
import collectivesim.dataseries.DataSeriesObserver;
import collectivesim.visualization.charts.Chart;
import collectivesim.visualization.charts.UnsoportedChartProperty;

public class JChart2dLinePlot implements Chart {

	private class JChart2dSequence implements DataSeriesObserver {

		/**
		 * JChart2D trace used to plot the sequence
		 */
		private ITrace2D trace;


		/**
		 * Sequence's name
		 */
		private String name;

		/**
		 * Source of iems for the Plot
		 */
		private DataSequence sequence;
		
		/**
		 * Display properties of the sequence
		 */
		private Map properties;
		
		
		public JChart2dSequence(String name, DataSequence sequence, Map properties) throws UnsoportedChartProperty {
			this.name = name;
			this.sequence = sequence;
			this.properties = properties;
			this.trace = new Trace2DLtd();
			this.trace.setName(name);		
			setProperties(properties);
			chart.addTrace(trace);
		}

		
		/**
		 * Refreshes the plot with all the available elements in the 
		 */
		void refresh() {
			
			while(sequence.hasItems()){
				trace.addPoint(trace.getSize()+1.0,sequence.getItem().getValue());
			}
		}
		
		private void setProperties(Map properties) throws UnsoportedChartProperty {
			Properties conf = new Properties();
			conf.putAll(properties);

			String colorName = conf.getProperty("color",DEFAULT_SERIES_COLOR);
			Color color = GraphicUtils.getColor(colorName);
			if(color == null) {
				throw new UnsoportedChartProperty("Unsupported color "+colorName);
			}
			trace.setColor(color);

		}


		@Override
		public void update(DataItem item) {
			trace.addPoint(trace.getSize()+1.0,item.getValue());
		}


	}


	private static String DEFAULT_WIDTH = "400";
	private static String DEFAULT_HEIGHT = "400";

	private static String DEFAULT_SERIES_COLOR = "BLACK";

	private static String DEFAULT_BACKGROUND_COLOR = "WHITE";

	/**
	 * Datasets are stored as traces
	 */
	private List<JChart2dSequence> sequences;


	private Chart2D chart;



	/**
	 * Indicates if the plot must update series automatically when data is available
	 */
	private boolean autoupdate;
	
	/**
	 * Constructor
	 * 
	 * @param title
	 * @param size
	 * @throws UnsoportedChartProperty 
	 */
	public JChart2dLinePlot(Map properties,boolean autoupdate) throws UnsoportedChartProperty {

		this.autoupdate = autoupdate;
		sequences = new ArrayList<JChart2dSequence>();

		chart = new Chart2D();
		chart.getAxisY().setRangePolicy(new RangePolicyHighestValues());

		setProperties(properties);

	}


	public JChart2dLinePlot() throws UnsoportedChartProperty {
		this(new HashMap(),false);
	}

	public JChart2dLinePlot(boolean autoupdate) throws UnsoportedChartProperty{
		this(new HashMap(),autoupdate);
	}
	
	public void refresh() {
		
		for(JChart2dSequence s: sequences) {
			s.refresh();
		}
		
		chart.repaint();
	}


	public void addSequence(String name,DataSeries series,Map properties) throws UnsoportedChartProperty  {

		JChart2dSequence sequence = new JChart2dSequence(name,series.getSequence(),properties);
	
		sequences.add(sequence);
		
		//if autoupdate, add the sequence as an observer to detect new items and display them
		if(autoupdate){
			series.addObserver(sequence);
		}
	}

	public void addSequence(String name,DataSeries series) throws UnsoportedChartProperty  {
		addSequence(name,series, new HashMap());
	}



	public void setProperties(Map properties) throws UnsoportedChartProperty {

		Properties conf = new Properties();
		conf.putAll(properties);


		//Plot's title
		String title = conf.getProperty("title","Untitled");
		chart.setName(title);

		//Plot size
		Integer sizeX = Integer.valueOf(conf.getProperty("width", DEFAULT_WIDTH));
		Integer sizeY = Integer.valueOf(conf.getProperty("height", DEFAULT_HEIGHT));
		chart.setSize(sizeX, sizeY);

		//Backgroun color

		if(conf.contains("background")) {
			String colorName = conf.getProperty("background");
			Color color = GraphicUtils.getColor(colorName);
			if(color == null) {
				throw new UnsoportedChartProperty("Unsupported color "+colorName);
			}
			chart.setBackground(color);
		}

		//X axis range
		//TODO: check that range values are double


		if((conf.containsKey("axisXmin")) || (conf.containsKey("axisXmax"))){

			Double min = chart.getAxisX().getRange().getMin();
			Double max = chart.getAxisX().getRange().getMax();

			if(conf.containsKey("axisXmin")){
				min = Double.valueOf(conf.getProperty("axisXmin"));
			}

			if(conf.containsKey("axisXmax")){
				max = Double.valueOf(conf.getProperty("axisXmax"));
			}


			chart.getAxisX().setRangePolicy(new RangePolicyFixedViewport(new Range(min, max)));
		}
		
		//axis X title
		if(conf.containsKey("axisXtitle")) {
			IAxis.AxisTitle axisTitle = new IAxis.AxisTitle(conf.getProperty("axisXtitle"));
			chart.getAxisX().setAxisTitle(axisTitle);
		}

		//Y axis range
		if((conf.containsKey("axisYmin")) || (conf.containsKey("axisYmax"))){

			Double min = chart.getAxisY().getRange().getMin();
			Double max = chart.getAxisY().getRange().getMax();

			if(conf.containsKey("axisYmin")){
				min = Double.valueOf(conf.getProperty("axisYmin"));
			}

			if(conf.containsKey("axisYmax")){
				max = Double.valueOf(conf.getProperty("axisYmax"));
			}


			chart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(new Range(min, max)));
		}	
		
		//axis Y title
		if(conf.containsKey("axisYtitle")) {
			IAxis.AxisTitle axisTitle = new IAxis.AxisTitle(conf.getProperty("axisXtitle"));
			chart.getAxisY().setAxisTitle(axisTitle);
		}
	}


	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}


	@Override
	public String getName() {
		return chart.getName();
	}


	@Override
	public int getHeight() {
		return chart.getWidth();
	}


	@Override
	public int getWidth() {
		return chart.getHeight();
	}


	@Override
	public JPanel getViewableContent() {
		return chart;
	}


	@Override
	public boolean isAutoRefresh() {
		return autoupdate;
	}


	@Override
	public boolean needRefresh() {
		//TODO: Optimize by checking if refresh is needed
		return true;
	}


}

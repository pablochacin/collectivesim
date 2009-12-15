package edu.upc.cnds.collectivesim.dataseries.functions;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectives.util.Histogram;
import edu.upc.cnds.collectives.util.MapFromString;
import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;
import edu.upc.cnds.collectivesim.dataseries.base.BaseDataItem;
import edu.upc.cnds.collectivesim.dataseries.base.BaseDataSeries;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.stream.base.EmpiricalRandomStream;
import edu.upc.cnds.collectivesim.table.Table;
import edu.upc.cnds.collectivesim.table.base.MemoryTable;
import edu.upc.cnds.collectivesim.visualization.ViewPanel;
import edu.upc.cnds.collectivesim.visualization.Viewer;
import edu.upc.cnds.collectivesim.visualization.charts.Chart;
import edu.upc.cnds.collectivesim.visualization.charts.UnsoportedChartProperty;
import edu.upc.cnds.collectivesim.visualization.charts.ptplot.PtPlotBarPlot;
import edu.upc.cnds.collectivesim.visualization.charts.ptplot.PtPlotHistogramChart;
import edu.upc.cnds.collectivesim.visualization.charts.ptplot.PtPlotLinePlot;

/**
 * 
 * 
 * 
 * @author 
 *
 */
public class SeriesHistogram implements SeriesFunction {


	protected String attribute;

	protected Histogram histogram;
	
	public SeriesHistogram(String attribute,Double min, Double max,int numBins,boolean truncate){

		this.attribute = attribute;
		this.histogram = new Histogram(min,max,numBins,truncate);
	

	}

	/**
	 * Create a Histogram with a variable number of bins
	 * 
	 * @param attribute
	 * @param binWidth
	 */
	public SeriesHistogram(String attribute,Double binWidth){

		this.attribute = attribute;
		this.histogram = new Histogram(binWidth);

	}	

	@Override
	public boolean processItem(DataItem item) {

		Double value = item.getDouble(attribute);
		
		histogram.addValue(value);
		
		return true;

	}
	
	
	@Override
	public void calculate(DataSeries result) {

				
		for(Histogram.Bin b: histogram.getBins()){		
			Map binAttributes = new HashMap();
			binAttributes.put("lower", b.lower);
			binAttributes.put("higher",b.higher);
			binAttributes.put("middle",(b.lower+b.higher)/2.0);
			binAttributes.put("count",b.count);
			binAttributes.put("fraction",b.count/histogram.getCount());

			result.addItem(binAttributes);
			


		}

	}

	@Override
	public void reset() {

		histogram.reset();
	}


	public static void main(String[] args) throws UnsoportedChartProperty{


		DataSeries histogramSeries = new BaseDataSeries("histogram",10);

		Table<Double> ditribution = new MemoryTable<Double>("distribution","0.025;0.05;0.1;0.15;0.35;0.15;0.1;0.05;0.025",";",Double.class);

		Stream<Double> valueStream = new EmpiricalRandomStream("values",-1.0,1.0,ditribution);

		//SeriesFunction histogramFunc = new SeriesHistogram("value",0.0,1.0,10,false);
		SeriesFunction histogramFunc = new SeriesHistogram("value",0.1);
		
		histogramFunc.reset();

		for(int i=0;i<1000;i++){
			DataItem item = new BaseDataItem(i,"value",valueStream.nextElement());
			histogramFunc.processItem(item);
		}

		histogramFunc.calculate(histogramSeries);

		Map histogramParams = new MapFromString("plot.bar.barwidth=0.09");

		Chart histogramPlot = new PtPlotBarPlot("","Histogram",histogramParams,false,true,true);

		ViewPanel panel = new ViewPanel(histogramPlot);


		histogramPlot.addSequence("", histogramSeries, "lower","count");

		histogramPlot.refresh();
	}
}

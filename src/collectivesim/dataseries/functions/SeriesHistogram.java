package collectivesim.dataseries.functions;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import collectivesim.util.FormattingUtils;
import collectivesim.util.Histogram;
import collectivesim.util.MapFromString;
import collectivesim.dataseries.DataItem;
import collectivesim.dataseries.DataSequence;
import collectivesim.dataseries.DataSeries;
import collectivesim.dataseries.SeriesFunction;
import collectivesim.dataseries.base.BaseDataItem;
import collectivesim.dataseries.base.BaseDataSeries;
import collectivesim.stream.Stream;
import collectivesim.stream.base.EmpiricalRandomStream;
import collectivesim.table.Table;
import collectivesim.table.base.MemoryTable;
import collectivesim.visualization.ViewPanel;
import collectivesim.visualization.Viewer;
import collectivesim.visualization.charts.Chart;
import collectivesim.visualization.charts.UnsoportedChartProperty;
import collectivesim.visualization.charts.ptplot.PtPlotBarPlot;
import collectivesim.visualization.charts.ptplot.PtPlotHistogramChart;
import collectivesim.visualization.charts.ptplot.PtPlotLinePlot;

/**
 * 
 * 
 * 
 * @author 
 *
 */
public class SeriesHistogram implements SeriesFunction {


	protected String attribute;
	
	protected String count;

	protected Histogram histogram;
	
	public SeriesHistogram(String attribute,String count,Double min, Double max,int numBins,boolean truncate){

		this.attribute = attribute;
		this.count = count;
		this.histogram = new Histogram(min,max,numBins,truncate);
	

	}

	public SeriesHistogram(String attribute,Double min, Double max,int numBins,boolean truncate){

		this(attribute,null,min,max,numBins,truncate);

	}
	/**
	 * Create a Histogram with a variable number of bins
	 * 
	 * @param attribute
	 * @param binWidth
	 */
	public SeriesHistogram(String attribute,String count,Double binWidth){

		this.attribute = attribute;
		this.count = count;
		this.histogram = new Histogram(binWidth);

	}	

	
	public SeriesHistogram(String attribute,Double binWidth){
		this(attribute,null,binWidth);
	}
	
	
	@Override
	public boolean processItem(DataItem item) {

		Double value = item.getDouble(attribute);
		Double countValue = 1.0;
		
		if(count != null){
			countValue = item.getDouble(count);
		}
		
		histogram.addValue(value,countValue);
		
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

		Table<Double> ditribution = new MemoryTable<Double>("distribution","0.1;0.1;0.1;0.1;0.1;0.1;0.1;0.1;0.1;0.1",";",Double.class);

		Stream<Double> valueStream = new EmpiricalRandomStream(1.0,3.0,ditribution);

		//SeriesFunction histogramFunc = new SeriesHistogram("value",0.0,1.0,10,false);
		SeriesFunction histogramFunc = new SeriesHistogram("value",1.0);
		
		histogramFunc.reset();

		for(int i=0;i<1000;i++){
			DataItem item = new BaseDataItem(i,"value",valueStream.nextElement());
			histogramFunc.processItem(item);
		}

		histogramFunc.calculate(histogramSeries);
		
		DataSequence s = histogramSeries.getSequence();
		
		while(s.hasItems()){
			DataItem i = s.getItem();
			System.out.println(FormattingUtils.mapToString(i.getAttributes()));
		}

		Map histogramParams = new MapFromString("plot.bar.barwidth=0.1");

		Chart histogramPlot = new PtPlotBarPlot("","Histogram",histogramParams,false,true,true);

		ViewPanel panel = new ViewPanel(histogramPlot);


		histogramPlot.addSequence("", histogramSeries, "lower","fraction");

		histogramPlot.refresh();
	}
}

package edu.upc.cnds.collectivesim.dataseries.functions;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.upc.cnds.collectives.util.FormattingUtils;
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

	private class Bin{
		public Double count;
		public Double lower;
		public Double higher;

		Bin(){
			this.count = 0.0;
			this.lower = 0.0;
			this.higher = 0.0;
		}

		Bin(Double lower,Double higher,Double count){
			this.lower = lower;
			this.higher = higher;
			this.count = count;
		}

		Bin(Double lower,Double higher){
			this.lower = lower;
			this.higher = higher;
			this.count = 0.0;
		}

		public void inc(){
			count++;
		}
	}

	protected Double binWith;

	protected String attribute;

	protected Vector<Bin> bins;

	//offset of bin's vector position with respect of its number (due to the vector's index starting at 0)
	protected int offset;


	//min value in the histogram
	protected Double min;

	//max value in the histogram
	protected Double max;

	//number of elements
	protected Double count;

	/**
	 * Indicates if values out of range should be ignored (true) or added to the count of the
	 * respective range limit
	 */
	protected boolean truncate;

	/**
	 * indicates if the histogram must be resized to accommodate values outside current boundaries
	 */
	protected boolean resize;

	/**
	 * Initial number of bins
	 */
	protected int initBins;
	
	public SeriesHistogram(String attribute,Double min, Double max,int numBins,boolean truncate){

		this.attribute = attribute;
		this.min = min;
		this.max = max;
		this.truncate = truncate;
		this.resize = false;
		this.initBins = numBins;

		binWith = (max-min)/(double)(numBins);
		offset = (int)(numBins*binWith-max);


	}

	/**
	 * Create a Histogram with a variable number of bins
	 * 
	 * @param attribute
	 * @param binWidth
	 */
	public SeriesHistogram(String attribute,Double binWidth){

		this.attribute = attribute;
		this.min = Double.MAX_VALUE;
		this.max = Double.MIN_VALUE;
		this.binWith = binWidth;
		this.offset = 0;
		this.resize = true;
		this.truncate = false;
		this.initBins = 0;


	}	

	@Override
	public boolean processItem(DataItem item) {

		count++;

		Double value = item.getDouble(attribute);

		//first insertion is a very complex case, treat separatelly
		if(bins.isEmpty()){
			Double lower = Math.floor(value/binWith)*binWith;
			Double higher = lower+binWith;
			bins.add(new Bin(lower,higher,1.0));
			min = lower;
			max = higher;
			offset = calculateOffet();
			return true;
		}
		
		//each bin covers the range [lower,higher) (includes lower, excludes higher)
		int bin = (int) Math.floor(value/binWith+offset);


		if(bin < 0){

			if(resize){
				//add missing bins
				while(bins.firstElement().lower > value){
					Double higher = bins.firstElement().lower;
					Double lower = higher - binWith;
					bins.add(0, new Bin(lower,higher,count));        		
					min = min - binWith;

				}

				//the first one is the corresponding to the value
				bin = 0;
				offset = calculateOffet();

			}
			else{
				if(truncate)
					//just ignore
					return true;
				else
					bin = 0;
			}
		}
		else{
			if(bin >= bins.size()){ 
				if(resize){
					//add missing bins
					while(bins.lastElement().higher < value){
						Double lower = bins.lastElement().higher;
						Double higher = lower + binWith;
						bins.add(bins.size(), new Bin(lower,higher,count)); 
						max = max + binWith;
					}
					
					//the last one is the corresponding to the value
					bin = bins.size()-1;
					offset = calculateOffet();
				}
				else{
					if(truncate)
						return true;
					else
						bin = bins.size()-1;
				}
			}

		}

		bins.get(bin).inc();

		

		return true;

	}

	
	/**
	 * Calculates the offset needed to access Bins in the bins Vector
	 * 
	 * @return
	 */
	private int calculateOffet(){
		return (int) (bins.size()-1-Math.floor(bins.lastElement().higher/binWith));
	}
	
	@Override
	public void calculate(DataSeries result) {


		for(Bin b: bins){		
			Map binAttributes = new HashMap();
			binAttributes.put("lower", b.lower);
			binAttributes.put("higher",b.higher);
			binAttributes.put("middle",(b.lower+b.higher)/2.0);
			binAttributes.put("count",b.count);
			binAttributes.put("fraction",b.count/count);

			result.addItem(binAttributes);


		}

	}

	@Override
	public void reset() {

		bins = new Vector<Bin>(initBins);

		for(int i=0;i<initBins;i++){
			Double lower = min+(binWith*i);
			bins.add(new Bin(lower,lower+binWith,0.0));
		}

		count = 0.0;
	}


	public static void main(String[] args) throws UnsoportedChartProperty{


		DataSeries histogramSeries = new BaseDataSeries("histogram",10);

		Table<Double> ditribution = new MemoryTable<Double>("distribution","0.025;0.05;0.1;0.15;0.35;0.15;0.1;0.05;0.025",";",Double.class);

		Stream<Double> valueStream = new EmpiricalRandomStream("values",-1.0,0.0,ditribution);

		//SeriesFunction histogramFunc = new SeriesHistogram("value",0.0,1.0,10,false);
		SeriesFunction histogramFunc = new SeriesHistogram("value",0.1);
		
		histogramFunc.reset();

		for(int i=0;i<1000;i++){
			histogramFunc.processItem(new BaseDataItem(i,"value",valueStream.getValue()));
		}

		histogramFunc.calculate(histogramSeries);

		Map histogramParams = new MapFromString("plot.bar.barwidth=0.09");

		Chart histogramPlot = new PtPlotBarPlot("","Histogram",histogramParams,false,true,true);

		ViewPanel panel = new ViewPanel(histogramPlot);


		histogramPlot.addSequence("", histogramSeries, "middle","count");

		histogramPlot.refresh();
	}
}

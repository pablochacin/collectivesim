package edu.upc.cnds.collectivesim.dataseries.functions;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
		
		public void inc(){
			count++;
		}
	}
	
	protected Double binWith;
	
	protected String attribute;
	
	protected Vector<Bin> bins;
	
	protected int midPos;
	
	protected int numBins;
	
	protected Double min;
	
	protected Double max;
	
	protected Double count;
	
	/**
	 * Indicates if values out of range should be ignored (true) or added to the count of the
	 * respective range limit
	 */
	protected boolean truncate;
		
	public SeriesHistogram(String attribute,Double min, Double max,int numBins,boolean truncate){
		
		this.attribute = attribute;
		this.min = min;
		this.max = max;
		this.numBins = numBins;
		this.truncate = truncate;
		
		binWith = (max-min)/(double)(numBins);
		midPos = (int)(numBins*binWith-max);
		

	}
	

	@Override
	public boolean processItem(DataItem item) {

		count++;
		
		Double value = item.getDouble(attribute);
		
        int bin = (int) Math.floor(value/binWith+midPos);
        
        if(bin < 0){
        	if(truncate)
        		return true;
        	else
        		bin = 0;
        }
        
        if(bin >= numBins){ 
        	if(truncate)
        		return true;
        	else
        		bin = bins.size()-1;
        }
                
        bins.get(bin).inc();
        
        return true;
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

		bins = new Vector<Bin>(numBins);

		for(int i=0;i<numBins;i++){
			Double lower = min+(binWith*i);
			bins.add(new Bin(lower,lower+binWith,0.0));
		}
		
		count = 0.0;
	}

	
	public static void main(String[] args) throws UnsoportedChartProperty{


		DataSeries histogramSeries = new BaseDataSeries("histogram",10);
		
		Table<Double> ditribution = new MemoryTable<Double>("distribution","0.025;0.05;0.1;0.15;0.35;0.15;0.1;0.05;0.025",";",Double.class);
		
		Stream<Double> valueStream = new EmpiricalRandomStream("values",0.0,1.0,ditribution);
		
		SeriesFunction histogramFunc = new SeriesHistogram("value",0.0,1.0,10,false);
		
		histogramFunc.reset();
		
		for(int i=0;i<1000;i++){
			histogramFunc.processItem(new BaseDataItem(i,"value",valueStream.getValue()));
		}
		
		histogramFunc.calculate(histogramSeries);
		
		Map histogramParams = new MapFromString("plot.bar.width=0.09");
		
		Chart histogramPlot = new PtPlotBarPlot("Histogram",histogramParams,false,true,true);
		
		ViewPanel panel = new ViewPanel(histogramPlot);
		
		
		histogramPlot.addSequence("", histogramSeries, "middle","count");
		
		histogramPlot.refresh();
	}
}

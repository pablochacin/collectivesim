package collectivesim.util;

import java.util.Random;
import java.util.Vector;

import collectivesim.dataseries.DataSeries;
import collectivesim.dataseries.base.BaseDataSeries;
import collectivesim.random.MersenneRandom;
import collectivesim.stream.Stream;
import collectivesim.stream.base.EmpiricalRandomStream;
import collectivesim.table.Table;
import collectivesim.table.base.MemoryTable;
import collectivesim.visualization.charts.UnsoportedChartProperty;

/**
 * 
 * 
 * 
 * @author 
 *
 */
public class Histogram {

	public class Bin{
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
		
		public void inc(double count){
			this.count+=count;
		}
	}

	protected Double binWith;


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
	
	public Histogram(Double min, Double max,int numBins,boolean truncate){

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
	public Histogram(Double binWidth){

		this.min = Double.MAX_VALUE;
		this.max = Double.MIN_VALUE;
		this.binWith = binWidth;
		this.offset = 0;
		this.resize = true;
		this.truncate = false;
		this.initBins = 0;


	}	

	public void addValue(Double value){
		addValue(value,1.0);
	}
	
	public void addValue(Double value, Double valueCount){
		//first insertion is a very complex case, treat separatelly
		if(bins.isEmpty()){
			Double lower = Math.floor(value/binWith)*binWith;
			Double higher = lower+binWith;
			bins.add(new Bin(lower,higher,valueCount));
			min = lower;
			max = higher;
			count = 1.0;
			offset = calculateOffet();
			return;
		}
		
		//each bin covers the range [lower,higher) (includes lower, excludes higher)
		int bin = (int) Math.floor(value/binWith+offset);


		if(bin < 0){

			if(resize){
				//add missing bins
				while(bins.firstElement().lower > value){
					Double higher = bins.firstElement().lower;
					Double lower = higher - binWith;
					bins.add(0, new Bin(lower,higher));        		
					min = min - binWith;					

				}

				//the first one is the corresponding to the value
				bin = 0;
				offset = calculateOffet();

			}
			else{
				if(truncate)
					//just ignore
					return;
				else
					bin = 0;
			}
		}
		else{
			if(bin >= bins.size()){ 
				if(resize){
					//add missing bins
					while(bins.lastElement().higher <= value){
						Double lower = bins.lastElement().higher;
						Double higher = lower + binWith;
						bins.add(bins.size(), new Bin(lower,higher)); 
						max = max + binWith;
					}
					
					//the last one is the corresponding to the value
					bin = bins.size()-1;
					offset = calculateOffet();
				}
				else{
					if(truncate)
						return;
					else
						bin = bins.size()-1;
				}
			}

		}

		bins.get(bin).inc(valueCount);
		
		count+=valueCount;
		

	}

	
	/**
	 * Calculates the offset needed to access Bins in the bins Vector
	 * 
	 * @return
	 */
	private int calculateOffet(){
		//return (int) (bins.size()-1-Math.floor(bins.lastElement().higher/binWith));
		return (int)(-Math.floor(bins.firstElement().lower/binWith));
	}
	

	public Double getCount(){
		return count;
	}

	public void reset() {

		bins = new Vector<Bin>(initBins);

		for(int i=0;i<initBins;i++){
			Double lower = min+(binWith*i);
			bins.add(new Bin(lower,lower+binWith,0.0));
		}

		count = 0.0;
	}

	
	public Bin[] getBins(){
	
		return bins.toArray(new Bin[bins.size()]);
	}
	
	
	
	public Double[] getPercentiles(Double[] percentiles){
	
		Double[] percentileValues = new Double[percentiles.length];
		
		if(bins.size() == 0){
			throw new IllegalStateException("Histogram is empty");
		}
		
		if(bins.size() == 1){
			Double value = bins.get(0).higher;
			for(int i=0;i<percentiles.length;i++){
				percentileValues[i] = value;
			}
		}
		
				
		Double[] commulative = new Double[bins.size()];
		commulative[0] = bins.get(0).count;
		for(int i=1;i<bins.size();i++){
			commulative[i] = commulative[i-1]+ bins.get(i).count;			
		}
		
		int b = 0;
		for(int p =0;p < percentiles.length;){
			Double limit = count*percentiles[p];		
	
			for(;commulative[b] < limit;b++);
			percentileValues[p] = bins.get(b).higher;
			p++;
		}
		
		
		return percentileValues;
	}
	
	
	
	
	
	
	public static void main(String[] args) throws UnsoportedChartProperty{


		DataSeries histogramSeries = new BaseDataSeries("histogram",10);

		Table<Double> ditribution = new MemoryTable<Double>("distribution","0.025;0.05;0.1;0.15;0.35;0.15;0.1;0.05;0.025",";",Double.class);

		Stream<Double> valueStream = new EmpiricalRandomStream(0.0,1.0,ditribution);

		//SeriesFunction histogramFunc = new SeriesHistogram("value",0.0,1.0,10,false);
		Histogram histogram = new Histogram(1.0);
		
		histogram.reset();

		Random rnd =  new MersenneRandom();
		for(int i=0;i<1000;i++){;
			//histogram.addValue(valueStream.nextElement());
			histogram.addValue((double)rnd.nextInt(11));
		}

		
		String bar = "====================";
		for(Bin b: histogram.getBins()){
			int barlength = (int) Math.round(bar.length()*b.count/histogram.getCount());
			String binBar = bar.substring(0,barlength);
			System.out.println("["+b.lower+"-"+b.higher+"] "+binBar);
		}
	}

	
}

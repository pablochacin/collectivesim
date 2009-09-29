package edu.upc.cnds.collectivesim.dataseries.functions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;

public class DescriptiveStatistics implements SeriesFunction {

	private static Double[] DEFAULT_PERCENTILES = {0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0};
	
	protected Double[] percentiles;
	
	protected Double[] pctValues;

	protected Vector<Double>values;

	String attribute;
	
	public DescriptiveStatistics(String attribute,Double[] percentiles) {
		this.attribute = attribute;
		this.percentiles = percentiles;
		this.pctValues = new Double[percentiles.length];
		this.values = new Vector<Double>();
		
	}

	public DescriptiveStatistics(String attribute) {
		this(attribute,DEFAULT_PERCENTILES);
	}


	@Override
	public void reset(){
		values.clear();
	}

	@Override
	public boolean processItem(DataItem item){
		values.add((Double)item.getDouble(attribute));

		return true;
	}
	
	@Override
	public void calculate(DataSeries result) {
				
		if(values.isEmpty()){
			return;

		}
		
		Collections.sort(values);
		
		for(int p =0; p < percentiles.length;p++){
	        double pos = percentiles[p] * (values.size() + 1);
	        double fpos = Math.floor(pos);
	        int intPos = (int) fpos;
	        double dif = pos - fpos;
	        
	        double lower = values.get(intPos - 1);
	        double upper = values.get(intPos);
	        pctValues[p] = lower + dif* (upper - lower);
	        
	       	        
		}
		
		
		Map<String,Object>attributes = new HashMap<String,Object>();
		attributes.put("count", new Double(values.size()));
		attributes.put("min", values.firstElement());
		attributes.put("max", values.lastElement());
		for(int p=0;p < percentiles.length;p++){
			attributes.put("pct"+percentiles[p], pctValues[p]);
		}
		
		
		result.addItem(attributes);
		
		
	}

}

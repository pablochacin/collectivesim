package edu.upc.cnds.collectivesim.model;

import java.util.Vector;

import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.baseImp.BaseDataItem;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelObserver;

/**
 * 
 * A ModelObserver that adds the values from a model's agents to a DataSeries.
 * 
 * Each time the values are updated, if the DataSeries doesn't have the append option, 
 * the serie's DataItems are removed before the new values are added. 
 * 
 * @author Pablo Chacin
 *
 */
public class DataSeriesUpdaterObserver implements ModelObserver {	

	/**
	 * DataSeries used to store the model's values
	 */
	private DataSeries series;
	
	/**
	 * Defines if the values of each update are appended to the previous values of the data series
	 * or sustitute them.
	 */
	boolean append;
	

	public DataSeriesUpdaterObserver(DataSeries series,boolean append){
		this.series = series;
		this.append = append;
	}

	/**
	 * Convenience constructor. Sets append to false 
	 * 
	 * @param series
	 */
	public DataSeriesUpdaterObserver(DataSeries series){
		this(series,false);
	}

	
	@Override
	public void updateValues(Model model, String name, Vector<Object> values) {
		
		if(!append)
			series.reset();
		
		for(Object value: values){
			series.addItem(new BaseDataItem((Double)value));

		}
		
	}



}

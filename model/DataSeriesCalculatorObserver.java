package edu.upc.cnds.collectivesim.model;

import java.util.Vector;

import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;
import edu.upc.cnds.collectivesim.dataseries.baseImp.BaseDataItem;
import edu.upc.cnds.collectivesim.dataseries.baseImp.MemoryDataSeries;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelObserver;

/**
 * 
 * A ModelObserver that adds the values from a model's agents to a DataSeries after applying a function.
 * 
 * @author Pablo Chacin
 *
 */
public class DataSeriesCalculatorObserver implements ModelObserver {	

	/**
	 * DataSeries used to store the model's values
	 */
	private DataSeries series;

	private DataSeries temp;

	private SeriesFunction function;

	public DataSeriesCalculatorObserver(DataSeries series,SeriesFunction function){
		this.series = series;
		this.function = function;
		this.temp = new MemoryDataSeries("temp");
	}



	@Override
	public void updateValues(Model model, String name, Vector<Object> values) {

		temp.reset();

		for(Object value: values){
			temp.addItem((Double)value);
		}

		Double value = temp.applyFunction(function);

		series.addItem(value);
	}



}

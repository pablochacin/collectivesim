package edu.upc.cnds.collectivesim.dataseries.baseImp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

import edu.upc.cnds.collectives.dataseries.InvalidDataItemException;
import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.DataItemFilter;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.DataSet;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;


/**
 * Basic Implementation of DataSets 
 * 
 * @author Pablo Chacin
 *
 */
public class BaseDataSet implements DataSet {

	
	
	private Map<String,BaseDataSeries> dataSeries;
	
	
	public BaseDataSet() {
		this.dataSeries = new HashMap<String, BaseDataSeries>();
	}
	
	
	public DataSeries addSeries(String name, int size) {
		BaseDataSeries series = new BaseDataSeries(name,size);
		dataSeries.put(name, series);
		return series;
	}

	public int getNumSeries() {
		return dataSeries.size();
	}

	public DataSeries getSeries(String name) {
		return dataSeries.get(name);
	}

	public String[] getSeriesNames() {
		throw new UnsupportedOperationException();
	}

	public void mergeDataSet(DataSet set) throws InvalidDataItemException {
		throw new UnsupportedOperationException();

	}

}

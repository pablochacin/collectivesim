package edu.upc.cnds.collectivesim.dataseries.baseImp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.DataItemFilter;
import edu.upc.cnds.collectivesim.dataseries.DataSequence;
import edu.upc.cnds.collectivesim.dataseries.DataSeriesObserver;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;

public class MemoryDataSeries implements DataSeries {

	private class DataItemsSequence implements DataSequence{
		
		private Vector<DataItem> items;
		
		private Iterator<DataItem> iterator;

		public DataItemsSequence(Vector<DataItem> items) {

			this.items = new Vector<DataItem>(items.size());
			this.items.addAll(items);
			iterator = this.items.iterator();
		}
		
		public DataItem getItem() {
			
			if(!iterator.hasNext()){
				return null;
			}
		
			return iterator.next();
		}

		@Override
		public boolean hasItems() {
			return iterator.hasNext();
		}
		
	}

	
	private Vector<DataItem> dataItems;

	private String name;

	private int maxSize;

	private static int DEFAULT_SIZE = 10;
	
	private List<DataSeriesObserver>observers;
	
	/**
	 * 
	 * @param name
	 * @param size maximun number of elements to hold. 0 means unbounded number of elements
	 */
	public MemoryDataSeries(String name,int size) {
		this.name = name;
		this.maxSize = size;
		this.dataItems = new Vector<DataItem>(maxSize>0?maxSize:DEFAULT_SIZE);
		this.observers = new ArrayList<DataSeriesObserver>();
	}

	public MemoryDataSeries(String name){
		this(name,0);
	}
	
	public boolean addItem(DataItem item) {
		
		if((maxSize > 0) && (dataItems.size() == maxSize)) {
			dataItems.remove(dataItems.firstElement());
		}
		
		dataItems.add(item);
		
		for(DataSeriesObserver o: observers){
			o.update(item);
		}
		
		return true;
	}

	public boolean addItem(Double value) {
		return addItem(new BaseDataItem(value));


	}

	public boolean addItem(Map categories, Double value){
		return addItem(new BaseDataItem(value,categories));

	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return dataItems.size();
	}

	public void resize(int size) {
		dataItems.setSize(size);
	}

	public void reset(){
		dataItems.clear();
	}
	
	public void setFilter(DataItemFilter filter) {
		throw new UnsupportedOperationException();

	}


	public DataItem getItem(int index) {

		return dataItems.elementAt(index);
	}


	public DataItem getItem() {
		if(dataItems.isEmpty())
		 return null;
			
		return getItem(getSize()-1);
	}
		

	public DataSequence getSequence() {
		return new DataItemsSequence(dataItems);
	}
	

	public void addObserver(DataSeriesObserver observer){
		observers.add(observer);
	}
	
	public void removeObserver(DataSeriesObserver observer){
		observers.remove(observer);
	}

	@Override
	public Double applyFunction(SeriesFunction function) {
		function.reset();
		
		for(DataItem i: dataItems){
			function.calculate(i);
		}
		
		return function.getResult();
	}

	
	public Double[] applyFunctions(SeriesFunction functions[]){
		
		for(SeriesFunction f: functions){
			f.reset();
		}
		
		for(DataItem i: dataItems){
			for(SeriesFunction f: functions){
				f.calculate(i);
			}
		}
		
		Double[] result = new Double[functions.length];
		for(int i=0;i < result.length;i++){
			result[i] = functions[i].getResult();
		}
		
		return result;
	}
	
}

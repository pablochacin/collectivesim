package edu.upc.cnds.collectivesim.dataseries.base;

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

/**
 * 
 * Implements a DataSeries as a in memory data structure
 * 
 * @author Pablo Chacin
 *
 */
public class BaseDataSeries implements DataSeries {

	/**
	 * A sequence of DataItems 
	 */
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
	
	protected int sequence;
	
	/**
	 * 
	 * @param name
	 * @param size maximum number of elements to hold. 0 means unbounded number of elements
	 */
	public BaseDataSeries(String name,int size) {
		this.name = name;
		this.maxSize = size;
		this.dataItems = new Vector<DataItem>(maxSize>0?maxSize:DEFAULT_SIZE);
		this.observers = new ArrayList<DataSeriesObserver>();
		this.sequence =  0;
	}

	public BaseDataSeries(String name){
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

	public boolean addItem(String attribute, Double value) {
		return addItem(new BaseDataItem(this,++sequence,attribute,value));

	}

	public boolean addItem(String attribute, Boolean value) {
		return addItem(new BaseDataItem(this,++sequence,attribute,value));

	}
	
	public boolean addItem(String attribute, String value) {
		return addItem(new BaseDataItem(this,++sequence,attribute,value));

	}
	public boolean addItem(Map attributes){
		
		return addItem(new BaseDataItem(this,++sequence,attributes));
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


	
}

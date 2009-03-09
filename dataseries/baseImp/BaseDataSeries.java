package edu.upc.cnds.collectives.dataseries.baseImp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.upc.cnds.collectives.dataseries.DataItem;
import edu.upc.cnds.collectives.dataseries.DataItemFilter;
import edu.upc.cnds.collectives.dataseries.DataSequence;
import edu.upc.cnds.collectives.dataseries.DataSequenceObserver;
import edu.upc.cnds.collectives.dataseries.DataSeries;
import edu.upc.cnds.collectives.dataseries.SeriesFunction;
import edu.upc.cnds.collectives.dataseries.InvalidDataItemException;

public class BaseDataSeries implements DataSeries {

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
	
	/**
	 * Generates a sequence of values appliying a Function to the values
	 * of a DataSeries
	 * 
	 * @author Pablo Chacin
	 *
	 */
	private class CalculatedSequence implements DataSequence,DataSequenceObserver {

		private SeriesFunction function;
		
		private DataItem value;

		private boolean updated;

		CalculatedSequence(SeriesFunction function){
			this.function = function;			

			this.value = null;
			
			this.updated = false;

		}
				
		/**
		 * Updates the calculated value iterating over the serie's items, from the newest one
		 * back, until items are exhausted or the functions stops visiting values.
		 */
		public void update(DataItem item){
			function.reset();
			boolean continueVisit = true;
			int i = dataItems.size()-1;
			while( (i >= 0) && continueVisit) {
				continueVisit = function.calculate(dataItems.get(i));
				i--;
			}

			value = new BaseDataItem(function.getResult());
			updated = true;
			
		}
		
		public DataItem getItem() {
			updated = false;
			return value;
			
		}

		@Override
		public boolean hasItems() {
		  return updated;
		}
		
		

	}
	
	private Vector<DataItem> dataItems;

	private String name;

	private int maxSize;

	private static int DEFAULT_SIZE = 10;
	
	private List<DataSequenceObserver>observers;
	
	/**
	 * 
	 * @param name
	 * @param size maximun number of elements to hold. 0 means unbounded number of elements
	 */
	public BaseDataSeries(String name,int size) {
		this.name = name;
		this.maxSize = size;
		this.dataItems = new Vector<DataItem>(maxSize>0?maxSize:DEFAULT_SIZE);
		this.observers = new ArrayList<DataSequenceObserver>();
	}

	public BaseDataSeries(String name){
		this(name,0);
	}
	
	public boolean addItem(DataItem item) throws InvalidDataItemException {
		
		if((maxSize > 0) && (dataItems.size() == maxSize)) {
			dataItems.remove(dataItems.firstElement());
		}
		
		dataItems.add(item);
		
		for(DataSequenceObserver o: observers){
			o.update(item);
		}
		
		return true;
	}

	public boolean addItem(Double value) throws InvalidDataItemException {
		return addItem(new BaseDataItem(value));


	}

	public boolean addItem(Map categories, Double value) throws InvalidDataItemException {
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
	
	
	
	public DataSequence getCalculatedSequence(SeriesFunction function) {
		CalculatedSequence sequence = new CalculatedSequence(function);
		observers.add(sequence);
		return  sequence;
	}

	public DataSequence getSequence() {
		return new DataItemsSequence(dataItems);
	}
	

	public void addObserver(DataSequenceObserver observer){
		observers.add(observer);
	}
	
	public void removeObserver(DataSequenceObserver observer){
		observers.remove(observer);
	}

	
}

package edu.upc.cnds.collectivesim.stream;

import java.util.Random;

import edu.upc.cnds.collectivesim.experiment.Table;

/**
 * Generates a Stream of values by picking randomly elements from a table
 * 
 * @author Pablo Chacin
 *
 * @param <T>
 */
public class RandomTableStream<T> implements Stream<T> {


	private String name;
	
	private Table<T>table;
	
	private Random rand;
	
	public RandomTableStream(String name,Table<T> table) {
		this.name = name;
		this.table= table;
		this.rand = new Random();
	}



	@Override
	public String getName() {
		return name;
	}



	@Override
	public T getValue() {
	 return table.getElement(rand.nextInt(table.getNumValues()));
	}



	@Override
	public void reset() {
		//do nothing
	}

	@Override
	public void open() {
		// Do nothing.
		
	}
	
	public String toString(){
		return "Table " + table.getName();
	}
}

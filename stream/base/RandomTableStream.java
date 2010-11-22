package edu.upc.cnds.collectivesim.stream.base;

import java.util.Date;
import java.util.Random;

import cern.jet.random.Empirical;
import cern.jet.random.EmpiricalWalker;
import cern.jet.random.engine.MersenneTwister64;

import edu.upc.cnds.collectivesim.random.ColtRandomEngine;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.table.Table;

/**
 * Generates a Stream of values by picking randomly elements from a table
 * 
 * @author Pablo Chacin
 *
 * @param <T>
 */
public class RandomTableStream<T> implements Stream<T> {


	
	private Table<T>values;
	
	private Table<Double>distribution;
	
	private EmpiricalWalker rand;
	
	public RandomTableStream(Table<T> values,Table<Double>distribution) {
		this.values= values;
		this.distribution = distribution;
		
		double[] histogram = new double[distribution.getNumValues()];
		for(int i=0;i<histogram.length;i++){
			histogram[i] = distribution.getElement(i);
		}
		
		this.rand = new EmpiricalWalker(histogram,Empirical.LINEAR_INTERPOLATION, 
				                  new ColtRandomEngine());

	}


	@Override
	public T nextElement() {
	 return values.getElement(rand.nextInt());
	}



	@Override
	public boolean hasMoreElements(){
		return true;
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
		return "Table Stream values " + values.toString() + 
		       " distribution " + distribution.toString();
	}
}

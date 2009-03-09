package edu.upc.cnds.collectivesim.model;

import java.util.Vector;

import edu.upc.cnds.collectivesim.experiment.Counter;

/**
 * Observes the Agents of a model and updates a counter with the observed value
 * 
 * @author Pablo Chacin
 *
 */
public class CounterUpdaterObserver implements ModelObserver {

	private Counter counter;
	
	public CounterUpdaterObserver(Counter counter){
		this.counter = counter;
	}
	
	@Override
	public void updateValues(Model model, String name, Vector<Object> values) {
		Double value = 0.0;
		for(Object o: values){
			value += (Double)o;
		}
		counter.increment(value);
		
	}
	

}

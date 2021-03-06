package collectivesim.stream.base;


import cern.jet.random.Empirical;
import collectivesim.random.ColtRandomEngine;
import collectivesim.stream.Stream;
import collectivesim.stream.StreamException;
import collectivesim.table.Table;

/**
 * 
 * Generates a Stream of random Double values in a given range, following an empirical distribution defined
 * in a table.
 * 
 *
 */
public class EmpiricalRandomStream implements Stream<Double> {
	
	/**	
	 * 
	 */
	Double min;
	
	Double max;
	
	private Table<Double> distribution;
	
	private Empirical rand;

		
	public EmpiricalRandomStream(Double min, Double max,Table<Double> distribution) {
		
		if(distribution == null){
			throw new IllegalArgumentException("Distribution can't be null");
		}

		this.distribution =distribution;
		this.min = min;
		this.max = max;
		double[] histogram = new double[distribution.getNumValues()];
		for(int i=0;i<histogram.length;i++){
			histogram[i] = distribution.getElement(i);
		}
		
		this.rand = new Empirical(histogram,Empirical.LINEAR_INTERPOLATION, 
				            new ColtRandomEngine());
		
	}


	@Override
	public Double nextElement() {		
		return min + (max-min)*rand.nextDouble();

	}
	

	@Override
	public void open() throws StreamException {
		//do nothing
		
	}

	@Override
	public void reset() {
		//do nothing
	}
	
	public String toString(){
		return "Empirical distribution " + distribution.toString() ;
	}

	@Override
	public boolean hasMoreElements() {
			return true;
	}
	
	
	
	
}

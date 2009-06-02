package edu.upc.cnds.collectivesim.dataseries;


/**
 * Generates a DataSeries of values applying a SeriesFunction to the values
 * of a source DataSeries
 * 
 * @author Pablo Chacin
 *
 */

public class FunctionCalculatorSeriesObserver implements DataSeriesObserver {


	/**
	 * Source values
	 */
	private DataSeries dataseries;

	/**
	 * Function used to calculate values
	 */
	private SeriesFunction function;

	/**
	 * Values calculated with the SeriesFunction
	 */
	private DataSeries result;


	public FunctionCalculatorSeriesObserver(DataSeries dataseries, DataSeries result,SeriesFunction function){
		this.dataseries = dataseries;
		this.function = function;			
		this.result = result;

		this.dataseries.addObserver(this);

	}

	/**
	 * Updates the calculated value iterating over the serie's items, from the newest one
	 * back, until items are exhausted or the functions stops visiting values.
	 */
	@Override
	public void update(DataItem item){
		function.reset();
		boolean continueVisit = true;
		int i = dataseries.getSize()-1;
		while( (i >= 0) && continueVisit) {
			continueVisit = function.processItem(dataseries.getItem(i));
			i--;
		}

		function.calculate(result);
	}					


}

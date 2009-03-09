package edu.upc.cnds.collectivesim.dataseries;

import edu.upc.cnds.collectivesim.dataseries.baseImp.BaseDataItem;

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
	private DataSeries calculatedValues;


	FunctionCalculatorSeriesObserver(DataSeries dataseries, DataSeries calculatedValues,SeriesFunction function){
		this.function = function;			
		this.calculatedValues = calculatedValues;

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
			continueVisit = function.calculate(dataseries.getItem(i));
			i--;
		}

		DataItem value = new BaseDataItem(function.getResult());
		calculatedValues.addItem(value);
	}					


}

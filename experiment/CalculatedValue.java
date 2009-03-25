package edu.upc.cnds.collectivesim.experiment;

/**
 * A valued calculated by applying a function to a series of 
 * values-
 * 
 * @author Pablo Chacin 
 *
 */
public class CalculatedValue implements StateValue {

	private String name;
		
	private StateValue[] arguments;
	
	private Function function;
	
	
	
	public CalculatedValue(String name, StateValue[] arguments, Function function) {
		this.name = name;
		this.arguments = arguments;
		this.function = function;
		
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Double getValue() {
		Double[] values = new Double[arguments.length];
		for(int i=0;i<values.length;i++){
			values[i] = arguments[i].getValue();
		}
		return function.calculate(values);
	}

}

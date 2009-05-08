package edu.upc.cnds.collectivesim.state;

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
	
	private ValueFunction function;
	
	
	
	public CalculatedValue(String name, StateValue[] arguments, ValueFunction function) {
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

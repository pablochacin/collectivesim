package collectivesim.state;

/**
 * Calculates a value from a series of values
 * 
 * 
 * @author PabloChacin
 *
 */
public interface ValueFunction {
	
	/**
	 * Calculates a value from the given arguments
	 * @param args an array of Double
	 * @return the calculated value.
	 * @throws IllegalArgumentException if the number of arguments is not the expected or
	 *         the function is not defined for the arguments' values (e.g. divide by zero)
	 */
	public Double calculate(Double ... args) throws IllegalArgumentException;
}

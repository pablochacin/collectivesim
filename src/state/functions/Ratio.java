package collectivesim.state.functions;

import collectivesim.state.ValueFunction;

/**
 * Calculates the ratio among two double values
 * 
 * @author Pablo Chacin
 *
 */
public class Ratio implements ValueFunction {

	
	public Double calculate(Double... args) throws IllegalArgumentException {
		
		if(args.length != 2){
			throw new IllegalArgumentException("Two arguments expected. "+ 
					                            args.length +"Arguments received");
		}
		
		if(args[1] == 0.0){
			//throw new IllegalArgumentException("Can't devide by 0");
			return 0.0;
		}
		
		
		return args[0] / args[1];
	}

}

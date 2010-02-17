package edu.upc.cnds.collectivesim.overlay.utility;

import edu.upc.cnds.collectives.node.Node;

public class FixedUtilityFunction implements UtilityFunction {

	Double utility;
	
	
	
	public FixedUtilityFunction(Double utility) {
		this.utility = utility;
	}



	@Override
	public Double getUtility(Node n) {
		return utility;
	}

}

package edu.upc.cnds.collectivesim.overlay.utility;

import edu.upc.cnds.collectives.node.Node;

public class ExponentialUtility implements UtilityFunction {

	Double exponent;
	
	public ExponentialUtility(Double exponent){
		this.exponent = exponent;
	}
	@Override
	public Double getUtility(Node node) {	
		
		Double capacity = (Double)node.getAttributes().get("capacity");
		Double load = (Double)node.getAttributes().get("load");
		
		Double utility = capacity/Math.pow(load+1.0, exponent);
		
		return utility;
		
	}

}

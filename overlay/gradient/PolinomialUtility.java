package edu.upc.cnds.collectivesim.overlay.gradient;

import edu.upc.cnds.collectives.node.Node;

public class PolinomialUtility implements UtilityFunction {

	Double limit;
	
	Double power;
	
	
	
	public PolinomialUtility(Double limit, Double power) {
		super();
		this.limit = limit;
		this.power = power;
	}



	@Override
	public Double getUtility(Node node) {
		
		Double capacity = (Double)node.getAttributes().get("capacity");
		Double load = (Double)node.getAttributes().get("load");
		
		Double utility  = capacity*Math.pow(Math.max(0,limit-load)/limit, power);
		
		return utility;
	}

}

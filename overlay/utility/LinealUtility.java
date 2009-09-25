package edu.upc.cnds.collectivesim.overlay.utility;

import edu.upc.cnds.collectives.node.Node;

public class LinealUtility implements UtilityFunction {

	Double maxLoad;
	
	public LinealUtility(Double maxLoad){
		this.maxLoad= maxLoad;
	}
	@Override
	public Double getUtility(Node node) {	
		
		Double capacity = (Double)node.getAttributes().get("capacity");
		Double load = (Double)node.getAttributes().get("load");
		
		Double utility = Math.max(0.0,capacity*(1.0-load/maxLoad));
		
		return utility;
		
	}

}

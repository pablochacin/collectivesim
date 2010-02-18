package edu.upc.cnds.collectivesim.overlay.utility;

import edu.upc.cnds.collectives.node.Node;

public class ResponseTimeUtilityFunction implements UtilityFunction {

	protected Double targetServiceTime;
	
	
	
	public ResponseTimeUtilityFunction(Double targetServiceTime) {
		this.targetServiceTime = targetServiceTime;
	}



	@Override
	public Double getUtility(Node node) {
		
		Double serviceTime = (Double)node.getAttributes().get("service.time");
		
		if((serviceTime == null) || (serviceTime.equals(Double.NaN))) {
			serviceTime = 0.0;
		}
		
		if (serviceTime > targetServiceTime) {
			return 0.0;
		}
		
		return (targetServiceTime-serviceTime)/targetServiceTime;
	}

}

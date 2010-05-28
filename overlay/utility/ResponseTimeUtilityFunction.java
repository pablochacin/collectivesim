package edu.upc.cnds.collectivesim.overlay.utility;

import edu.upc.cnds.collectives.node.Node;

public class ResponseTimeUtilityFunction implements UtilityFunction {

	protected Double targetServiceTime;
	
	protected Double alpha;
	
	
	
	public ResponseTimeUtilityFunction(Double targetServiceTime,Double alpha) {
		this.targetServiceTime = targetServiceTime;
		this.alpha =  alpha;
	}



	@Override
	public Double getUtility(Node node) {
		
		Double serviceTime = (Double)node.getAttributes().get("service.response");
		
		if((serviceTime == null) || (serviceTime.equals(Double.NaN))) {
			serviceTime = 0.0;
		}
		
		if (serviceTime > targetServiceTime) {
			return 0.0;
		}
		
		return Math.pow((targetServiceTime-serviceTime)/targetServiceTime,alpha);
	}

}

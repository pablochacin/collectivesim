package edu.upc.cnds.collectivesim.overlay.gradient;

import java.util.Map;

import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.MatchFunction;

public class UtilityMatchFunction implements MatchFunction {


	String attribute;
	
	public UtilityMatchFunction(String attribute) {
		this.attribute = attribute;
		
	}

	@Override
	public Double fitness(Destination destination, Map attributes) {
		Double utilityDestination = (Double)destination.getAttributes().get(attribute);
		Double utilityNode = (Double)attributes.get(attribute);
		
		
		Double difference = utilityNode - utilityDestination;
		
		if(difference < 0)
			return 0.0;
		else
			return 1.0-difference;
		
	}

	@Override
	public boolean match(Destination destination, Map attributes,
			Double tolerance) {

		 boolean match = fitness(destination,attributes) >= tolerance;
		 return match;
	}

	@Override
	public boolean match(Destination destination, Map attributes) {
		 return match(destination,attributes,destination.getTolerance());
		
	}

}

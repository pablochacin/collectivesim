package edu.upc.cnds.collectivesim.grid;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectiveg.GridResource;

/**
 * 
 * Describes a Resource in the Grid
 * 
 * @author Pablo Chacin
 *
 */
public class BasicGridResource implements GridResource {

	private int numPE;
	
	private Map attributes;
	
	private double speed;
		

	public BasicGridResource(int numPE, double speed,Map attributes) {
		this.numPE = numPE;
		this.attributes = attributes;
		this.speed = speed;
	}

	
	public BasicGridResource(int numPE,double speed){
		this(numPE,speed,new HashMap());
	}
	
	@Override
	public Map getAttributes() {
		return attributes;
	}

	@Override
	public int getNumPE() {
		return numPE;
	}


	@Override
	public double getSpeed() {
		return speed;
	}

	
}

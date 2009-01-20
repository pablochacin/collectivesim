package edu.upc.cnds.collectivesim.underlay;

import edu.upc.cnds.collectives.underlay.UnderlayAddress;

public class UnderlayModelNodeAddress implements UnderlayAddress{

	
	private String location;

	public UnderlayModelNodeAddress(String location) {
		this.location = location;
	}
	
	//TODO generate a meaningful location information
	public String getLocation() {
		return location;
	}
}
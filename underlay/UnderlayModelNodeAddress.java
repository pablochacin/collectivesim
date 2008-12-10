package edu.upc.cnds.collectivesim.underlay;

import edu.upc.cnds.collectives.underlay.UnderlayAddress;

public class UnderlayModelNodeAddress implements UnderlayAddress{

	private String address;
	
	private UnderlayModel underlay;
	
	public UnderlayModelNodeAddress(String address,UnderlayModel underlay) {
		this.underlay = underlay;
		this.address = address;
	}
	
	//TODO generate a mininful location information
	public String getLocation() {
		return address;
	}
	
	String getAddress(){
		return address;
	}
	
	UnderlayModel getUnderlay() {
		return underlay;
	}
	
}
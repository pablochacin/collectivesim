package edu.upc.cnds.collectivesim.underlay;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.NodeAddress;

public class UnderlayModelNodeAddress implements NodeAddress{

	private Identifier id;
	
	private UnderlayModel underlay;
	
	public UnderlayModelNodeAddress(Identifier id,UnderlayModel underlay) {
		this.id =  id;
		this.underlay = underlay;
	}
	
	//TODO generate a mininful location information
	public String getLocation() {
		return "node@"+ id.toString();
	}
	
	Identifier getId(){
		return id;
	}
	
	UnderlayModel getUnderlay() {
		return underlay;
	}
	
}
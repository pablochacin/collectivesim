package edu.upc.cnds.collectivesim.underlay.Grid2D;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.underlay.UnderlayAddress;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;
import edu.upc.cnds.collectivesim.underlay.UnderlayModelNode;


public class Grid2DUnderlayNode extends UnderlayModelNode{

	private Grid2DLocation location;
	
	public Grid2DUnderlayNode(Identifier id, UnderlayAddress address, Grid2DLocation location,UnderlayModel model) {	
		super(id, address, model);
		this.location = location;
	}

	public Grid2DLocation getLocation(){
		return location;
	}

}

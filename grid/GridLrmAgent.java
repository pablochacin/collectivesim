package edu.upc.cnds.collectivesim.grid;

import edu.upc.cnds.collectiveg.GridResource;
import edu.upc.cnds.collectiveg.InvalidResourceEspecification;
import edu.upc.cnds.collectiveg.baseImp.BasicGridLRM;

public class GridLrmAgent extends BasicGridLRM {

	GridLrmModel model;
	
	public GridLrmAgent(String name, GridResource[] resources, POLICY policy) throws InvalidResourceEspecification {
		super(name, resources, policy);
	}

	@Override
	public long getCurrentTime() {
		return model.getCurrentTime();
	}


}

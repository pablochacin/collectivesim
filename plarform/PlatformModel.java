package edu.upc.cnds.collectivesim.plarform;

import edu.upc.cnds.collectives.configuration.Configuration;
import edu.upc.cnds.collectives.execution.ExecutionService;
import edu.upc.cnds.collectives.platform.Platform;
import edu.upc.cnds.collectivesim.execution.ExecutionServiceModel;
import edu.upc.cnds.collectivesim.scheduler.SimulationModel;

public class PlatformModel implements Platform {

	private SimulationModel model;
	
	public PlatformModel(SimulationModel model) {
		this.model = model;
	}
	
	public Configuration getConfiguration() {
		throw new UnsupportedOperationException();
	}

	public ExecutionService getExecutionService() {
	
		return new ExecutionServiceModel(model);
	}
	

}

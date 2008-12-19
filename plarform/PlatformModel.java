package edu.upc.cnds.collectivesim.plarform;

import edu.upc.cnds.collectives.configuration.Configuration;
import edu.upc.cnds.collectives.execution.ExecutionService;
import edu.upc.cnds.collectives.platform.Platform;
import edu.upc.cnds.collectivesim.execution.ExecutionServiceModel;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;

public class PlatformModel implements Platform {

	private Scheduler scheduler;
	
	public PlatformModel(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public Configuration getConfiguration() {
		throw new UnsupportedOperationException();
	}

	public ExecutionService getExecutionService() {
	
		return new ExecutionServiceModel(scheduler);
	}
	

}

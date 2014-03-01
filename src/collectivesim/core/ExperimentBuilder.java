package collectivesim.core;

import java.util.Map;

import collectivesim.configuration.Configuration;
import collectivesim.configuration.ConfigurationException;
import collectivesim.experiment.Experiment;
import collectivesim.random.MersenneRandom;
import collectivesim.scheduler.base.BasicScheduler;

/**
 * Encapsulates the logic for building an Experiment
 * 
 * @author Pablo Chacin
 *
 */
public abstract class ExperimentBuilder {


	public Experiment build(Configuration config) throws ConfigurationException {
		
		String description = config.getParameters().getString("description");
		String workDir = config.getParameters().getString("directory");
		int runs = config.getParameters().getInteger("runs");
		long runLength = config.getParameters().getLong("duration");
		boolean exitOnEnd = config.getParameters().getBoolean("exit");
		long delay = config.getParameters().getLong("delay");
		long speed = config.getParameters().getLong("speed");
		
		Experiment experiment = new Experiment(description, new MersenneRandom(), new BasicScheduler(speed, runLength), workDir, runs, runLength, exitOnEnd, delay);
				
	
		addValues(experiment);

		addTables(experiment);

		addStreams(experiment);

		addModels(experiment);

		addBehaviors(experiment);

		addDataSeries(experiment);

		addViews(experiment);

		addBeginTasks(experiment);

		addInitTasks(experiment);

		addEndRunTasks(experiment);

		addFinalizationTasks(experiment);

		addPeriodicTasks(experiment);
		
		return experiment;
	}

	protected abstract void addPeriodicTasks(Experiment experiment);

	protected abstract void addFinalizationTasks(Experiment experiment);

	protected abstract void addEndRunTasks(Experiment experiment);

	protected abstract void addInitTasks(Experiment experiment);

	protected abstract void addBeginTasks(Experiment experiment);

	protected abstract void addViews(Experiment experiment);

	protected abstract void addDataSeries(Experiment experiment);

	protected abstract void addBehaviors(Experiment experiment);

	protected abstract void addModels(Experiment experiment);

	protected abstract void addStreams(Experiment experiment);

	protected abstract void addTables(Experiment experiment);

	protected abstract void addValues(Experiment experiment) ;

	protected abstract void setParameters(Map parameters, Experiment experiment);

	


}

package edu.upc.cnds.collectivesim.configuration;

import java.util.Iterator;
import java.util.Map;

import sun.reflect.misc.ReflectUtil;

import edu.upc.cnds.collectives.configuration.Configuration;
import edu.upc.cnds.collectives.configuration.ConfigurationElement;
import edu.upc.cnds.collectives.configuration.ConfigurationException;
import edu.upc.cnds.collectives.util.ReflectionUtils;
import edu.upc.cnds.collectivesim.experiment.Experiment;

/**
 * Encapsulates the logic for building an Experiment
 * 
 * @author Pablo Chacin
 *
 */
public class ExperimentBuilder {


	public static Experiment build(ConfigurationElement element) throws ConfigurationException {

		Experiment experiment;
				
		setParameters(element.getParameters(),experiment);

		Configuration experimentConfiguration = element.getSubConfiguration();
		
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
	}

	private static void addStreams(Experiment experiment) {
		// TODO Auto-generated method stub
		
	}

	private static void addTables(Experiment experiment) {
		// TODO Auto-generated method stub
		
	}

	private static void addCounters(Map<String,String> counters,Experiment experiment) {
		
		for(String c: counters.keySet()){
			experiment.addCounter(c);
		}
		
	}

	private static void addCalculatedValues(Map<String,ConfigurationElement> counters,Experiment experiment) {
		
		for(ConfigurationElement e: counters.values()){
			e.instantiate();
		}
		
	}
	
	
	/**
	 * Sets the parameters for the experiment
	 * 
	 * @param parameters a Map with the parameters
	 * @param experiment
	 * 
	 * @throws ConfigurationException
	 */
	private static void setParameters(Map<String,String> parameters,Experiment experiment) throws ConfigurationException {

		for(String param: parameters.keySet()){
			experiment.getParameters().put(param, ReflectionUtils.parseValue((String)parameters.get(param)));
		}
		
		
	}


}

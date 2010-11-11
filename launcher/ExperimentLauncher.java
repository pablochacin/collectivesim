package edu.upc.cnds.collectivesim.launcher;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.configuration.Configuration;
import edu.upc.cnds.collectives.configuration.propertiesConf.PropertiesConfiguration;
import edu.upc.cnds.collectivesim.experiment.Experiment;



public class ExperimentLauncher implements Runnable {


	private static String DEFAULT_LOG_CONFIGURATION = "log4j.properties";


	private Map argsMap;

	/**
	 * Constructor with initialization arguments in an String array. Creates and
	 * initializes the hosting environment. 
	 * Convenient for applications that are executed from a command line.
	 * 
	 * @param args a String array with the arguments.
	 */
	public ExperimentLauncher(String[] args) {
		this.argsMap = getArguments(args);
	}


	/**
	 * Constructor with the initialization arguments in a Map. Creates and initializes
	 * the hosting environment.
	 * 
	 * @param argsMap a Map with initialization arguments
	 */
	public ExperimentLauncher(Map argsMap) {
		this.argsMap = new HashMap(argsMap);
	}

	/**
	 * Constructor without parameters. Requieres  the method <code>initialize</code> 
	 * to be called to complete the middleware initialization.
	 */
	public ExperimentLauncher(){
		super();
	}




	/**
	 * Transform an array of strings of the form -key value and creates a Map 
	 * of <key,value> pairs. 
	 * 
	 * @param args
	 * @return
	 */
	private static Map getArguments(String[] args) {
		//load the parametes passed to in the args into a map
		HashMap argsMap = new HashMap();

		//check an even number of elements
		if(args.length % 2 != 0) {
			throw new InvalidParameterException("invalid syntax of argument lists");
		}

		for(int i = 0;i<args.length;i+=2){

			if(!args[i].startsWith("-")) {
				throw new InvalidParameterException("invalid syntax of argument lists");
			}

			//Add to map removing leading "-"
			argsMap.put(args[i].substring(1),args[i+1]);
		}

		return argsMap;
	}


	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args){
		try {
			new ExperimentLauncher(args).run();
		} catch (Exception e) {
			System.err.println("Exception initializing the Launcher");
			e.printStackTrace(System.err);
		}

	}

	public void run() {

		try{

			Experiment experiment;
			
			Configuration config;


			//		//configure logging. If no log config file has been specified, load from the gmm's jar
			//		String logConfig = (String)argsMap.get("config.log");
			//		if( logConfig == null){
			//			logConfig = DEFAULT_LOG_CONFIGURATION;
			//		}
			//		
			//		PropertyConfiguration.configure(logConfig);


			config = new PropertiesConfiguration();

			//check if a configuration file was specified 
			//if not, set a default file
			String configFile = (String)argsMap.get("config.file");

			//second, load the configuration supplied at startup
			if( configFile != null){
				config.load(configFile);
			}

			// second load the configuration passed by command line
			// it can overwrite definitions of the configuration file
			config.load(argsMap);

			config.build();
			
			experiment = (Experiment) config.getElement("experiment","").instantiate();
			
			experiment.start();

		}catch(Exception e){
			throw new RuntimeException(e);
		}
	} 


}

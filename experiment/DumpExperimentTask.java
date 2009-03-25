package edu.upc.cnds.collectivesim.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Utilitary class that dumps the experiment's description and relevant information (parameters and resulting values)
 * 
 * @author Pablo Chacin
 *
 */
public class DumpExperimentTask implements Runnable {

	private static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-DD-HH:mm:ss";
	
	private static String DEFAULT_TIME_FORMAT = "DD:HH:mm:ss";
		
	private Experiment experiment;
	
	private String outFileName;
	
	private SimpleDateFormat dateFormater,elapseTimeFormater;
	
	
	public DumpExperimentTask(Experiment experiment,String outFileName){
		this.experiment = experiment;
		this.outFileName = outFileName;
		this.dateFormater = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
		this.elapseTimeFormater = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
	}
	
	
	@Override
	public void run() {
		
		File outfile = new File(experiment.getWorkingDirectory(),outFileName);
		
		try {
			PrintStream out = new PrintStream(new FileOutputStream(outfile));
			
			dumpDescription(out);
			
			dumpParameters(out);
			
			dumpValues(out);
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	protected void dumpDescription(PrintStream out ){
		
		out.println(experiment.getDescription());
		out.println("Begin time: "+ dateFormater.format(new Date(experiment.beginTime())));
		out.println("Execution time:  " + elapseTimeFormater.format(new Date(experiment.endTime()-experiment.beginTime())));
		out.println("Simulation time: " +experiment.getRunTime());
	}
	
	
	protected void dumpParameters(PrintStream out){
		out.println("Parameters");
		for(Map.Entry<String, Object> e: experiment.getParameters().entrySet()){
			out.println(e.getKey() +"=" +e.getValue().toString());
		}
	}
	
	protected void dumpValues(PrintStream out){
		out.println("Values");
		for(Map.Entry<String, Double> v: experiment.getState().entrySet()){
			out.println(v.getKey() +"=" +v.getValue().toString());
		}
	}
}

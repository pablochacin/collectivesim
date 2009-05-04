package edu.upc.cnds.collectivesim.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DurationFormatUtils;

import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.DataSequence;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.experiment.Table;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * Utility class that dumps the experiment's description and relevant information (parameters and resulting values)
 * 
 * @author Pablo Chacin
 *
 */
public class DumpExperimentTask implements Runnable {

	private static final String DEFAULT_DURATION_FORMAT = "HH:mm:ss";


	private static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-DD-HH:mm:ss";
	
		
	private Experiment experiment;
	
	private String outFileName;
	
	
	public DumpExperimentTask(Experiment experiment,String outFileName){
		this.experiment = experiment;
		this.outFileName = outFileName;

	}
	
	
	@Override
	public void run() {
		
		File outfile = new File(experiment.getWorkingDirectory(),outFileName);
		
		try {
			PrintStream out = new PrintStream(new FileOutputStream(outfile));
			
			dumpDescription(out);
			
			dumpParameters(out);
			
			dumpStreams(out);
			
			dumpValues(out);
						
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	protected void dumpStreams(PrintStream out ){
		
		out.println("\n ------ Streams ----- \n");
		
		for(Stream s: experiment.getStreams().values()){
			out.println(s.getName() + ": " + s.toString());
		}
	}
	

	protected void dumpTables(PrintStream out ){
		
		out.println("\n ------ Tables ----- \n");
		
		for(Table t: experiment.getTables().values()){
			out.println(t.getName() + ": " + t.toString());
		}
	}
	
	
	
	protected void dumpDescription(PrintStream out ){
		
		out.println(experiment.getDescription());
		out.println("Begin time: "+ DateFormatUtils.format(experiment.beginTime(),DEFAULT_DATE_TIME_FORMAT));
		out.println("Execution time:  " + DurationFormatUtils.formatDuration(experiment.endTime()-experiment.beginTime(),DEFAULT_DURATION_FORMAT));
		out.println("Simulation time: " +experiment.getRunTime());
	}
	
	
	protected void dumpParameters(PrintStream out){
		out.println("\n ------ Parameters ----- \n");
		for(Map.Entry<String, Object> e: experiment.getParameters().entrySet()){
			out.println(e.getKey() +"=" +e.getValue().toString());
		}
	}
	
	protected void dumpValues(PrintStream out){
		
		out.println("\n ------ Values ----- \n");
		
		for(Map.Entry<String, Double> v: experiment.getState().entrySet()){
			out.println(v.getKey() +"=" +v.getValue().toString());
		}
	}
}

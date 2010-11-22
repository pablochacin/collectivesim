package edu.upc.cnds.collectivesim.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DurationFormatUtils;

import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.table.Table;

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
		
		Map<String,Stream> streamMap = new TreeMap();
		streamMap.putAll(experiment.getStreams());
		
		for(Entry<String, Stream> s: streamMap.entrySet()){
			out.println(s.getKey() + ": " + s.getValue().toString());
		}
	}
	

	protected void dumpTables(PrintStream out ){
		
		out.println("\n ------ Tables ----- \n");
		
		Map<String,Table> tableMap = new TreeMap();
		tableMap.putAll(experiment.getTables());
		
		for(Table t: tableMap.values()){
			out.println(t.getName() + ": " + t.toString());
		}
	}
	
	
	
	protected void dumpDescription(PrintStream out ){
		
		out.println(experiment.getDescription());
		out.println("Run length: "+ experiment.getRunLength());		
		out.println("Begin time: "+ DateFormatUtils.format(experiment.beginTime(),DEFAULT_DATE_TIME_FORMAT));
		out.println("Execution time:  " + DurationFormatUtils.formatDuration(experiment.endTime()-experiment.beginTime(),DEFAULT_DURATION_FORMAT));
		out.println("Simulation time: " +experiment.getRunTime());
	}
	
	
	protected void dumpParameters(PrintStream out){
		out.println("\n ------ Parameters ----- \n");
		
		Map<String,Object> parameterMap = new TreeMap();
		parameterMap.putAll(experiment.getParameters());
		
		for(Map.Entry<String, Object> e: parameterMap.entrySet()){
			out.println(e.getKey() +"=" +e.getValue().toString());
		}
	}
	
	protected void dumpValues(PrintStream out){
		
		out.println("\n ------ Values ----- \n");
		
		Map<String,Double> valueMap = new TreeMap();
		valueMap.putAll(experiment.getState());
		
		for(Map.Entry<String, Double> v: valueMap.entrySet()){
			out.println(v.getKey() +"=" +v.getValue().toString());
		}
	}
}

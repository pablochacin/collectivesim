package edu.upc.cnds.collectivesim.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.DataSequence;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.experiment.Experiment;

/**
 * 
 * Dumps a DataSeries to a text file. Prints a line for each item, selecting the attributes
 * specified in the constructor. The file has a header line with the name of the attributes.
 * 
 * @author Pablo Chacin
 *
 */
public class DumpDataSeriesTask implements Runnable {

	private static String DEFAULT_SEPARATOR = ",";

	/**
	 * Name of the data series
	 */
	protected DataSeries dataseries;

	/**
	 * List of attributes to print in the output
	 */
	protected String[] attributes;

	/**
	 * Experiment context
	 */
	protected Experiment experiment;

	/**
	 * Separator for elements
	 */
	protected String separator;


	/**
	 * Append data to file if it exists
	 */
	protected boolean append;
	
	/**
	 * Indicates if the ouput should be put into the experiment's path (true) or the run's path (false) 
	 */
	protected boolean useExperimentPath;
	
	/**
	 * Constructor
	 * @param dataseries
	 * @param attributes
	 * @param experiment
	 * @param separator
	 */
	public DumpDataSeriesTask(Experiment experiment,String dataseries, String[] attributes,String separator,boolean append,boolean useExperimentPath) {
		super();		
		this.dataseries = experiment.getDataSeries(dataseries);
		this.attributes = attributes;
		this.experiment = experiment;
		this.separator = separator;
		this.append = append;
		this.useExperimentPath = useExperimentPath;
	
	}

	/**
	 * Convenience Constructor with default separator
	 * 
	 * @param dataseries
	 * @param attributes
	 * @param experiment
	 */
	public DumpDataSeriesTask(Experiment experiment,String dataseries, String[] attributes) {
		this(experiment,dataseries,attributes,DEFAULT_SEPARATOR,false,false);
	}

	/**
	 * Convenience constructor. Attributes are listed in a String separated by ","
	 * @param experiment
	 * @param dataseries
	 * @param attributes
	 */
	public DumpDataSeriesTask(Experiment experiment,String dataseries, String attributes) {
		this(experiment,dataseries,attributes.split(","));
	}

	@Override
	public void run() {


		String directory;
		
		//select the target directory. Can't be established in the constructor 
		//because working directory has not yet been created 
		
		if(useExperimentPath)
			directory = experiment.getRootDirectory();
		else
			directory = experiment.getWorkingDirectory();
		
		File outfile = new File(directory,dataseries.getName()+".txt");
		
		boolean newFile = !outfile.exists();
		
		try {
			PrintStream out = new PrintStream(new FileOutputStream(outfile,append));

			StringBuffer header = new StringBuffer();
			header.append(attributes[0]);
			for(int i = 1;i<attributes.length;i++){
				header.append(separator);
				header.append(attributes[i]);
			}
			
			//if the file is new or the existing one will be overriden, then add a header
			if(newFile || !append)
				out.println(header);
			
			DataSequence items = dataseries.getSequence();
			while(items.hasItems()){
				DataItem item = items.getItem();
				StringBuffer itemLine = new StringBuffer();

				itemLine.append(item.getString(attributes[0]));
				for(int i = 1;i<attributes.length;i++){
					itemLine.append(separator);
					itemLine.append(item.getString(attributes[i]));
				}
				out.println(itemLine);
			}
			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			return;
		}

	}


}

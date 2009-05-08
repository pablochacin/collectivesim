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
	protected String dataseries;

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
	 * Constructor
	 * @param dataseries
	 * @param attributes
	 * @param experiment
	 * @param separator
	 */
	public DumpDataSeriesTask(Experiment experiment,String dataseries, String[] attributes,String separator) {
		super();
		this.dataseries = dataseries;
		this.attributes = attributes;
		this.experiment = experiment;
		this.separator = separator;
	}

	/**
	 * Convenience Constructor with default separator
	 * 
	 * @param dataseries
	 * @param attributes
	 * @param experiment
	 */
	public DumpDataSeriesTask(Experiment experiment,String dataseries, String[] attributes) {
		this(experiment,dataseries,attributes,DEFAULT_SEPARATOR);
	}

	/**
	 * Convenienve constructor. Attributes are listed in a String separated by ","
	 * @param experiment
	 * @param dataseries
	 * @param attributes
	 */
	public DumpDataSeriesTask(Experiment experiment,String dataseries, String attributes) {
		this(experiment,dataseries,attributes.split(","),DEFAULT_SEPARATOR);
	}

	@Override
	public void run() {

		DataSeries series = experiment.getDataSeries(dataseries);

		File outfile = new File(experiment.getWorkingDirectory(),dataseries+".txt");
		try {
			PrintStream out = new PrintStream(new FileOutputStream(outfile));

			StringBuffer header = new StringBuffer();
			header.append(attributes[0]);
			for(int i = 1;i<attributes.length;i++){
				header.append(separator);
				header.append(attributes[i]);
			}
			
			out.println(header);
			
			DataSequence items = series.getSequence();
			while(items.hasItems()){
				DataItem item = items.getItem();
				StringBuffer itemLine = new StringBuffer();
				itemLine.append(item.getSequence());
				for(String attribute: attributes){
					itemLine.append(separator);
					itemLine.append(item.getString(attribute));
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

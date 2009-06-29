package edu.upc.cnds.collectivesim.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.dataseries.DataItem;
import edu.upc.cnds.collectivesim.dataseries.DataSequence;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;

/**
 * 
 * Exports a Topology Model as an GraphML file
 * 
 * @author Pablo Chacin
 *
 */
public class ExportTopologyTask implements Runnable {


	protected Experiment experiment;
	
	protected String fileName;
	
	protected OverlayModel model;


	/**
	 * Constructor
	 * @param dataseries
	 * @param attributes
	 * @param experiment
	 * @param separator
	 */
	public ExportTopologyTask(Experiment experiment,OverlayModel model,String fileName) {
		this.experiment = experiment;
		this.model= model;
		this.fileName = fileName;
	}

	

	public ExportTopologyTask(Experiment experiment,OverlayModel model) {
		
		this(experiment, model, model.getName()+".xml");
		
	}

	private static String GRAPHML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
	   "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" " +
	   "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
	   "xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns " +  
	   "http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\" >" ;


	@Override
	public void run() {


		File outfile = new File(experiment.getWorkingDirectory(),fileName);
		try {
			OutputStream out = new FileOutputStream(outfile);

		    PrintWriter outGraph = new PrintWriter(out, true);

		    outGraph.println(GRAPHML_HEADER);
		    outGraph.println("<graph id=\"" + model.getName() + "\"" + 
		    		         " edgedefault=\"directed\">");
		    
		    for(ModelAgent a: model.getAgents()){
		    	outGraph.println("<node id=\""+a.getName()+"\" />");
		    }
		    
		    for(ModelAgent a: model.getAgents()){

		    	
		    	List<Node> links = ((OverlayAgent)a).getOverlay().getNodes();
		    	
		    	for(Node n: links){
		    		outGraph.println("<edge source=\""+a.getName()+"\" "+
		    				                "target=\""+n.getId().toString() + "\" />");
		    	}
		    }
		    
		    outGraph.println("</graph>");
		    outGraph.println("</graphml>");
		    
		    
		    outGraph.flush();
		    outGraph.close();


		} catch (FileNotFoundException e) {
			return;
		}

	}


}

package edu.upc.cnds.collectivesim.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.simulation.overlay.OverlayAgent;
import edu.upc.cnds.collectives.simulation.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelAgent;

/**
 * 
 * Exports a Topology Model as an GraphML file
 * 
 * @author Pablo Chacin
 *
 */
public class DumpOverlayToPajek implements Runnable {


	protected Experiment experiment;
	
	protected String fileName;
	
	protected OverlayModel model;
	
	/**
	 * Maintains the name and type of node attributes
	 */
	protected Map<String,String> nodeAttributes;
	
	/**
	 * 
	 * @param experiment
	 * @param model
	 * @param fileName
	 * @param nodeAttributes
	 * @param edgeAttributes
	 */
	public DumpOverlayToPajek(Experiment experiment,OverlayModel model,String fileName,Map nodeAttributes) {
		this.experiment = experiment;
		this.model= model;
		this.fileName = fileName;
		this.nodeAttributes = nodeAttributes;
		
	}

	public DumpOverlayToPajek(Experiment experiment,OverlayModel model,String fileName) {
		this(experiment,model,fileName,new HashMap());
	}
	

	public DumpOverlayToPajek(Experiment experiment,OverlayModel model) {
		
		this(experiment, model, model.getName());
		
	}

	
	@Override
	public void run() {


		File outfile = new File(experiment.getWorkingDirectory(),fileName+".net");

		Map<String,PrintWriter> attrFiles = new HashMap<String,PrintWriter>();
		
		
		try {
			OutputStream out = new FileOutputStream(outfile);

			PrintWriter outGraph = new PrintWriter(out, true);

		 	for(String attr: nodeAttributes.keySet()){
	    		OutputStream attrOut = new FileOutputStream(new File(experiment.getWorkingDirectory(),attr+".vec"));
	    		PrintWriter attrFile = new PrintWriter(attrOut,true);
	    		attrFiles.put(attr,attrFile);
	    	}
			
			String verticesHeader  = "*Vertices "+model.getAgents().size();
		    outGraph.println(verticesHeader);
		    		    
	    	for(String attr: nodeAttributes.keySet()){
	    		PrintWriter attrFile = attrFiles.get(attr);
	    		attrFile.println(verticesHeader);
	    	}

		    
	    	Map <String,String> vertices = new HashMap<String,String>();
	    	
		    int i =0;
		    for(ModelAgent a: model.getAgents()){
		    	i++;
		    	Node node = ((OverlayAgent)a).getLocalNode();
		    
		    	vertices.put(node.getId().toString(), String.valueOf(i));
		    	
		    	outGraph.println(i + " \"" + node.getId().toString()+"\"");

		    	for(String attr: nodeAttributes.keySet()){
		    		PrintWriter attrFile = attrFiles.get(attr);
		    		attrFile.println(node.getAttributes().get(attr).toString());
		    	}

		    	
		    }
		    
		    //In pajek's format, directed graphs has arcs (not edges!)
		    outGraph.println("*arcs");
		    
		    for(ModelAgent a: model.getAgents()){

		    	Node node = ((OverlayAgent)a).getLocalNode();

		    	String vertice = vertices.get(node.getId().toString());
		    	
		    	List<Node> links = ((OverlayAgent)a).getNeighbors();
		    	
		    	for(Node n: links){
		    		//check that end of edge exists
		    		if(vertices.get(n.getId().toString()) != null){
		    			outGraph.println(vertice + " " + vertices.get(n.getId().toString()));		    					    	
		    		}
		    	}
		    }
		    
		    		    
		    outGraph.flush();
		    outGraph.close();

	    	for(String attr: nodeAttributes.keySet()){
	    		PrintWriter attrFile = attrFiles.get(attr);
	    		attrFile.flush();
	    		attrFile.close();
	    	}

		    

		} catch (FileNotFoundException e) {
			return;
		}

	}


}

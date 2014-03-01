package collectivesim.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import collectivesim.experiment.Experiment;
import collectivesim.model.ModelAgent;
import collectivesim.model.ModelException;
import collectivesim.network.NetworkModel;

/**
 * 
 * Exports a Network for a Model as an Pajek file
 * 
 * @author Pablo Chacin
 *
 */
public class DumpNetworkToPajek implements Runnable {


	protected Experiment experiment;
	
	protected String fileName;
	
	protected NetworkModel model;
	
	protected String network;
	
	/**
	 * Maintains the name and type of agent attributes
	 */
	protected Map<String,String> agentAttributes;
	
	/**
	 * 
	 * @param experiment
	 * @param model
	 * @param fileName
	 * @param agentAttributes
	 * @param edgeAttributes
	 */
	public DumpNetworkToPajek(Experiment experiment,NetworkModel model,String network,String fileName,Map<String,String> agentAttributes) {
		this.experiment = experiment;
		this.model= model;
		this.network = network;
		this.fileName = fileName;
		this.agentAttributes = agentAttributes;
		
	}

	public DumpNetworkToPajek(Experiment experiment,NetworkModel model,String network,String fileName) {
		this(experiment,model,network,fileName,new HashMap<String,String>());
	}
	

	public DumpNetworkToPajek(Experiment experiment,NetworkModel model,String network) {
		
		this(experiment, model, network,model.getName()+network+".net");
		
	}

	
	@Override
	public void run() {


		File outfile = new File(experiment.getWorkingDirectory(),fileName);

		Map<String,PrintWriter> attrFiles = new HashMap<String,PrintWriter>();
		
		
		try {
			OutputStream out = new FileOutputStream(outfile);

			PrintWriter outGraph = new PrintWriter(out, true);

		 	for(String attr: agentAttributes.keySet()){
	    		OutputStream attrOut = new FileOutputStream(new File(experiment.getWorkingDirectory(),attr+".vec"));
	    		PrintWriter attrFile = new PrintWriter(attrOut,true);
	    		attrFiles.put(attr,attrFile);
	    	}
			
			String verticesHeader  = "*Vertices "+model.getAgents().size();
		    outGraph.println(verticesHeader);
		    		    
	    	for(String attr: agentAttributes.keySet()){
	    		PrintWriter attrFile = attrFiles.get(attr);
	    		attrFile.println(verticesHeader);
	    	}

		    
	    	Map <String,String> vertices = new HashMap<String,String>();
	    	
		    int i =0;
		    for(ModelAgent a: model.getAgents()){
		    	i++;
		    			    
		    	vertices.put(a.getName(), String.valueOf(i));
		    	
		    	outGraph.println(i + " \"" + a.getName()+"\"");

		    	for(String attr: agentAttributes.keySet()){
		    		PrintWriter attrFile = attrFiles.get(attr);
		    		try {
						attrFile.println(a.inquire(attr).toString());
					} catch (ModelException e) {
						e.printStackTrace();
					}
		    	}

		    	
		    }
		    
		    //In pajek's format, directed graphs has arcs (not edges!)
		    outGraph.println("*arcs");
		    
		    for(ModelAgent a: model.getAgents()){

		    	String vertice = vertices.get(a.getName());
		    	
		    	List<ModelAgent> links = model.getNeighbors(network,a);
		    	
		    	for(ModelAgent n: links){
		    		//check that end of edge exists
		    		if(vertices.get(n.getName()) != null){
		    			outGraph.println(vertice + " " + vertices.get(n.getName()));		    					    	
		    		}
		    	}
		    }
		    
		    		    
		    outGraph.flush();
		    outGraph.close();

	    	for(String attr: agentAttributes.keySet()){
	    		PrintWriter attrFile = attrFiles.get(attr);
	    		attrFile.flush();
	    		attrFile.close();
	    	}

		    

		} catch (FileNotFoundException e) {
			return;
		}

	}


}

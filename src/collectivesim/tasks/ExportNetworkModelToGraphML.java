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
 * Exports a Topology Model as an GraphML file
 * 
 * @author Pablo Chacin
 *
 */
public class ExportNetworkModelToGraphML<A extends ModelAgent> implements Runnable {


	protected Experiment experiment;
	
	protected String fileName;
	
	protected NetworkModel model;
	
	/**
	 * Maintains the name and type of node attributes
	 */
	protected Map<String,String> nodeAttributes;

	/**
	 * Maintains the name and type of edge attributes
	 */
	protected Map<String,String> edgeAttributes;
	
	/**
	 * Maintains the name and key of node attributes
	 */
	protected Map<String,String> nodeAttrKey;
	
	/**
	 * Maintains the name and key of edge attributes
	 */
	protected Map<String,String> edgeAttrKey;
	

	protected String network;
	
	/**
	 * 
	 * @param experiment
	 * @param model
	 * @param network name of the network to export
	 * @param fileName
	 * @param nodeAttributes
	 * @param edgeAttributes
	 */
	public ExportNetworkModelToGraphML(Experiment experiment,NetworkModel model,String network,String fileName,Map<String,String> nodeAttributes,Map<String,String> edgeAttributes) {
		this.experiment = experiment;
		this.model= model;
		this.network = network;
		this.fileName = fileName;
		this.nodeAttributes = nodeAttributes;
		this.edgeAttributes = edgeAttributes;
		this.nodeAttrKey = new HashMap<String,String>();
		this.edgeAttrKey = new HashMap<String,String>();
		
		//generate keys
		int i=0;
		for(String a: this.nodeAttributes.keySet()){
			nodeAttrKey.put(a, "d"+i);
		}
		
		for(String a: this.edgeAttributes.keySet()){
			edgeAttrKey.put(a, "d"+i);
		}

	}

	public ExportNetworkModelToGraphML(Experiment experiment,NetworkModel model,String network,String fileName) {
		this(experiment,model,network,fileName,new HashMap<String,String>(),new HashMap<String,String>());
	}
	

	public ExportNetworkModelToGraphML(Experiment experiment,NetworkModel model,String network) {
		
		this(experiment, model, network,model.getName()+"_"+network+".xml");
		
	}

	private static String GRAPHML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
	   "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" " +
	   "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
	   "xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns " +  
	   "http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\" >" ;

	private static String ATTR_KEY = "<key id=\"%s\" for=\"%s\" attr.name=\"%s\" attr.type=\"%s\" />";

	private static String DATA_ELEMENT = "<data key=\"%s\"> %s</data>";
	
	@Override
	public void run() {


		File outfile = new File(experiment.getWorkingDirectory(),fileName);
		try {
			OutputStream out = new FileOutputStream(outfile);

		    PrintWriter outGraph = new PrintWriter(out, true);

		    outGraph.println(GRAPHML_HEADER);

		    //headers for attributes
		    
		    for(String a: nodeAttributes.keySet()){
		    	String attrDef = String.format(ATTR_KEY, nodeAttrKey.get(a),"node",a,nodeAttributes.get(a).toString());
		    	outGraph.println(attrDef);
		    }

		    for(String a: edgeAttributes.keySet()){
		    	String attrDef = String.format(ATTR_KEY, edgeAttributes.get(a),"edge",a,edgeAttributes.get(a));
		    	outGraph.println(attrDef);

		    }

		    
		    outGraph.println("<graph id=\"" + model.getName() + "\"" + 
		    		         " edgedefault=\"directed\">");

		    
		    for(ModelAgent a: model.getAgents()){
		    	
		    	outGraph.println("<node id=\""+a.getName()+ "\" >");
		    	
		    	for(String attr: nodeAttributes.keySet()){
		    		String data = String.format(DATA_ELEMENT,nodeAttrKey.get(attr),a.inquire(attr).toString());
		    		outGraph.println(data);
		    	}
		    	
		    	outGraph.println("</node>");
		    }
		    
		    for(ModelAgent a: model.getAgents()){

		    	
		    	List<ModelAgent> links = model.getNeighbors(network, a);
		    	
		    	for(ModelAgent n: links){
		    		outGraph.println("<edge source=\""+a.getName()+"\" "+
		    				                "target=\""+n.getName().toString() + "\" >");
		    		
			    	for(String attr: edgeAttributes.keySet()){
			    		
						try {
							String data = String.format(DATA_ELEMENT,edgeAttrKey.get(attr),n.inquire(attr).toString());
				    		outGraph.println(data);

						} catch (ModelException e) {
							e.printStackTrace();
						}
			    	}
			    	
			    	outGraph.println("</edge>");

		    	}
		    }
		    
		    outGraph.println("</graph>");
		    outGraph.println("</graphml>");
		    
		    
		    outGraph.flush();
		    outGraph.close();


		} catch (FileNotFoundException e) {
			return;
		} catch (ModelException e) {
			e.printStackTrace();
		}

	}


}

package collectivesim.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import collectives.node.Node;
import collectives.simulation.overlay.OverlayAgent;
import collectives.simulation.overlay.OverlayModel;
import collectivesim.dataseries.DataItem;
import collectivesim.dataseries.DataSequence;
import collectivesim.dataseries.DataSeries;
import collectivesim.experiment.Experiment;
import collectivesim.model.ModelAgent;

/**
 * 
 * Exports a Topology Model as an GraphML file
 * 
 * @author Pablo Chacin
 *
 */
public class DumpOverlayToGraphML implements Runnable {


	protected Experiment experiment;
	
	protected String fileName;
	
	protected OverlayModel model;
	
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
	


	/**
	 * 
	 * @param experiment
	 * @param model
	 * @param fileName
	 * @param nodeAttributes
	 * @param edgeAttributes
	 */
	public DumpOverlayToGraphML(Experiment experiment,OverlayModel model,String fileName,Map nodeAttributes,Map edgeAttributes) {
		this.experiment = experiment;
		this.model= model;
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

	public DumpOverlayToGraphML(Experiment experiment,OverlayModel model,String fileName) {
		this(experiment,model,fileName,new HashMap(),new HashMap());
	}
	

	public DumpOverlayToGraphML(Experiment experiment,OverlayModel model) {
		
		this(experiment, model, model.getName()+".xml");
		
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
		    	
		    	Node node = ((OverlayAgent)a).getOverlay().getLocalNode();
		    	outGraph.println("<node id=\""+node.getId().toString()+ "\" >");
		    	
		    	for(String attr: nodeAttributes.keySet()){
		    		String data = String.format(DATA_ELEMENT,nodeAttrKey.get(attr),node.getAttributes().get(attr).toString());
		    		outGraph.println(data);
		    	}
		    	
		    	outGraph.println("</node>");
		    }
		    
		    for(ModelAgent a: model.getAgents()){

		    	
		    	List<Node> links = ((OverlayAgent)a).getOverlay().getNodes();
		    	
		    	for(Node n: links){
		    		outGraph.println("<edge source=\""+a.getName()+"\" "+
		    				                "target=\""+n.getId().toString() + "\" >");
		    		
			    	for(String attr: edgeAttributes.keySet()){
			    		String data = String.format(DATA_ELEMENT,edgeAttrKey.get(attr),n.getAttributes().get(attr));
			    		outGraph.println(data);
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
		}

	}


}

package collectivesim.visualization.network.jung;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.ConstantEdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.ConstantVertexAspectRatioFunction;
import edu.uci.ics.jung.graph.decorators.ConstantVertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.ConstantVertexSizeFunction;
import edu.uci.ics.jung.graph.decorators.EllipseVertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.ToolTipFunction;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.io.GraphMLFile;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.LayoutMutable;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.VertexLocationFunction;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import collectivesim.model.ModelAgent;
import collectivesim.visualization.network.AgentRenderer;
import collectivesim.visualization.network.AgentRenderingException;
import collectivesim.visualization.network.AgentView;


public class JungAgentView implements AgentView {

	/**
	 * Name of the User datum used to store a node's tooltip
	 */
	private static final String  TOOL_TIP = "TOOL_TIP";	
	
	/**
	 * Name of te user datum used to store a node's label
	 */
	private static final String  LABEL = "LABEL";
	

	/**
	 * Agent attribute used as tooltip
	 */
	String tooltip;
	
	/**
	 * Agent attribute used as label
	 */
	String label;
	

	/**
	 * Label vertex using a user supplied datum
	 *
	 */
	private class VertexLabelStringer implements VertexStringer{
		

		public String getLabel(ArchetypeVertex v) {
			String label = (String)v.getUserDatum(LABEL );
			return label;
		}
		
	}
	
	private class VertexToolTip implements ToolTipFunction{

		private String datum;
		
		public VertexToolTip(String datum) {
			this.datum = datum;
		}
		
		public String getToolTipText(Vertex v) {
			String tip = (String)v.getUserDatum(datum );
			return tip;
		}

		public String getToolTipText(Edge arg0) {
				return null;
		}

		public String getToolTipText(MouseEvent arg0) {
			return null;
		}
		
	}
	

	private Graph graph;
	
	private Map<String,Vertex> vertexes;

	private VisualizationViewer viewer;

	private LayoutMutable layout;

	/**
	 * Indicates if graph must be updated after a change
	 */
	private boolean autoRefresh = false;


	private String name;
	
	private int sizeX,sizeY;
	
	/**
	 * 
	 * @param space
	 * @param labelAgents indicates if nodes must be labled with its ID
	 */
	public JungAgentView(String name,String tooltip, String label, int sizeX, int sizeY,VertexLocationFunction locationFunction,VertexPaintFunction colorFunction, boolean labelAgents) {
		
		this.name = name;
		this.tooltip = tooltip;
		this.label = label;
		graph = new DirectedSparseGraph();
		vertexes = new HashMap<String,Vertex>();

		this.sizeX =  sizeX;
		this.sizeY = sizeY;
		
		//Create a viewer with a default CircularLayout
		//layout = (LayoutMutable) new FRLayout(graph );
		//
		layout = (LayoutMutable) new GenericGraphLayout(locationFunction,graph);
		//layout = new FRLayout(graph);
		((Layout) layout).initialize(new Dimension(sizeX,sizeY));
		
		PluggableRenderer pr = new PluggableRenderer();
		pr.setVertexPaintFunction(colorFunction);
		pr.setEdgePaintFunction(new  ConstantEdgePaintFunction(Color.lightGray, null));
		//pr.setVertexPaintFunction(new ColorScaleVertexPaintFunction(Color.black,"utility",0.0,1.0));	
		//pr.setVertexPaintFunction(new ConstantVertexPaintFunction(Color.black,Color.lightGray));
		//pr.setVertexPaintFunction(new DiscreteColorScaleVertexPaintFunction(Color.black,"utility",0.0,1.0,5));
		
		pr.setVertexShapeFunction(  new EllipseVertexShapeFunction(
                new ConstantVertexSizeFunction(10),
                new ConstantVertexAspectRatioFunction(1.0f)));
		
		if(labelAgents){
			pr.setVertexStringer(new VertexLabelStringer());
		}
		
		viewer = new VisualizationViewer((Layout) layout,pr);
		viewer.setVisible(true);
		viewer.setToolTipFunction(new VertexToolTip(TOOL_TIP) );
		viewer.init();

	}

	/**
	 * Convenience constructor. Use labels for nodes
	 * 
	 * @param space
	 */
	public JungAgentView(String name,int sizeX,int sizeY,VertexLocationFunction locationFunction) {
		this(name,null,null,sizeX,sizeY,locationFunction,new ConstantVertexPaintFunction(Color.lightGray, null),false);
	}

	
	public void add(ModelAgent agent) throws AgentRenderingException {

		if(vertexes.containsKey(agent.getName())) {
			throw new AgentRenderingException("Agent: " + agent.getName().toString()+ "already exists");
		}

		Vertex vertex = new AgentVertex(agent);
		
		vertexes.put(agent.getName(),vertex);
		graph.addVertex(vertex);
		update();
	}

	public void connect(ModelAgent src, ModelAgent target) throws AgentRenderingException {
		Vertex srcVertex = vertexes.get(src.getName());
		Vertex  targetVertex = vertexes.get(target.getName());

		// connect vertexes after verifying they exists and are not connected
		if((srcVertex != null) && (targetVertex != null)){

			if(srcVertex.findEdge(targetVertex)!= null){
				throw new AgentRenderingException("Agents alredy Connected" +
		                  " ["+src.toString() + ","+target.toString()+"]");
			}

			graph.addEdge(new DirectedSparseEdge(srcVertex,targetVertex));
			update();
		}
		else {
			throw new AgentRenderingException("Source or Target not found" + 
					                  " ["+src.toString() + ","+target.toString()+"]");
		}

	}


	public void disconnect(ModelAgent src, ModelAgent target) throws AgentRenderingException {
		Vertex srcVertex = vertexes.get(src.getName());
		Vertex  targetVertex = vertexes.get(target.getName());
		// connect vertexes after verifying they exists and are not connected
		if((srcVertex != null) && (targetVertex != null)){
			Edge edge = srcVertex.findEdge(targetVertex);
			if(edge== null){
				throw new AgentRenderingException("Agents not Connected" +
		                  " ["+src.toString() + ","+target.toString()+"]");
			}

			graph.removeEdge(edge);
			update();
		}
		else {
			throw new AgentRenderingException("Source or Target not found" +
	                  " ["+src.toString() + ","+target.toString()+"]");
		}


	}

	public void removeNode(ModelAgent agent) throws AgentRenderingException{

		Vertex vertex = vertexes.get(agent.getName());
		if(vertex == null) {
			throw new AgentRenderingException("Agent " + agent.getName().toString() +" does not exist");
		}

		graph.removeVertex(vertex);
		vertexes.remove(agent.getName());
		update();
	}

	public void tagNode(ModelAgent agent, String tag) throws AgentRenderingException {
		throw new UnsupportedOperationException();
		
	}
	

	//TODO: see who invokes this method. 
	private void update() {
		if(autoRefresh) {
			refresh();
		}
	}

	public boolean setAutoUpdate(boolean autoUpdate) {
		this.autoRefresh = autoUpdate;
		return this.autoRefresh;
	}


	public boolean isAutoRefresh() {
		return autoRefresh;
	}

	public void refresh() {
		
		layout.update();
		if(layout.isIncremental()){
		
			while(!layout.incrementsAreDone()){
				layout.advancePositions();		
			}
		}
		else{
			viewer.repaint();
		}

	}


	

	public void setAgentRenderer(AgentRenderer renderer) {
		throw new UnsupportedOperationException();		
	}


	@Override
	public void clear() {
		graph.removeAllVertices();
		refresh();
	}

	@Override
	public JPanel getViewableContent() {
		return viewer;
	}

	@Override
	public boolean needRefresh() {
		//When not in auto refresh mode, indicates is data 
		return true;
	}
	
	
	
	

	@Override
	public String getName() {
		return name;
	}


	@Override
	public int getWidth() {
		return sizeY;
	}

	@Override
	public int getHeight() {
		return sizeX;
	}

	@Override
	public void print(Graphics g) {

		viewer.paint(g);
		
	}

	@Override
	public void exportView(OutputStream out) {
		
		GraphMLFile graphXml = new GraphMLFile();
		
		graphXml.save(graph, new PrintStream(out));
		
	}

	@Override
	public void remove(ModelAgent agent) throws AgentRenderingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tag(ModelAgent agent, String tag)
			throws AgentRenderingException {
		// TODO Auto-generated method stub
		
	}

}

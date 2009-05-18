package edu.upc.cnds.collectivesim.visualization.nodes.jungImp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.ConstantEdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.ConstantVertexAspectRatioFunction;
import edu.uci.ics.jung.graph.decorators.ConstantVertexColorFunction;
import edu.uci.ics.jung.graph.decorators.ConstantVertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.ConstantVertexSizeFunction;
import edu.uci.ics.jung.graph.decorators.EllipseVertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.ToolTipFunction;
import edu.uci.ics.jung.graph.decorators.VertexColorFunction;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.visualization.FRLayout;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.LayoutMutable;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.VertexLocationFunction;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.identifier.Imp.CircularIdSpace;
import edu.upc.cnds.collectives.identifier.Imp.DirectedCircularIdSpace;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.base.BasicNode;
import edu.upc.cnds.collectivesim.visualization.Viewer;
import edu.upc.cnds.collectivesim.visualization.nodes.NodeDrawer;
import edu.upc.cnds.collectivesim.visualization.nodes.NodeRenderingException;
import edu.upc.cnds.collectivesim.visualization.nodes.NodeView;


public class JungNodeView implements NodeView {

	/**
	 * Name of the User datum used to store a node's tooltip
	 */
	private static final String  TOOL_TIP = "TOOL_TIP";	
	
	/**
	 * Name of te user datum used to store a node's label
	 */
	private static final String  LABEL = "LABEL";
	
	/**
	 * Name of user datum used to store the node's ID.
	 */
	private static final String  ID = "ID";

	/**
	 * Node attribute used as tooltip
	 */
	String tooltip;
	
	/**
	 * Node attribute used as label
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
			// TODO Auto-generated method stub
			return null;
		}

		public String getToolTipText(MouseEvent arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	

	private Graph graph;
	
	private Map<Identifier,Vertex> vertexes;

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
	 * @param labelNodes indicates if nodes must be labled with its ID
	 */
	public JungNodeView(String name,String tooltip, String label, int sizeX, int sizeY,VertexLocationFunction locationFunction,VertexPaintFunction colorFunction, boolean labelNodes) {
		
		this.name = name;
		this.tooltip = tooltip;
		this.label = label;
		graph = new DirectedSparseGraph();
		vertexes = new HashMap<Identifier,Vertex>();

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
		
		if(labelNodes){
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
	public JungNodeView(String name,int sizeX,int sizeY,VertexLocationFunction locationFunction) {
		this(name,null,null,sizeX,sizeY,locationFunction,new ConstantVertexPaintFunction(Color.lightGray, null),false);
	}

	
	public void addNode(Node node) throws NodeRenderingException {

		if(vertexes.containsKey(node.getId())) {
			throw new NodeRenderingException("Node: " + node.getId().toString()+ "already exists");
		}

		Vertex vertex = new NodeVertex(node);
		
		vertexes.put(node.getId(),vertex);
		graph.addVertex(vertex);
		update();
	}

	public void connect(Node src, Node target) throws NodeRenderingException {
		Vertex srcVertex = vertexes.get(src.getId());
		Vertex  targetVertex = vertexes.get(target.getId());

		// connect vertexes after verifying they exists and are not connected
		if((srcVertex != null) && (targetVertex != null)){

			if(srcVertex.findEdge(targetVertex)!= null){
				throw new NodeRenderingException("Nodes alredy Connected" +
		                  " ["+src.toString() + ","+target.toString()+"]");
			}

			graph.addEdge(new DirectedSparseEdge(srcVertex,targetVertex));
			update();
		}
		else {
			throw new NodeRenderingException("Source or Target not found" + 
					                  " ["+src.toString() + ","+target.toString()+"]");
		}

	}


	public void disconnect(Node src, Node target) throws NodeRenderingException {
		Vertex srcVertex = vertexes.get(src.getId());
		Vertex  targetVertex = vertexes.get(target.getId());
		// connect vertexes after verifying they exists and are not connected
		if((srcVertex != null) && (targetVertex != null)){
			Edge edge = srcVertex.findEdge(targetVertex);
			if(edge== null){
				throw new NodeRenderingException("Nodes not Connected" +
		                  " ["+src.toString() + ","+target.toString()+"]");
			}

			graph.removeEdge(edge);
			update();
		}
		else {
			throw new NodeRenderingException("Source or Target not found" +
	                  " ["+src.toString() + ","+target.toString()+"]");
		}


	}

	public void removeNode(Node node) throws NodeRenderingException{

		Vertex vertex = vertexes.get(node.getId());
		if(vertex == null) {
			throw new NodeRenderingException("Node " + node.getId().toString() +" does not exist");
		}

		graph.removeVertex(vertex);
		vertexes.remove(node.getId());
		update();
	}

	public void tagNode(Node node, String tag) throws NodeRenderingException {
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


	

	public void setNodeDrawer(NodeDrawer drawer) {
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

}

package edu.upc.cnds.collectivesim.visualization.nodes;

import java.io.OutputStream;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.visualization.View;





/**
 * Renders the elements defined by the visualizer
 * 
 * @author Pablo Chacin
 *
 */
public interface NodeView extends View {

	
	/**
	 * Sets the NodeDrawer used to draw each node.
	 * 
	 * @param drawer a NodeDrawer to draw nodes
	 */
	public void setNodeDrawer(NodeDrawer drawer);
	
	
	
	/**
	 * Display a node with the given Identifier and label
	 * @param node
	 * @param label
	 * @throws NodeRenderingException if a node with the same id already exists
	 */
	public void addNode(Node node) throws NodeRenderingException;  
	
	
	
	/**
	 * Remove a node with the given Identifier
	 * @param node
	 * @param label
	 * @throws NodeRenderingException if no node with the id exists
	 */
	public void removeNode(Node node) throws NodeRenderingException;
	
	/**
	 * Draw a connection between two nodes, given their Identifiers
	 * @param src source the node
	 * @param target the target node
	 * @throws NodeRenderingException if any of the nodes doesn't exists or they are 
	 *                         already connected
	 */
	public void connect(Node src,Node target) throws NodeRenderingException;
		
	/**
	 * Removes a connection between the nodes 
	 *  
	 * @param src the source node
	 * @param target the target node
	 * @throws NodeRenderingException if any of the nodes doesn't exists or they are nor 
	 *                         currently connected
	 */
	public void disconnect(Node src,Node target) throws NodeRenderingException;
	
	
	/**
	 * Sets the auto-refresh mode on/off
	 * @param autoUpdate
	 * @return the current value. Must be checked because not all renders 
	 *         support setting the auto-refresh on/off.
	 */
	public boolean setAutoUpdate(boolean autoUpdate);
	
	
	/**
	 * Add a tag to the given identifier 
	 * @param id
	 */
	public void tagNode(Node node,String tag) throws NodeRenderingException;
	
	/**
	 * Exports the view as a GraphML file
	 * @param out
	 */
	public void exportView(OutputStream out);
}

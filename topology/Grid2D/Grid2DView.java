package edu.upc.cnds.collectivesim.topology.Grid2D;

import java.util.List;

import edu.uci.ics.jung.utils.AbstractVertexMapper;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.topology.NodeView;
import edu.upc.cnds.collectives.topology.NodeViewObserver;
import edu.upc.cnds.collectives.topology.imp.BasicNodeView;

/**
 * Implements a Node view based on a 2D Grid.
 * 
 * Starting from the node, it returns the node's neighbors 
 * within a given scope.
 * 
 * @author Pablo Chacin
 *
 */
public class Grid2DView implements NodeView{

	/**
	 * Address of the node 
	 */
	private Grid2DAddress address;
	
	/**
	 * Maximun size of the view. 
	 */
	private int maxSize;
	
	/**
	 * Scope of the neighborhood
	 */
	private int scope;
	
	
	/**
	 * Grid from wich the view is built
	 */
	private Grid2D grid;
	
	
	
	public Grid2DView(Grid2DAddress address, int maxSize, int scope, Grid2D grid) {
		super();
		this.address = address;
		this.maxSize = maxSize;
		this.scope = scope;
		this.grid = grid;
	}



	public int getMaxSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Node> getNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void notAlive(List<Node> node) {
		// TODO Auto-generated method stub
		
	}

	public void notAlive(Node node) {
		// TODO Auto-generated method stub
		
	}

	public void propose(List<Node> candidates) {
		// TODO Auto-generated method stub
		
	}

	public void propose(Node node) {
		// TODO Auto-generated method stub
		
	}

	public void removeObserver(NodeViewObserver observer) {
		// TODO Auto-generated method stub
		
	}

	public void setMaxSize(int size) {
		// TODO Auto-generated method stub
		
	}

	public void addObserver(NodeViewObserver observer) {
		// TODO Auto-generated method stub
		
	}




}

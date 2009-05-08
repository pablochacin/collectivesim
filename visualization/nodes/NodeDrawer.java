package edu.upc.cnds.collectivesim.visualization.nodes;

import java.awt.Graphics;

import edu.upc.cnds.collectives.node.Node;

public interface NodeDrawer  {

	/**
	 * Draws a node in a graphics space 
	 * 
	 * @param node
	 * @param g
	 * @param x
	 * @param y
	 * @param sizeX
	 * @param sizeY
	 */
	public abstract void drawNode(Node node,Graphics g,int x,int y,int sizeX,int sizeY);
}

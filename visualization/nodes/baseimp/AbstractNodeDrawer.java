package edu.upc.cnds.collectivesim.visualization.nodes.baseimp;


import java.awt.Color;
import java.awt.Graphics;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.visualization.nodes.NodeDrawer;


/**
 * 
 * Returns a graphical representation of the node based on its attributes
 * 
 * @author Pablo Chacin
 *
 */
public abstract class AbstractNodeDrawer implements NodeDrawer {

    protected String attribute;

   
    /**
     * Sets the cell the viewer must observe
     */
    public AbstractNodeDrawer(String attribute){
        this.attribute = attribute;
    }
    

     
	/**
	 * Draws the given node 
	 * 
	 * @param node
	 * @param g
	 * @param x
	 * @param y
	 * @param sizeX
	 * @param sizeY
	 */
	public abstract void drawNode(Node node,Graphics g,int x,int y,int sizeX,int sizeY);
	
	/**
	 * Sets the color of a graphics context
	 * @param g
	 * @param c
	 */
	protected Color setColor(Graphics g,Color c) {
		
		Color currentColor = g.getColor(); 
		if (currentColor != c) {
			g.setColor(c);
		}
		return currentColor;
	   
}

/**
 * Helper function to draw a rectangle of the given color
 * @param g
 * @param x
 * @param y
 * @param sizeX
 * @param sizeY
 * @param color
 */
 protected void drawRectangle(Graphics g,int x,int y,int sizeX,int sizeY,Color color) {
 	Color c = setColor(g,color);
    g.fillRect(x, y, sizeX, sizeY);
    setColor(g,c);
    return;
 }
 
}

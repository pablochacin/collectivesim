package edu.upc.cnds.collectivesim.views.nodes;


import java.awt.Color;
import java.awt.Graphics;

import edu.upc.cnds.collectivesim.topology.grid2d.Cell;

/**
 * A Cell that can be drawn in a 2D display space. 
 * 
 * It is responsible of  observing an attrubute of the agent(s) at a
 * given cell and return it's graphical representation
 * 
 * @author Pablo Chacin
 *
 */
public abstract class NodeViewer implements Drawable {
	//coordinate space
    protected String attribute;
    protected Cell cell;
	
    
    /**
     * Sets the cell the viewer must observe
     */
    public void setCell(Cell cell){
        this.cell= cell ;
    }
    

    
	/**
	 * sets the agent's attribute this cell must observe
	 */
    
	public void setAttribute(String attribute) {

		this.attribute = attribute;

	}
   
    
	/**
	 * Observes one agent's attribute and records its value
     * 
     *  TODO: generalize to allow more than one agent per cell
     *  and the application of some operator on this list of agents
     *  to obtain a single attribute (for instance, the maximun,
     *  minimun, average, total, count)
	 */

	protected Object observeAttribute(){

		return cell.handelInquire(attribute);
	}
	

	/**
	 * draw cell
	 */
	public abstract void draw(Graphics g);
	
	
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

package collectivesim.visualization.network.baseimp;


import java.awt.Color;
import java.awt.Graphics;

import collectivesim.model.ModelAgent;
import collectivesim.visualization.network.AgentRenderer;
import collectivesim.visualization.network.AgentRenderingException;


/**
 * 
 * Returns a graphical representation of the agent based on its attributes
 * 
 * @author Pablo Chacin
 *
 */
public abstract class AbstractAgentDrawer implements AgentRenderer {

    protected String attribute;

   
    /**
     * Sets the cell the viewer must observe
     */
    public AbstractAgentDrawer(String attribute){
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
	 * @throws AgentRenderingException 
	 */
	public abstract void draw(ModelAgent agent,Graphics g,int x,int y,int sizeX,int sizeY) throws AgentRenderingException;
	
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

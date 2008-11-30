package edu.upc.cnds.collectivesim.views.nodes;

import java.awt.Graphics;


/**
 * 
 * Interface that must implement any element that can be drawn. 
 * 
 * @author Pablo Chacin
 *
 */
public interface Drawable {

	/**
	 * Requests the element to draw itself in a graphics context, at a given position
	 * and with a maximun size
	 * 
	 * @param g graphic context
	 * @param x 
	 * @param y
	 * @param sizeX
	 * @param sizeY
	 */
	public void draw(Graphics g,int x,int y,int sizeX,int sizeY);
     
     
}

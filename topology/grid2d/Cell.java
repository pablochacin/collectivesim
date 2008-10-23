package edu.upc.cnds.collectivesim.topology.grid2d;

import edu.upc.cnds.collectivesim.topology.Node;


/**
 * Represents a Node with coordinates in a 2D grid
 * 
 * @author pchacin
 *
 */
public interface Cell extends Node {
    
    
    /**
     * Return the X coodinate of this cell
     * @return
     */
    public int getX();
    
    /**
     * Return the Y coordinate of this cell
     * @return
     */
    public int getY();
    
    /**
     * Sets the coordinates of the cell
     * @param x
     * @param y
     */

    public void setCoordinates(int x, int y);

    
}

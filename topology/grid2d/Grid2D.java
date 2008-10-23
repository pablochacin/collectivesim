package edu.upc.cnds.collectivesim.topology.grid2d;


import java.util.List;

import edu.upc.cnds.collectivesim.agents.Agent;


/**
 * 
 * Defines the interface to access and modify a set of nodes located in a
 * discrete two dimentional space of sizeX*sizeY locations. 
 * Coordinates in this space are pairs of integers in the range [0-sixeX][0-sizeY].
 * 
 * @author Pablo Chacin
 *
 */
public interface Grid2D {


	/**
	 * Returns the Cell at the given location. 
	 * 
	 * @param x coordinate in the x axis (colums)
	 * @param y coordinate in the y axis (rowy
	 * @return the location at the given coordinates. Null if it is empty.
	 */
	public Cell getCell(int x,int y);
	
    
    /**
     * Returns all the Cells of the space
     */
    public List getCells();

/**
	 * Tests if the given location is empty or occupied.
	 * 
	 * @param x coordinate in the x axis (colums)
	 * @param y coordinate in the y axis (rows)
	 * 
	 * @return true is the cell is empty.
	 */
	public boolean isEmpty(int x,int y);

	/**
	 * Checks if it is possible to put an agent at the given location. For multi-agent
	 * locations, this should be always true. For single agent locations, <code>isAvailable()</code>
	 *  is equalto <code>isEmpty()</code>
	 */
	public boolean isAvailable(int x, int y);
	

	/**
	 * Returns the size of the space in the X coordinates. Coordinates of the space
	 * range from 0 to getSizeX()-1
	 * 
	 * @return an integer with the size of the X coordinates 
	 */
	public int getSizeX();
	
	/**
	 * Returns the size of the space in the Ycoordinates. Coordinates of the space
	 * range from 0 to getSizeY()-1
	 * 
	 * @return an integer with the size of the Y coordinates 
	 */
	public int getSizeY();
}

package edu.upc.cnds.collectivesim.topology.Grid2D;


/**
 * 
 * A strategy to position nodes in a Grid.
 * 
 * 
 * @author Pablo Chacin
 *
 */
public interface LocationStrategy {
	
	/**
	 * 
	 * 
	 * @return an Address with the location of the node
	 */
	public Grid2DLocation getLocation(Grid2D grid) throws Grid2DException;

}

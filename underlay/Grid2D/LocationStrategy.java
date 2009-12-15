package edu.upc.cnds.collectivesim.underlay.Grid2D;


/**
 * 
 * A strategy to assign Locations in a Grid. Can be used to generate 
 * Locations using a specific distribution pattern, like an statistical
 * distribution function (e.g. uniform).
 * 
 * @author Pablo Chacin
 *
 */
public interface LocationStrategy {
	
	/**
	 * @return a free Location in the grid
	 * 
	 * @throws Grid2DLocation location if no free location can be found
	 */
	public Grid2DLocation getLocation(Grid2DTopology topology) throws UnderlayModelException;

}

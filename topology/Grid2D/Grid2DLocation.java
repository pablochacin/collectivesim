package edu.upc.cnds.collectivesim.topology.Grid2D;

/**
 * A Location in a 2D Grid
 * @author pchacin
 *
 */
public class Grid2DLocation {

	private int coordX;
	
	private int coordY;
		
	public Grid2DLocation(Grid2DModel grid,int coordX, int coordY) {
		super();
		this.coordX = coordX;
		this.coordY = coordY;
	}

	
	public int getCoordX() {
		return coordX;
	}

	public int getCoordY() {
		return coordY;
	}
	
		
}

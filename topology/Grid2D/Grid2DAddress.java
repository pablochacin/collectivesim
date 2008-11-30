package edu.upc.cnds.collectivesim.topology.Grid2D;

import edu.upc.cnds.collectives.node.NodeAddress;

public class Grid2DAddress implements NodeAddress {

	private int x;
	
	private int y;
	
	private Grid2D grid;
	
	Grid2DAddress(int x,int y,Grid2D grid) {
		this.x = x;
		this.y = y;
		this.grid = grid;
	}
	
	int getX() {
		return x;
	}
	
	
	int getY() {
		return y;
	}
	
	public String getLocation() {
		return  "[" + x +","+y+"]";
	}

}

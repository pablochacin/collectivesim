package edu.upc.cnds.collectivesim.underlay.Space2D;

import edu.upc.cnds.collectives.underlay.UnderlayNode;

public class Space2DLocation {
	
	Double positionX;
	
	Double positionY;
	
	int gridX,gridY;
	
	UnderlayNode node;
	
	Space2DTopology topology;

	public Space2DLocation(Space2DTopology topology,UnderlayNode node,Double x, Double y) {
		super();
		this.positionX = x;
		this.positionY = y;
		this.topology = topology;
		this.node = node;
		this.gridX = (int)((positionX/topology.getSizeX())*(topology.getGridSizeX()-1));
		this.gridY = (int)((positionY/topology.getSizeY())*(topology.getGridSizeY()-1));
	}

	
	public Space2DLocation(Space2DTopology topology,Double x, Double y) {
		this(topology,null,x,y);
	}
	
	
	public int getGridX() {
		return gridX;
	}




	public int getGridY() {
		return gridY;
	}




	public UnderlayNode getNode() {
		return node;
	}


	public void setNode(UnderlayNode node) {
		this.node = node;
	}


	public Double getCoordX() {
		return positionX;
	}

	public void setCoordX(Double x) {
		this.positionX = x;
	}

	public Double getCoordY() {
		return positionY;
	}

	public void setCoordY(Double y) {
		this.positionY = y;
	}

	
}

package edu.upc.cnds.collectivesim.views.grids;

import java.util.HashMap;

import edu.upc.cnds.collectivesim.agents.Agent;

import uchicago.src.sim.gui.SimGraphics;

/**
 * 
 * Draws the agent as a graphic based on its type
 * 
 * @author Pablo Chacin 
 * 
 */
public class GraphicsCell extends Drawable2DCell {

	//Map of images to used. Indexed by agent type
	private HashMap images;
	
	public GraphicsCell(HashMap images){
		super();
		this.images = images;
		
		//set type as attribute to observe
		setAttribute("AgentType");
	}
	
	public void draw(SimGraphics g) {
		
		
	}

	
}

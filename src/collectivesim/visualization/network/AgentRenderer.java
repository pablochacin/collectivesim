package collectivesim.visualization.network;

import java.awt.Graphics;

import collectivesim.model.ModelAgent;

public interface AgentRenderer  {

	/**
	 * Renders a ModelAgent in a graphics space 
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
}

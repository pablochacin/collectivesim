package collectivesim.visualization.network.baseimp;

import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import collectivesim.model.ModelAgent;
import collectivesim.model.ModelException;
import collectivesim.visualization.network.AgentRenderer;
import collectivesim.visualization.network.AgentRenderingException;



/**
 * 
 * Draws the agent as a graphic based on an attribute
 * 
 * @author Pablo Chacin 
 * 
 */
public class ImageAgentRenderer implements AgentRenderer {

	/**
	 * Map of images to used. Indexed by agent type
	 */
	private Map<String,Image> images;
	
	/*
	 * Attribute used to select the image
	 */
	private String attribute;
	

	
	/**
	 * Constructor with the map of images
	 * @param images
	 * @param attribute
	 */
	public ImageAgentRenderer(ModelAgent agent,String attribute,HashMap<String, Image> images){

		this.attribute = attribute;
		this.images = images;
		
	}

	

	@Override
	public void draw(ModelAgent agent, Graphics g, int x, int y, int sizeX, int sizeY) {
		Image image;
		try {
			image = images.get(agent.inquire(attribute));
		
			if(image != null) {
				g.drawImage(image, x, y, null);
			}
		} catch (ModelException e) {
			e.printStackTrace();
		}
	}


	
}

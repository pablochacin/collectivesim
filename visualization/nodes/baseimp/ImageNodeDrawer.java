package edu.upc.cnds.collectivesim.visualization.nodes.baseimp;

import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.visualization.nodes.NodeDrawer;


/**
 * 
 * Draws the agent as a graphic based on an attribute
 * 
 * @author Pablo Chacin 
 * 
 */
public class ImageNodeDrawer implements NodeDrawer {

	/**
	 * Map of images to used. Indexed by agent type
	 */
	private Map<String,Image> images;
	
	/*
	 * Attribute used to select the image
	 */
	private String attribute;
	
	
	private Node node;
	
	/**
	 * Constructor with the map of images
	 * @param images
	 * @param attribute
	 */
	public ImageNodeDrawer(Node node,String attribute,HashMap<String, Image> images){
		this.node = node;
		this.attribute = attribute;
		this.images = images;
		
	}

	

	public void drawNode(Node node, Graphics g, int x, int y, int sizeX, int sizeY) {
		Image image = images.get(node.getAttributes().get(attribute));
		if(image != null) {
			g.drawImage(image, x, y, null);
		}
		
	}
			

	
}

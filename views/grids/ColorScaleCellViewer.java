package edu.upc.cnds.collectivesim.views.grids;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.upc.cnds.collectives.util.ValueScale;
import edu.upc.cnds.collectivesim.agents.Agent;

import uchicago.src.sim.gui.SimGraphics;

/**
 * A cell that can display an attribute of the agent in a color scale
 * 
 * @author Pablo Chacin
 *
 */
public class ColorScaleCellViewer extends CellViewer {

	//Color  Maps
	/**
	 * Default color map for tree level scales 
	 */
	public static Color[] SEMAPHORE_COLOR_MAP = {Color.GREEN,Color.YELLOW,Color.RED};
	
	/**
	 * Deafault thermal map (from cold to hot)
	 */
	public static Color[] THERMAL_COLOR_MAP = {Color.BLUE,Color.YELLOW,Color.ORANGE,Color.GREEN,Color.RED};
	
	
    /**
     * Default color WHITE
     */
    private static Color SOFT_WHITE_COLOR = new Color(250,250,250); 
    
    /**
     * Default color for non-occupied cells
     */
    private Color backgroundColor = SOFT_WHITE_COLOR;
   
    
    private ValueScale scale;
	private Color[] colorScale;

	/**
	 * Constructor
	 */
	public ColorScaleCellViewer (String attribute,ValueScale scale,Color[] colorScale){
		super();
		
		this.scale = scale;
		this.colorScale = colorScale;
		
        setAttribute(attribute);
        
	   //check arguments
	  if (colorScale.length < scale.getLevels()){
		 throw new IllegalArgumentException("Color Scale must cover all scale levels");
	  }
	}
	

	/**
     * sets the background color 
	 */
    public void setBackgroundColor(Color background){
        this.backgroundColor = background;
         
    }
    
	/**
	 * Draw the cell in the given dwawing space
	 */
	public void draw(SimGraphics g) {
		
        //if cell is non occupied, pait it of default color
        if(cell == null){
            g.drawFastRect(backgroundColor);
            return;
        }
       
		//get agent's current attribute value
		Double value = (Double)observeAttribute();
		
		//if scale is closed, check for values outside limits. 
		//Those are not drawn
		if(!scale.isInRange(value)){
			return;
		}
		
		//draw the cell as a rectangle of the color corresponding to
		//its current value
		g.drawFastRect(colorScale[scale.mapToScaleLevel(value.doubleValue())]);
	}

	
}

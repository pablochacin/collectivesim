package collectivesim.visualization.network.baseimp;

import java.awt.Color;
import java.awt.Graphics;

import collectivesim.util.ValueScale;
import collectivesim.visualization.network.AgentRenderingException;
import collectivesim.model.ModelAgent;
import collectivesim.model.ModelException;

/**
 * Displays an attribute of a Node in a color scale
 * 
 * @author Pablo Chacin
 *
 */
public class ColorScaleNodeDrawer extends AbstractAgentDrawer {

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
	public ColorScaleNodeDrawer (ModelAgent A, String attribute,ValueScale scale,Color[] colorScale){
		super(attribute);
		
		this.scale = scale;
		this.colorScale = colorScale;
		
       
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
    
    public Color getBackgroundColor() {
    	return this.backgroundColor;
    }



	@Override
	public void draw(ModelAgent agent, Graphics g, int x, int y, int sizeX, int sizeY) throws AgentRenderingException {
		try {
			//get agent's current attribute value
			Double value = (Double)agent.inquire(attribute);
		
			//if scale is closed, check for values outside limits. 
			//Those are not drawn
			if(!scale.isInRange(value)){
				return;
			}
			
			//draw the cell as a rectangle of the color corresponding to
			//its current value
			drawRectangle(g,x,y,sizeX,sizeY,colorScale[scale.mapToScaleLevel(value.doubleValue())]);
		} catch (ModelException e) {
			throw new AgentRenderingException("Exception inquiring attribute " + attribute +" for agent " + agent.getName(),e);
		}

	}



	
	
}

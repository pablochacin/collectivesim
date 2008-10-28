package edu.upc.cnds.collectivesim.views.grids;


import edu.upc.cnds.collectivesim.topology.grid2d.Cell;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

/**
 * A Cell that can be drawn in a 2D display space. 
 * 
 * It is responsible of  observing an attrubute of the agent(s) at a
 * given cell and return it's graphical representation
 * 
 * @author Pablo Chacin
 *
 */
public abstract class CellViewer implements Drawable {
	//coordinate space
    protected String attribute;
    protected Cell cell;
	
    
    /**
     * Sets the cell the viewer must observe
     */
    public void setCell(Cell cell){
        this.cell= cell ;
    }
    

    
	/**
	 * sets the agent's attribute this cell must observe
	 */
    
	public void setAttribute(String attribute) {

		this.attribute = attribute;

	}
   
    
	public int getX() {
		return this.cell.getX();
	}

	public int getY() {
		return this.cell.getY();
        }

	/**
	 * Observes one agent's attribute and records its value
     * 
     *  TODO: generalize to allow more than one agent per cell
     *  and the application of some operator on this list of agents
     *  to obtain a single attribute (for instance, the maximun,
     *  minimun, average, total, count)
	 */

	protected Object observeAttribute(){

		return cell.handelInquire(attribute);
	}
	

	/**
	 * draw cell
	 */
	public abstract void draw(SimGraphics g);
}

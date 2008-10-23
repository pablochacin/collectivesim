package edu.upc.cnds.collectivesim.views;

import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Displayable;

/**
 * A view that allows the display of a Realm
 * 
 * @author Pablo Chacin
 *
 */
public interface View {
	
	/**
	 * Dispose the view
	 */
	abstract public void dispose();

	/**
	 * Displays the view
	 */
	abstract public void display();
	
	/**
	 *  Returns the View's title to be displayed
	 */
	abstract public String getTitle();


}

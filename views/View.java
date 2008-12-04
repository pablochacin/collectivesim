package edu.upc.cnds.collectivesim.views;


/**
 * A view that allows the display of a Topology
 * 
 * @author Pablo Chacin
 *
 */
public interface View {
	
	/**
	 * Dispose the view. Release any graphical resource associated.
	 */
	public void dispose();

	/**
	 * Displays the view, without refreshing its content (used to handle
	 * window hiding/unhiding events
	 */
	public void display();
	
	
	/**
	 * Refresh the content of the view
	 *
	 */
	public void refresh();
	
	/**
	 *  Returns the View's title to be displayed
	 */
	public String getTitle();


}

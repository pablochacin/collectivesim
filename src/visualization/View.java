package collectivesim.visualization;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * A viewable Element
 * 
 * @author Pablo Chacin
 *
 */
public interface View {

	/**
	 * Repaints the content of the View.
	 */
	public void refresh();
	
	
	/**
	 * 
	 * @return a boolean indicating if the View needs to be refreshed. 
	 *         Used to optimize the refresh of multiple views.
	 */
	public boolean needRefresh();
	
	/**
	 * Clears the View' content
	 */
	public void clear();
	
	
	/**
	 * @returns a JPanel that displays the View's content
	 */
	public JPanel getViewableContent();


	/**
	 * @return the name of the view
	 */
	public String getName();

	
	/**
	 * 
	 * @return the width in pixels
	 */
	public int getWidth();

	/**
	 * 
	 * @return the height in pixels
	 */
	public int getHeight();
	
	/**
	 * Prints the contents of the view to a Graphics
	 * @param g graphic environment to print the content to
	 * 
	 */
	public void print(Graphics g);
}

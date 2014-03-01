package collectivesim.visualization.state;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import collectivesim.experiment.Experiment;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Displays The State of an Experiment 
 * 
 * @author Pablo Chacin
 *
 */
public class StateView extends JPanel{

		private Experiment experiment;
		
		/**
		 * Name of values to display. Empty means all values
		 */
		private String[] values;

	    public StateView(Experiment experiment,String[] values) {
	        super(new GridLayout(1,0));
	        
	        this.experiment = experiment;
	        this.values = values;

	        String[] columnNames = {"State Value","value"};

	        Object[][] data = {{},{}};

	        final JTable table = new JTable(data, columnNames);
	        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
	        table.setFillsViewportHeight(true);

	       
	        //Create the scroll pane and add the table to it.
	        JScrollPane scrollPane = new JScrollPane(table);

	        //Add the scroll pane to this panel.
	        add(scrollPane);
	    }

	    /**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event-dispatching thread.
	     */
	    private static void createAndShowGUI() {
	        //Create and set up the window.
	        JFrame frame = new JFrame("SimpleTableDemo");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        //Create and set up the content pane.
	        StateView newContentPane = new StateView();
	        newContentPane.setOpaque(true); //content panes must be opaque
	        frame.setContentPane(newContentPane);

	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	    }

	    public static void main(String[] args) {
	        //Schedule a job for the event-dispatching thread:
	        //creating and showing this application's GUI.
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI();
	            }
	        });
	    }


}

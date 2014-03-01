package collectivesim.visualization.state;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import collectivesim.experiment.Experiment;

import java.awt.Dimension;
import java.awt.GridLayout;

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

}

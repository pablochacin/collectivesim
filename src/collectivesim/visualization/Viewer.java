package collectivesim.visualization;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;


import collectivesim.experiment.Experiment;

/**
 * Displays one or more Views
 * 
 * @author Pablo Chacin
 *
 */
public class Viewer extends JFrame {
	
	private static Comparator<ViewPanel> VIEW_COMPARATOR = new Comparator<ViewPanel>() {

		@Override
		public int compare(ViewPanel v0, ViewPanel v1) {
			return v0.getName().compareTo(v1.getName());
		}
		
	};
	/**
	 * Takes a periodic snapshot. On each iteration, a new file is created on the experiment's 
	 * working directory under the "snapshots" subdirectory. 
	 * 
	 * Each snapshot has the name of the view plus a correlative number and the extension of the format used
	 * 
	 * If any snapshot fails, the task deactivates itself to avoid further errors.
	 */
	private class PeriodicSnapshot implements Runnable{
		
		private boolean active;
		
		private int counter;
		
		private ViewPanel panel;
		
		private EXPORT_FORMAT format;
		
		public PeriodicSnapshot(ViewPanel panel,EXPORT_FORMAT format) {
			this.panel = panel;
			this.format = format;
			counter = 0;
			active = true;
		}
		
		public void run(){
			
			if(!active){
				return;
			}
			
			counter++;
			
			String fileName = panel.getView().getName() + counter + "." + format.name().toLowerCase();
			

			File snapshotFile = new File(experiment.getWorkingDirectory(),fileName);

			active = panel.takeSnapshot(format, snapshotFile);
		}
	}
	
	
	public static enum EXPORT_FORMAT  {EPS,JPEG};
	
	protected SortedMap<String,ViewPanel>views;
	
	protected Experiment experiment;

	protected JDesktopPane pane;
	
	public Viewer(Experiment experiment){
		super(experiment.getDescription() + "running at " + experiment.getRootDirectory());
		this.experiment = experiment;
		this.views = new TreeMap<String, ViewPanel>();
		this.pane = new JDesktopPane();
		add(pane);
		
		JMenuBar mb = new JMenuBar();
	    JMenu menu = new JMenu("Views");

	    menu.add(new TileAction(pane)); // add tiling capability

	    setJMenuBar(mb);
	    mb.add(menu);
		 
		    
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 480);
		setVisible( false );
	
	}

	
	/**
	 * Adds a View and schedule its periodic refresh
	 * @param view
	 * @param delay
	 * @param frequency
	 */
	public void addView(final View view,long delay, long frequency){
		
		addView(view);
		
		experiment.addPeriodicTask(new Runnable(){
			public void run(){
				view.refresh();
			}
		}, delay, frequency);
	}
	
	/**
	 * Adds a view that auto-refreshes
	 * @param view
	 */
	public void addView(View view){
		ViewPanel panel = new ViewPanel(view);
		views.put(view.getName(), panel);
		pane.add(panel);
		setVisible( true );
	}
	
	public View getView(String name){
		ViewPanel panel = views.get(name);
		if(panel == null){
			throw new IllegalArgumentException("View " + name + " is not registered");
		}
		
		return panel.getView();
	}
	
	public void takeSnapshot(String view,EXPORT_FORMAT format,long delay,long frequency){
	
		ViewPanel panel = views.get(view);
		if(panel == null){
			throw new IllegalArgumentException("View " + view + " is not registered");
		}
		
		experiment.addPeriodicTask( new PeriodicSnapshot(panel,format), delay, frequency);
		
	}

	public void takeSnapshot(String view,EXPORT_FORMAT format){
		ViewPanel panel = views.get(view);
		
		if(panel == null){
			throw new IllegalArgumentException("View " + view + " is not registered");
		}
		
		File file = new File(experiment.getWorkingDirectory(),panel.getView().getName()+"."+format.toString().toLowerCase());
		
		panel.takeSnapshot(format, file );
	}
	
	/**
	 * 
	 * Tiles all the components in the pane
	 * 
	 * @author Pablo Chacin
	 *
	 */
	class TileAction extends AbstractAction {
		  private JDesktopPane desk; // the desktop to work with

		  public TileAction(JDesktopPane desk) {
		    super("Tile Frames");
		    this.desk = desk;
		  }

		  public void actionPerformed(ActionEvent ev) {

		    // How many frames do we have?
		    ViewPanel[] allframes = views.values().toArray(new ViewPanel[views.size()]);

		    
		    int count = allframes.length;
		    if (count == 0)
		      return;

		    // Determine the necessary grid size
		    int sqrt = (int) Math.sqrt(count);
		    int rows = sqrt;
		    int cols = sqrt;
		    if (rows * cols < count) {
		      cols++;
		      if (rows * cols < count) {
		        rows++;
		      }
		    }

		    // Define some initial values for size & location.
		    Dimension size = desk.getSize();

		    int w = size.width / cols;
		    int h = size.height / rows;
		    int x = 0;
		    int y = 0;

		    // Iterate over the frames, deiconifying any iconified frames and then
		    // relocating & resizing each.
		    for (int i = 0; i < rows; i++) {
		      for (int j = 0; j < cols && ((i * cols) + j < count); j++) {
		        JInternalFrame f = allframes[(i * cols) + j];

		        if (!f.isClosed() && f.isIcon()) {
		          try {
		            f.setIcon(false);
		          } catch (PropertyVetoException ignored) {
		          }
		        }

		        desk.getDesktopManager().resizeFrame(f, x, y, w, h);
		        x += w;
		      }
		      y += h; // start the next row
		      x = 0;
		    }
		  }
		}
}

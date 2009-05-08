package edu.upc.cnds.collectivesim.visualization;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


import edu.upc.cnds.collectivesim.experiment.Experiment;

/**
 * Displays one or more Views
 * 
 * @author Pablo Chacin
 *
 */
public class Viewer {
	
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
			
			File snapshotFile = new File(snapshotDir,fileName);

			active = panel.takeSnapshot(format, snapshotFile);
		}
	}
	
	
	public static enum EXPORT_FORMAT  {EPS,JPEG};
	
	protected Map<String,ViewPanel>views;
	
	protected Experiment experiment;

	protected File snapshotDir;
	
	public Viewer(Experiment experiment){
		this.experiment = experiment;
		this.views = new HashMap<String, ViewPanel>();
		this.snapshotDir = new File(experiment.getWorkingDirectory());

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
		
		File file = new File(snapshotDir,panel.getView().getName()+"."+format.toString().toLowerCase());
		
		panel.takeSnapshot(format, file );
	}
}

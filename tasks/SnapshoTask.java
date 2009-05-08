package edu.upc.cnds.collectivesim.tasks;

import edu.upc.cnds.collectivesim.visualization.View;
import edu.upc.cnds.collectivesim.visualization.Viewer;
import edu.upc.cnds.collectivesim.visualization.Viewer.EXPORT_FORMAT;

/**
 * Takes a snapshot for a list of views using a given format
 * 
 * @author Pablo Chacin
 */

public class SnapshoTask implements Runnable {

	private View[] views;
	
	private EXPORT_FORMAT format;

	private Viewer viewer;
	
	
	
	public SnapshoTask(Viewer viewer,String[] views, EXPORT_FORMAT format) {
		this.format = format;
		this.viewer = viewer;
		
		//get all the views from the viewer to check they exists
		this.views = new View[views.length];
		for(int i=0;i<views.length;i++){
			this.views[i] = viewer.getView(views[i]);
		}
	}



	@Override
	public void run() {

		for(View view: views){
			viewer.takeSnapshot(view.getName(), format);
		}
	}

}

package collectivesim.tasks;

import collectivesim.visualization.View;
import collectivesim.visualization.Viewer;
import collectivesim.visualization.Viewer.EXPORT_FORMAT;

/**
 * Takes a snapshot for a list of views using a given format
 * 
 * @author Pablo Chacin
 */

public class SnapshotTask implements Runnable {

	private View[] views;
	
	private EXPORT_FORMAT format;

	private Viewer viewer;
	
	
	
	public SnapshotTask(Viewer viewer,String[] views, EXPORT_FORMAT format) {
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

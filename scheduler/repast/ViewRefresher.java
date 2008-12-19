package edu.upc.cnds.collectivesim.scheduler.repast;

import edu.upc.cnds.collectivesim.views.View;

/**
 * Action used to refresh a View
 * 
 * @author Pablo Chacin
 *
 */
public class ViewRefresher implements Runnable {

	private View view;
	
	public ViewRefresher(View view) {
		this.view = view;
	}
	public void run() {

		view.refresh();
	}

}

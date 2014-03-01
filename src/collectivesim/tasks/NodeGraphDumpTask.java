package collectivesim.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import collectivesim.experiment.Experiment;
import collectivesim.visualization.Viewer;
import collectivesim.visualization.network.AgentView;

public class NodeGraphDumpTask implements Runnable {

	protected Experiment experiment;
	
	protected String nodeView;
	
	protected Viewer viewer;
	
	public NodeGraphDumpTask(Experiment experiment,Viewer viewer,String nodeView){
	
		this.experiment = experiment;
		this.viewer = viewer;
		this.nodeView = nodeView;
	}
	
	@Override
	public void run() {

		File graphFile = new File(experiment.getWorkingDirectory(),nodeView+".xml");
		
		OutputStream out;
		try {
			out = new FileOutputStream(graphFile);
			AgentView graph = (AgentView)viewer.getView(nodeView);
			graph.exportView(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}

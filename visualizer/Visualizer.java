package edu.upc.cnds.collectivesim.visualizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.picocontainer.Characteristics;

import edu.upc.cnds.collectives.application.AppLauncher;
import edu.upc.cnds.collectives.application.ApplicationConfigurationException;
import edu.upc.cnds.collectives.dataseries.DataSeries;
import edu.upc.cnds.collectives.dataseries.DataSet;
import edu.upc.cnds.collectives.dataseries.InvalidDataItemException;
import edu.upc.cnds.collectives.dataseries.baseImp.BaseDataSet;
import edu.upc.cnds.collectives.dataseries.functions.RunningAverage;
import edu.upc.cnds.collectives.dataseries.functions.Sum;
import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.events.EventCollector;
import edu.upc.cnds.collectives.events.EventObserver;
import edu.upc.cnds.collectives.events.imp.BasicEventCollector;
import edu.upc.cnds.collectives.execution.ExecutionService;
import edu.upc.cnds.collectives.execution.nativeImp.JavaExecutionService;
import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.identifier.Imp.DirectedCircularIdSpace;
import edu.upc.cnds.collectives.messaging.MessagingAddress;
import edu.upc.cnds.collectives.messaging.MessagingService;
import edu.upc.cnds.collectives.messaging.monitoring.EndPointCollector;
import edu.upc.cnds.collectives.messaging.monitoring.NodeObserver;
import edu.upc.cnds.collectives.messaging.owImp.OwMessagingService;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.topology.monitoring.TopologyEvent;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectives.visualizer.nodes.NodeVisualizerException;
import edu.upc.cnds.collectives.visualizer.nodes.NodeVisualizer;
import edu.upc.cnds.collectives.visualizer.nodes.jungImp.JungRenderer;
import edu.upc.cnds.collectives.visualizer.plots.Plot;
import edu.upc.cnds.collectives.visualizer.plots.jchart2dImp.JChart2dLinePlot;

/**
 * Interface to the OverlayWeaver's provides Visualizer used to display
 * messsaging EndPoints and communication Events.
 * 
 * @author Pablo Chacin
 *
 */
public class Visualizer implements Runnable,EventObserver {


	private final static Logger logger = Logger.getLogger("collectives.visualizer");



	/**
	 * Renderer of topology
	 */
	NodeVisualizer renderer;


	public Visualizer(NodeVisualizer renderer,List<UnderlayNode>nodes) {

		this.renderer = renderer;
		for(Node n: nodes){
			try {
				renderer.addNode(n, n.getId().toString());
			} catch (NodeVisualizerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			renderer.start();
		} catch (NodeVisualizerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void run() {
		//refresh representation of id Space
		renderer.refresh();
	
	}



	/**
	 * handle topology related events
	 */
	public void notify(Event event) {



		if(event.getType().equals(TopologyEvent.TOPOLOGY_JOIN)){
			try {
				Node target = (Node)(event.getData()[0]);

				renderer.connect(event.getNode(),target);
				logger.info("Connecting " + event.getNode().toString() + " with "+target);
			} catch (NodeVisualizerException e) {
				logger.warning("Exception connecting nodes: "+e.getMessage());
			}
			return;
		}

		if(event.getType().equals(TopologyEvent.TOPOLOGY_LEAVE)) {
			try {
				Node target = (Node)(event.getData()[0]);

				renderer.disconnect(event.getNode(),target);
				logger.info("Disconnecting " + event.getNode().toString() + " with "+target);
			} catch (NodeVisualizerException e) {
				logger.warning("Exception disconnecting nodes: "+e.getMessage());
			}
			return;
		}		
	
	}

}

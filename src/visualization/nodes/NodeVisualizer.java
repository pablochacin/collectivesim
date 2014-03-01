 package collectivesim.visualization.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import collectives.events.Event;
import collectives.events.EventObserver;
import collectives.node.Node;
import collectives.topology.TopologyEvent;
import collectives.underlay.UnderlayEvent;
import collectives.underlay.UnderlayNode;
import collectives.util.FormattingUtils;

/**
 * Handles node related events and displays them using a NodeView
 * 
 * @author Pablo Chacin
 *
 */
public class NodeVisualizer implements EventObserver {


	private final static Logger logger = Logger.getLogger("collectivesim.visualizer");



	/**
	 * View of topology
	 */
	NodeView nodeview;



	
	public NodeVisualizer(NodeView renderer){
		this(renderer,new ArrayList<UnderlayNode>());
	}
	
	public NodeVisualizer(NodeView view,List<UnderlayNode>nodes) {

		this.nodeview = view;
		for(Node n: nodes){
			try {
				this.nodeview.addNode(n);
			} catch (NodeRenderingException e) {
				logger.warning("Exception adding node "+ n.toString() + FormattingUtils.getStackTrace(e));
			}
		}
		

	}


	public void run() {
		//refresh representation of id Space
		nodeview.refresh();
	
	}



	/**
	 * handle topology related events
	 */
	public void notify(Event event) {


		if(event.getType().equals(UnderlayEvent.NODE_FOUND)){
			try {

				nodeview.addNode(event.getNode());
				
				if(logger.isLoggable(Level.FINEST))
					logger.finest("Adding node " + event.getNode().toString());
			} catch (NodeRenderingException e) {
				if(logger.isLoggable(Level.WARNING)) 
					logger.warning("Exception adding node: "+e.getMessage());
			}
			return;
		}

		if(event.getType().equals(TopologyEvent.TOPOLOGY_LINK)){
			try {
				Node target = (Node)(event.getData()[0]);

				nodeview.connect(event.getNode(),target);
				
				if(logger.isLoggable(Level.FINEST))
					logger.finest("Connecting " + event.getNode().toString() + " with "+target);
			} catch (NodeRenderingException e) {
				if(logger.isLoggable(Level.WARNING))
					logger.warning("Exception connecting nodes: "+e.getMessage());
			}
			return;
		}

		if(event.getType().equals(TopologyEvent.TOPOLOGY_UNLINK)) {
			try {
				Node target = (Node)(event.getData()[0]);

				nodeview.disconnect(event.getNode(),target);

				if(logger.isLoggable(Level.FINEST))
					logger.finest("Disconnecting " + event.getNode().toString() + " with "+target);
			} catch (NodeRenderingException e) {
				if(logger.isLoggable(Level.WARNING))
					logger.warning("Exception disconnecting nodes: "+e.getMessage());
			}
			return;
		}		
	
	}



}

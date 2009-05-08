 package edu.upc.cnds.collectivesim.visualization.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.events.EventObserver;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.topology.TopologyEvent;
import edu.upc.cnds.collectives.underlay.UnderlayEvent;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectives.util.FormattingUtils;

/**
 * Handles node related events and displays them using a NodeView
 * 
 * @author Pablo Chacin
 *
 */
public class NodeVisualizer implements EventObserver {


	private final static Logger logger = Logger.getLogger("collectives.visualizer");



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

		if(event.getType().equals(TopologyEvent.TOPOLOGY_JOIN)){
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

		if(event.getType().equals(TopologyEvent.TOPOLOGY_LEAVE)) {
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

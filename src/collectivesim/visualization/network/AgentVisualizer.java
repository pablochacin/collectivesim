 package collectivesim.visualization.network;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import collectivesim.events.Event;
import collectivesim.events.EventObserver;
import collectivesim.model.ModelAgent;
import collectivesim.network.NetworkEvent;
import collectivesim.util.FormattingUtils;

/**
 * Handles agent topology related events and displays them using a AgentView
 * 
 * @author Pablo Chacin
 *
 */
public class AgentVisualizer implements EventObserver {
	
	private final static Logger logger = Logger.getLogger("collectivesim.visualizer");



	/**
	 * View of topology
	 */
	AgentView agentView;



	
	public AgentVisualizer(AgentView view){
		this(view,new ArrayList<ModelAgent>());
	}
	
	public AgentVisualizer(AgentView view,List<ModelAgent>nodes) {

		this.agentView = view;
		for(ModelAgent a: nodes){
			try {
				this.agentView.add(a);
			} catch (AgentRenderingException e) {
				logger.warning("Exception adding agent "+ a.toString() + FormattingUtils.getStackTrace(e));
			}
		}
		

	}


	public void run() {
		//refresh representation of id Space
		agentView.refresh();
	
	}



	/**
	 * handle network related events
	 */
	public void notify(Event event) {


		if(event.getType().equals(NetworkEvent.NETWORK_JOIN)){
			try {

				agentView.add(event.getAgent());
				
				if(logger.isLoggable(Level.FINEST))
					logger.finest("Adding node " + event.getAgent().toString());
			} catch (AgentRenderingException e) {
				if(logger.isLoggable(Level.WARNING)) 
					logger.warning("Exception adding node: "+e.getMessage());
			}
			return;
		}

		if(event.getType().equals(NetworkEvent.NETWORK_LINK)){
			try {
				String targetName = (String) event.getAttributes().get("target");
				ModelAgent target = (ModelAgent)event.getModel().getAgent(targetName);

				agentView.connect(event.getAgent(),target);
				
				if(logger.isLoggable(Level.FINEST))
					logger.finest("Connecting " + event.getAgent().getName()+ " with "+target);
			} catch (AgentRenderingException e) {
				if(logger.isLoggable(Level.WARNING))
					logger.warning("Exception connecting nodes: "+e.getMessage());
			}
			return;
		}

		if(event.getType().equals(NetworkEvent.NETWORK_UNLINK)) {
			try {

				String targetName = (String) event.getAttributes().get("target");
				ModelAgent target = (ModelAgent)event.getModel().getAgent(targetName);

				agentView.disconnect(event.getAgent(),target);

				if(logger.isLoggable(Level.FINEST))
					logger.finest("Disconnecting " + event.getAgent().getName()+ " with "+target);
			} catch (AgentRenderingException e) {
				if(logger.isLoggable(Level.WARNING))
					logger.warning("Exception disconnecting nodes: "+e.getMessage());
			}
			return;
		}		
	
	}



}

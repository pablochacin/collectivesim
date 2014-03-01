package collectivesim.visualization.network;

import java.io.OutputStream;

import collectivesim.model.ModelAgent;
import collectivesim.visualization.View;



/**
 * Renders the elements defined by the visualizer
 * 
 * @author Pablo Chacin
 *
 */
public interface AgentView extends View {

	
	/**
	 * Sets the AgentRenderer used to draw each agent
	 * 
	 * @param drawer a agentDrawer to draw agents
	 */
	public void setAgentRenderer(AgentRenderer drawer);
	
	
	
	/**
	 * Display an agent with the given Identifier and label
	 * @param agent
	 * @param label
	 * @throws AgentRenderingException if a agent with the same id already exists
	 */
	public void add(ModelAgent agent) throws AgentRenderingException;  
	
	
	
	/**
	 * Remove a agent with the given Identifier
	 * @param agent
	 * @param label
	 * @throws AgentRenderingException if no agent with the id exists
	 */
	public void remove(ModelAgent agent) throws AgentRenderingException;
	
	/**
	 * Draw a connection between two agents, given their Identifiers
	 * @param src source the agent
	 * @param target the target agent
	 * @throws AgentRenderingException if any of the agents doesn't exists or they are 
	 *                         already connected
	 */
	public void connect(ModelAgent src,ModelAgent target) throws AgentRenderingException;
		
	/**
	 * Removes a connection between the agents 
	 *  
	 * @param src the source agent
	 * @param target the target agent
	 * @throws AgentRenderingException if any of the agents doesn't exists or they are nor 
	 *                         currently connected
	 */
	public void disconnect(ModelAgent src,ModelAgent target) throws AgentRenderingException;
	
	
	/**
	 * Sets the auto-refresh mode on/off
	 * @param autoUpdate
	 * @return the current value. Must be checked because not all renders 
	 *         support setting the auto-refresh on/off.
	 */
	public boolean setAutoUpdate(boolean autoUpdate);
	
	
	/**
	 * Add a tag to the given identifier 
	 * @param id
	 */
	public void tag(ModelAgent agent,String tag) throws AgentRenderingException;
	
	/**
	 * Exports the view as a GraphML file
	 * @param out
	 */
	public void exportView(OutputStream out);
}

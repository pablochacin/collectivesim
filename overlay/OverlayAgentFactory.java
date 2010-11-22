package edu.upc.cnds.collectivesim.overlay;

import java.util.Map;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayException;
import edu.upc.cnds.collectives.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.model.AgentFactory;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelException;

/**
 * Offers the basic functionality to implement OverlayAgent factories. Uses two provided 
 * factories to create new Agents:
 * <ul>
 * <li> an Underlay to create UnderlayNodes 
 * <li> an OverlayFactory to create the Overlay
 * </ul> 
 * 
 *  Subclasses can override the {@link #createOverlayAgent(Overlay, Map)} to instantiate
 *  specific subclasses of agents
 * 
 * @author Pablo Chacin
 *
 */
public class OverlayAgentFactory implements AgentFactory<Model<? extends OverlayAgent>,OverlayAgent> {

	protected OverlayFactory factory;
			
	/**
	 * Constructor 
	 * 
	 * @param factory
	 * @param underlay
	 */
	public OverlayAgentFactory(OverlayFactory factory) {
		this.factory = factory;
	}


	@Override
	public OverlayAgent createAgent(Model<? extends OverlayAgent> model) throws ModelException {
		
		Overlay overlay;
		try {
			overlay = factory.getOverlay();
	
		
			OverlayAgent agent = createOverlayAgent(overlay);
			overlay.setHandler(agent);
			
			return agent;
		}catch (OverlayException e) {
			throw new ModelException("Exception creating overlay node",e);
		}
	}
	

	/**
	 * Instantiates an OverlayAgent. Subclasses can override to instantiate specific subclasses
	 * of OverlayAgent.
	 * 
	 * @param model
	 * @param overlay
	 * @param args
	 * @return
	 */
	
	protected OverlayAgent createOverlayAgent(Overlay overlay){
	
		return new OverlayAgent(overlay);
	}



}

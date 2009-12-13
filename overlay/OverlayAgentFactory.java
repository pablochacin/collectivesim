package edu.upc.cnds.collectivesim.overlay;

import java.util.Map;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayException;
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

	OverlayFactory factory;
	
	Underlay underlay;
	
	
	
	/**
	 * Constructor 
	 * 
	 * @param factory
	 * @param underlay
	 */
	public OverlayAgentFactory(OverlayFactory factory, Underlay underlay) {
		super();
		this.factory = factory;
		this.underlay = underlay;
	}


	@Override
	public OverlayAgent createAgent(Model<? extends OverlayAgent> model,Map<String, Object> args) throws ModelException {
		Identifier id = (Identifier)args.get("id");
		
		Overlay overlay;
		try {
			overlay = factory.getOverlay(underlay.createNode(id));
	
		
			OverlayAgent agent = createOverlayAgent((OverlayModel)model,overlay,args);
			
			return agent;
		} catch (UnderlayException e) {
			throw new ModelException("Exception creating underlay node",e);
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
	protected OverlayAgent createOverlayAgent(OverlayModel model,Overlay overlay,Map<String,Object> args){
	
		return new OverlayAgent(model,overlay,args);
	}



}

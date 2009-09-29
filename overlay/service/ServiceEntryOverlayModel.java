package edu.upc.cnds.collectivesim.overlay.service;

import java.util.Map;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.service.ServiceOverlayAgent;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityOverlayModel;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * 
 * Factory for GradientTopologyAgents that maintains a gradient topology.
 * 
 * @author Pablo Chacin
 *
 */
public class ServiceEntryOverlayModel extends UtilityOverlayModel{
					
	
	/**
	 * Constructor
	 * @param name name of the model
	 * @param experiment Experiment on which this model resides
	 * @param underlay UnderlayModel that gives access to underlay nodes
	 * @param gradientTopologySize maximum size of the gradient Topology
	 * @param randomTopologySize maximum size of the random topology
	 */
	public ServiceEntryOverlayModel(String name,Experiment experiment,
									UnderlayModel underlay,  
									OverlayFactory factory,
									Stream ... attributes) {
		
		super(name,experiment,underlay,factory,attributes);
	}



	/**
	 * Creates an OverlayAgent from the overlay components
	 * 
	 * @param overlay
	 * @param router
	 * @param randomTopology
	 * @return
	 * @throws ModelException 
	 */
	protected OverlayAgent createAgent(Overlay overlay,Map attributes) throws ModelException{
		
		Node localNode = overlay.getLocalNode();
		
		Double utility  = (Double)localNode.getAttributes().get("utility");
		String role 	= (String)localNode.getAttributes().get("role");	
		
		if(role.equals("entry")){
			Double preference = (Double)localNode.getAttributes().get("preference");
			return new ServiceEntryAgent(this,overlay, utility,role,preference);
		}

		return new ServiceOverlayAgent(this,overlay,utility,role);
		
	}


	@Override
	protected void terminate() {
		// Do nothing
		
	}


}

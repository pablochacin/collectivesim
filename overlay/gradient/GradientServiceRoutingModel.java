package edu.upc.cnds.collectivesim.overlay.gradient;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * 
 * Factory for GradientTopologyAgents that maintains a gradient topology.
 * 
 * @author Pablo Chacin
 *
 */
public class GradientServiceRoutingModel extends GradientOverlayModel{
			
	
	protected Integer ttl;
	
	protected Double tolerance;
	

	
	/**
	 * Constructor
	 * @param name name of the model
	 * @param experiment Experiment on which this model resides
	 * @param underlay UnderlayModel that gives access to underlay nodes
	 * @param gradientTopologySize maximum size of the gradient Topology
	 * @param randomTopologySize maximum size of the random topology
	 */
	public GradientServiceRoutingModel(String name,Experiment experiment,UnderlayModel underlay, 
			                    Integer viewSize,Integer exchangeSet,long cycleLength,Double alpha,
			                    Integer randomViewSize,Integer randomExchangeSet,Double tolerance,Integer ttl) {
		
		super(name,experiment,underlay,viewSize,exchangeSet,cycleLength,alpha,randomViewSize,randomExchangeSet);
		this.tolerance = tolerance;
		this.ttl = ttl;
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
	protected OverlayAgent createAgent(Overlay overlay,Routing router,Topology randomTopology,Double utility) throws ModelException{
		
		String role = (String)overlay.getLocalNode().getAttributes().get("role");
		
		
		if(overlay.getLocalNode().getId().toString().equals("0000000000000000")){
			role = "consumer";
			overlay.getLocalNode().getAttributes().put("role","consumer");
		}
		
		if(role.equals("provider"))
			return new GradientOverlayServiceAgent(this,overlay, router, randomTopology, utility, role);
		
		if(role.equals("consumer"))
		   return new GradientOverlayConsumerAgent(this,overlay,router,randomTopology,utility,role,tolerance,ttl);
		
		throw new ModelException("Unknown role " + role);
		
	}


	@Override
	protected void terminate() {
		// Do nothing
		
	}


}

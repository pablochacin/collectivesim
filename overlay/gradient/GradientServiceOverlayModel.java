package edu.upc.cnds.collectivesim.overlay.gradient;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * 
 * Factory for GradientTopologyAgents that maintains a gradient topology.
 * 
 * @author Pablo Chacin
 *
 */
public class GradientServiceOverlayModel extends GradientOverlayModel{
			
	
	protected Integer ttl;
	
	protected Double tolerance;
	
	protected UtilityFunction function;

	
	/**
	 * Constructor
	 * @param name name of the model
	 * @param experiment Experiment on which this model resides
	 * @param underlay UnderlayModel that gives access to underlay nodes
	 * @param gradientTopologySize maximum size of the gradient Topology
	 * @param randomTopologySize maximum size of the random topology
	 */
	public GradientServiceOverlayModel(String name,Experiment experiment,UnderlayModel underlay, 
			                    Integer viewSize,Integer exchangeSet,long cycleLength,Double alpha,
			                    Integer randomViewSize,Integer randomExchangeSet,Double tolerance,Integer ttl,
			                    UtilityFunction function) {
		
		super(name,experiment,underlay,viewSize,exchangeSet,cycleLength,alpha,randomViewSize,randomExchangeSet);
		this.tolerance = tolerance;
		this.ttl = ttl;
		this.function= function;
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
			return new GradientOverlayProviderAgent(this,overlay, router, randomTopology,utility,role,function);
		
		if(role.equals("consumer"))
		   return new GradientOverlayConsumerAgent(this,overlay,router,randomTopology,utility,role,tolerance,ttl);
		
		throw new ModelException("Unknown role " + role);
		
	}


	@Override
	protected void terminate() {
		// Do nothing
		
	}


}

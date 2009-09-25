package edu.upc.cnds.collectivesim.overlay.service;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityOverlayModel;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * 
 * Factory of agents that are peers in a service overlay (can make and process requests) 
 *  
 * @author Pablo Chacin
 *
 */
public class P2PServiceOverlayModel extends UtilityOverlayModel{
			
	
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
	public P2PServiceOverlayModel(String name,Experiment experiment,UnderlayModel underlay, 
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
				
		
		 Double preference = (Double)overlay.getLocalNode().getAttributes().get("preference");
			
		 return new ServiceConsumerAgent(this,overlay,router,randomTopology,utility,"peer",tolerance,ttl,tolerance);
				
	}


	@Override
	protected void terminate() {
		// Do nothing
		
	}


}

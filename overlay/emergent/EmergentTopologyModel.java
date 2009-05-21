package edu.upc.cnds.collectivesim.overlay.emergent;

import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.node.base.RandomSelector;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.ordered.OrderedTopologyModel;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;



public class EmergentTopologyModel extends OrderedTopologyModel {

     protected String adaptationAttribute;
     protected int adaptationViewSize;

	
	public EmergentTopologyModel(String name, Experiment experiment,UnderlayModel underlay, 
			                     int viewSize, IdSpace space, String adaptationAttribute,int adaptationViewSize) {
		super(name, experiment, underlay, viewSize, space);
		this.adaptationAttribute = adaptationAttribute;
		this.adaptationViewSize = adaptationViewSize;
	}

	
	@Override
	public OverlayAgent createAgent(Topology topology) {

		OverlayAgent agent = new EmergentTopologyAgent(this,topology,new RandomSelector(),viewSize,
				                                        adaptationAttribute,adaptationViewSize);
		
		return agent;
	}

}

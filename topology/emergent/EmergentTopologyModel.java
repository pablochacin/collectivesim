package edu.upc.cnds.collectivesim.topology.emergent;

import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.node.RandomSelector;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.topology.TopologyAgent;
import edu.upc.cnds.collectivesim.topology.ordered.OrderedTopologyModel;
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
	public TopologyAgent createAgent(Topology topology) {

		TopologyAgent agent = new EmergentTopologyAgent(this,topology,new RandomSelector(),viewSize,
				                                        adaptationAttribute,adaptationViewSize);
		
		return agent;
	}

}

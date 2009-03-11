package edu.upc.cnds.collectivesim.topology.emergent;

import java.util.List;

import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeAttributeComparator;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.OrderedSelector;
import edu.upc.cnds.collectives.node.RandomSelector;
import edu.upc.cnds.collectives.topology.BasicTopology;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.topology.TopologyAgent;
import edu.upc.cnds.collectivesim.topology.TopologyModel;
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

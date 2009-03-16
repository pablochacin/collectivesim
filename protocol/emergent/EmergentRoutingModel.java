package edu.upc.cnds.collectivesim.protocol.emergent;

import edu.upc.cnds.collectives.routing.GreedyRouting;
import edu.upc.cnds.collectives.routing.MatchFunction;
import edu.upc.cnds.collectives.routing.RoutingAlgorithm;
import edu.upc.cnds.collectives.routing.RoutingProtocol;
import edu.upc.cnds.collectives.routing.emergent.EmergingKbrProtocolImp;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.transport.Transport;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.protocol.ProtocolModelAgent;
import edu.upc.cnds.collectivesim.protocol.kbr.KbrProtocolAgent;
import edu.upc.cnds.collectivesim.protocol.kbr.KbrProtocolModel;
import edu.upc.cnds.collectivesim.topology.TopologyModel;
import edu.upc.cnds.collectivesim.transport.TransportModel;

public class EmergentRoutingModel extends KbrProtocolModel {

	/**
	 * Desired routing convergence rate
	 */
	protected Double convergence;
	
	public EmergentRoutingModel(String name, Experiment experiment,
			TopologyModel topology, MatchFunction function,Double convergence,
			TransportModel transport, DataSeries hops) {
		super(name, experiment, topology, function, transport, hops);
		
		this.convergence = convergence;
		
		
	}
	
	@Override
	public ProtocolModelAgent installProtocol(String name,Topology topology, Transport transport) {
		
		RoutingAlgorithm algorithm = new GreedyRouting(topology, function);
		
		RoutingProtocol protocol = new EmergingKbrProtocolImp(name,topology,function,algorithm,convergence,transport);
				
		return new KbrProtocolAgent(topology.getLocalNode().getId().toString(),protocol,this);
		
	}

	
	
}

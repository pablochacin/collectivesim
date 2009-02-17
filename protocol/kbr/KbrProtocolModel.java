package edu.upc.cnds.collectivesim.protocol.kbr;

import edu.upc.cnds.collectives.protocol.MatchFunction;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.routing.GreedyRouting;
import edu.upc.cnds.collectives.routing.RoutingAlgorithm;
import edu.upc.cnds.collectives.routing.kbr.KbrProtocolImp;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.transport.Transport;
import edu.upc.cnds.collectivesim.protocol.ProtocolModel;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.topology.TopologyModel;
import edu.upc.cnds.collectivesim.transport.TransportModel;

public class KbrProtocolModel extends ProtocolModel {
	
	protected MatchFunction function;

	public KbrProtocolModel(String name, Scheduler scheduler,TopologyModel topology, MatchFunction function,TransportModel transport) {
		super(name, scheduler, topology, transport);
		this.function = function;

	}
	
	@Override
	public Protocol installProtocol(String name,Topology topology, Transport transport) {
		
		RoutingAlgorithm algorithm = new GreedyRouting(topology, function);
		
		Protocol protocol = new KbrProtocolImp(name,topology,function,algorithm,transport);
		
		return protocol;
	}

}

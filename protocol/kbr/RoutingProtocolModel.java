package edu.upc.cnds.collectivesim.protocol.kbr;

import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.transport.Transport;
import edu.upc.cnds.collectivesim.protocol.ProtocolModel;
import edu.upc.cnds.collectivesim.protocol.ProtocolModelAgent;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.topology.TopologyModel;
import edu.upc.cnds.collectivesim.transport.TransportModel;

public class RoutingProtocolModel extends ProtocolModel {

	public RoutingProtocolModel(String name, Scheduler scheduler,
			TopologyModel topology, TransportModel transport) {
		super(name, scheduler, topology, transport);
	}

	@Override
	public ProtocolModelAgent installProtocol(String name, Topology topology,
			Transport transport) {
		throw new UnsupportedOperationException();
	}

}

package edu.upc.cnds.collectivesim.protocol.kbr;

import edu.upc.cnds.collectives.dataseries.DataSeries;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.protocol.Destination;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.routing.GreedyRouting;
import edu.upc.cnds.collectives.routing.MatchFunction;
import edu.upc.cnds.collectives.routing.Route;
import edu.upc.cnds.collectives.routing.RoutingAlgorithm;
import edu.upc.cnds.collectives.routing.kbr.KbrProtocol;
import edu.upc.cnds.collectives.routing.kbr.KbrProtocolImp;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.transport.Transport;
import edu.upc.cnds.collectivesim.protocol.ProtocolModel;
import edu.upc.cnds.collectivesim.protocol.ProtocolModelAgent;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.topology.TopologyModel;
import edu.upc.cnds.collectivesim.transport.TransportModel;

/**
 * Implements a model for routing protocols. 
 * 
 * @author Pablo Chacin
 *
 */
public class KbrProtocolModel extends ProtocolModel {
	
	protected MatchFunction function;
	
	private DataSeries hops;

	public KbrProtocolModel(String name, Scheduler scheduler,TopologyModel topology, MatchFunction function,TransportModel transport) {
		super(name, scheduler, topology, transport);
		this.function = function;

	}
	
	@Override
	public ProtocolModelAgent installProtocol(String name,Topology topology, Transport transport) {
		
		RoutingAlgorithm algorithm = new GreedyRouting(topology, function);
		
		KbrProtocol protocol = new KbrProtocolImp(name,topology,function,algorithm,transport);
				
		return new KbrProtocolAgent(protocol,this);
	}
	
	/**
	 * Reports the delivery of a request on a node. The route includes the origin and 
	 * final target.
	 * 
	 * @param node
	 * @param route
	 * @param destination
	 */
	void reportDeliverey(Node node, Route route,Destination destination){
		
	}

}

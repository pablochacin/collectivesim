package edu.upc.cnds.collectivesim.protocol.kbr;



import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.protocol.Destination;
import edu.upc.cnds.collectives.routing.GreedyRouting;
import edu.upc.cnds.collectives.routing.MatchFunction;
import edu.upc.cnds.collectives.routing.Route;
import edu.upc.cnds.collectives.routing.RoutingAlgorithm;
import edu.upc.cnds.collectives.routing.RoutingProtocol;
import edu.upc.cnds.collectives.routing.kbr.KbrProtocolImp;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.transport.Transport;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.ModelObserver;
import edu.upc.cnds.collectivesim.protocol.ProtocolModel;
import edu.upc.cnds.collectivesim.protocol.ProtocolModelAgent;
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
	
	protected DataSeries hops;
	
	public KbrProtocolModel(String name, Experiment experiment,TopologyModel topology, 
			                MatchFunction function,TransportModel transport,DataSeries hops) {
		super(name, experiment, topology, transport);
		this.function = function;
		this.hops = hops;
	}
	
	
	@Override
	public ProtocolModelAgent installProtocol(String name,Topology topology, Transport transport) {
		
		RoutingAlgorithm algorithm = new GreedyRouting(topology, function);
		
		RoutingProtocol protocol = new KbrProtocolImp(name,topology,function,algorithm,transport);

		//the routing protocol requires the node id as an attribute of the node
		topology.getLocalNode().getAttributes().put("key",topology.getLocalNode().getId());
		
		return new KbrProtocolAgent(topology.getLocalNode().getId().toString(),protocol,this);
	}
	
	/**
	 * Reports the delivery of a request on a node. The route includes the origin and 
	 * final target.
	 * 
	 * @param node
	 * @param route
	 * @param destination
	 */
	void reportDelivered(Node node, String protocol,Destination destination,Route route){

		Map attributes = new HashMap();
		attributes.put("source", route.getRoute().get(0).toString());
		attributes.put("target",node.getId().toString());
		attributes.put("hops",String.valueOf(route.getHops()));
		attributes.put("protocol",protocol);
		for(Map.Entry< String, Object> e: destination.getAttributes().entrySet()){
			attributes.put("destination."+e.getKey().toString(), 
					     e.getValue().toString());
		}
		
		Event event = new RoutingModelEvent(node,experiment.getScheduler().getTime(),
														attributes,protocol,destination,route);
		
		experiment.reportEvent(event);
		
		hops.addItem(attributes, new Double(route.getHops()));
	}




	@Override
	protected void terminate() {
		// Do nothing
		
	}



}

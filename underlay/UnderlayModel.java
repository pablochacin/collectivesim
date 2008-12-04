package edu.upc.cnds.collectivesim.underlay;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.platform.Platform;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.protocol.Transport;
import edu.upc.cnds.collectives.underlay.Metric;
import edu.upc.cnds.collectives.underlay.MetricType;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectivesim.models.SimulationModel;
import edu.upc.cnds.collectivesim.plarform.PlatformModel;
import edu.upc.cnds.collectivesim.protocol.ProtocolModel;
import edu.upc.cnds.collectivesim.protocol.TransportModel;
/**
 * Simulates an underlay
 * 
 * @author Pablo Chacin
 *
 */
public class UnderlayModel implements Underlay {

	private Map<Identifier,UnderlayModelNode>nodes;
	
	private Map<String,TransportModel>transports;
	
	private Map<String,ProtocolModel>protocols;
	
	private SimulationModel model;
	
	private PlatformModel platform;
	
	public UnderlayModel(SimulationModel model,PlatformModel platform) {
		this.model = model;
		this.platform = platform;
		this.nodes = new HashMap<Identifier,UnderlayModelNode>();
		this.protocols = new HashMap<String, ProtocolModel>();
		this.transports = new HashMap<String,TransportModel>();
	}
	
	
	public Node createNode(Identifier id) {
		UnderlayModelNodeAddress address = new UnderlayModelNodeAddress(id,this);
		UnderlayModelNode node = new UnderlayModelNode(id,address,this);
		nodes.put(id,node);
		return node;
	}

	public Set<MetricType> getSupportedMetrics() {
		throw new UnsupportedOperationException();
	}

	public Metric[] probe(Node node, Set<MetricType> metrics) {
		throw new UnsupportedOperationException();
	}
	
	
	Platform getPlatform() {
		return platform;
	}
	
	
	UnderlayModelNode getNode(UnderlayModelNodeAddress address){
		return nodes.get(address);
	}


	/**
	 * Returns a ProtocolModel that simulates a protocol for a given Node
	 *  
	 * @param name name of the protocol
	 * @param node
	 * @return
	 */
	public Protocol getProtocol(String name, UnderlayModelNode node) {
		ProtocolModel protocol = protocols.get(name);
		return protocol.getProtocol(node);
	}

	/**
	 * Return a Transport for a given Node from the corresponding TransportModel.
	 * 
	 * @param name
	 * @param node
	 * @return
	 */
	public Transport getTransport(String name, UnderlayModelNode node) {
		TransportModel transport = transports.get(name);
		return transport.getTransport(node);
	}
	
	
	
	

}

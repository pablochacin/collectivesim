package edu.upc.cnds.collectivesim.transport;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.transport.Transport;

public class TransportModelProxy implements Transport {

	private TransportModel model;
	
	private Node localNode;
	
	
	
	TransportModelProxy(TransportModel model, Node localNode) {
		super();
		this.model = model;
		this.localNode = localNode;
	}


	public Protocol getProxy(Protocol protocol, Node node) {
		return model.getProtocolProxy(localNode,protocol,node);
	}



}

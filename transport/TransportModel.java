package edu.upc.cnds.collectivesim.transport;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.protocol.Transport;


public interface TransportModel {
	
	/**
	 * 
	 * @return
	 */
	public Transport getTransportModelProxy(Node node);

	
	/**
	 * Gets a proxy for the source node of a protocol in a target node
	 * @param source
	 * @param protocol
	 * @param target
	 * @return
	 */
	public Protocol getProtocolProxy(Node source,Protocol protocol, Node target);
	
}

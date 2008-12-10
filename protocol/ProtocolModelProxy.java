package edu.upc.cnds.collectivesim.protocol;

import java.io.Serializable;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.platform.Platform;
import edu.upc.cnds.collectives.protocol.Destination;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.protocol.ProtocolObserver;
import edu.upc.cnds.collectives.protocol.Transport;
import edu.upc.cnds.collectives.protocol.baseImp.AbstracProtocol;

/**
 * Serves as a proxy between a node and the ProtocolModel. It fordwards propagate
 * requests to the ProtocolModel. At each node, it handles the propagation messages
 * and delivers them to the node.
 * 
 * @author Pablo Chacin
 *
 */
public class ProtocolModelProxy extends AbstracProtocol {
	
	/**
	 * Node this proxy serves
	 */
	private Node node;
	
	private ProtocolModel protocol;
	
	public ProtocolModelProxy(ProtocolModel protocol,Node node) {
		super(protocol.getName());
		this.protocol = protocol;
		this.node = node;
	}



	public void propagate(Destination destination, Node[] hint, Serializable... args) {
		protocol.propagate(node, destination, hint, args);
		
	}

	@Override
	public void propagate(Destination destination, Serializable... args) {
		protocol.propagate(node, destination, args);
		
	}
	
	
	/**
	 * Method used by the ProtocolModel to deliver to a Node
	 * 
	 * @param source
	 * @param destination
	 * @param args
	 */
	public void handleDeliver(Node source,Destination destination,Serializable...args) {
		
		//call the superclass to deliver to observer(s)
		deliver(destination,source,args);
	}
	
}
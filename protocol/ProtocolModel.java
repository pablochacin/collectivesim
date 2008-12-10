package edu.upc.cnds.collectivesim.protocol;

import java.io.Serializable;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.protocol.Destination;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.protocol.ProtocolObserver;
import edu.upc.cnds.collectives.protocol.Transport;

/**
 * A simulation model of a Protocol. It handles the request from all nodes
 * and simulates the protocol's propagation rules using a simulated transport.
 * 
 * The ProtocolModel is called from the UnderlayNodeModel passing the node that
 * made the request. 
 * 
 * Tipically, the ProtocolModel will use the TopologyModel to obtain information about the
 * topology that communicates the nodes (for example, to know a node's neighbors). 
 * 
 * @author Pablo Chacin
 *
 */
public interface ProtocolModel  {
	
	/**
	 * Propagate the given message to a destination described as a set of attributes. 
	 * 
	 * @param args a series of values to be propagated
	 */
	public void propagate(Node source,Destination destination, Serializable ... args);
	

	/**
	 * Propagate the given message to a destination described as a set of attributes 
	 * using an application supplied hint of the nodes to consider to propagate.
	 * The protocols can ingnore this attribute. 
	 * 
	 */
	public void propagate(Node sorce,Destination destination, Node[] hint,Serializable ... args);

	
	
	/**
	 * @return the name of this protocol.
	 */
	public String getName();
	
	
	/**
	 * 
	 * @param node
	 * 
	 * @return a ProtocolProxy for the given node
	 */
	public Protocol getProtocolModelProxy(Node node);
	

}

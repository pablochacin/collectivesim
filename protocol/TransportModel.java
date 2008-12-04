package edu.upc.cnds.collectivesim.protocol;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.protocol.Transport;

/**
 * A simulation model of a Transport
 * 
 * @author Pablo Chacin
 *
 */
public interface TransportModel  {
	
	/**
	 * Generates a transport for the given Node
	 * 
	 * @param node
	 * 
	 * @return
	 */
	Transport getTransport(Node node);

}

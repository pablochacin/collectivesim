package edu.upc.cnds.collectivesim.protocol;

import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectivesim.underlay.UnderlayModelNode;

/**
 * A simulation model of a Protocol. 
 * 
 * @author Pablo Chacin
 *
 */
public interface ProtocolModel {

	public Protocol getProtocol(UnderlayModelNode node);

}

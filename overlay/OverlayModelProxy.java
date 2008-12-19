
package edu.upc.cnds.collectivesim.overlay;

import java.util.Map;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayException;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.transport.Transport;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.protocol.ProtocolModel;
import edu.upc.cnds.collectivesim.topology.TopologyModel;
import edu.upc.cnds.collectivesim.transport.TransportModel;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * Simulates a node of the overlay
 * 
 * @author Pablo Chacin
 *
 */

public class OverlayModelProxy implements Overlay {

	private UnderlayNode localNode;
	
	private UnderlayModel underlay;
	
	public UnderlayNode getLocalNode() {
		return localNode;
	}

	public Protocol getProtocol(String name)  throws OverlayException {
		overlay.getProtocol(localNode,name);
	}

	public Topology getTopology() {
		return topology.getTopologyModelProxy(localNode);
	}

	public Transport getTransport(String name) throws OverlayException{
		TransportModel model = transports.get(name);
		if(model == null) {
			throw new OverlayException("Unknown transport "+name);
		}
		
		return model.getTransportModelProxy(localNode);
	}

	public Underlay getUnderlay() {
		return underlay.getUnderlayModelProxy(localNode.getAddress());
	}



}

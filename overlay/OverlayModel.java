
package edu.upc.cnds.collectivesim.overlay;

import java.util.Map;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayException;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.protocol.Transport;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.underlay.Underlay;
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

public class OverlayModel implements Overlay {

	private Node localNode;
	
	private Map<String,ProtocolModel> protocols;
	
	private Map<String,TransportModel> transports;
	
	private UnderlayModel underlay;
	
	private TopologyModel topology;
	
	public Node getLocalNode() {
		return localNode;
	}

	public Protocol getProtocol(String name)  throws OverlayException {
		ProtocolModel model = protocols.get(name);
		if(model == null) {
			throw new OverlayException("Unknown protocol "+name);
		}
		
		return model.getProtocolModelProxy(localNode);
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

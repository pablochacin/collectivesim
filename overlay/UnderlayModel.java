package edu.upc.cnds.collectivesim.overlay;

import java.util.Map;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.OverlayException;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectivesim.protocol.ProtocolModel;
import edu.upc.cnds.collectivesim.topology.TopologyModel;

public class UnderlayModel {

	private TopologyModel topology;
	
	private Map<String,ProtocolModel> protocols;
	
	public Underlay getUnderlayProxy(Node node) {
		return new UnderlayModelProxy(node,this);
	}
	
	
	public Protocol getProtocol(Node node,String name) {
		ProtocolModel model = protocols.get(name);
		
		if(model == null) {
			throw new OverlayException("Unknown protocol "+name);
		}
		
		return model.getProtocolModelProxy(node);
	}
	
	
}

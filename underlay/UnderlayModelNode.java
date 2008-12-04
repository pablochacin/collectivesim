package edu.upc.cnds.collectivesim.underlay;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.imp.BasicNode;
import edu.upc.cnds.collectives.platform.Platform;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.protocol.Transport;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectives.underlay.UnderlayObserver;

/**
 * Implements a simulated UnderlayNode. Delegates most functions to the UnderlayModel
 * 
 * @author Pablo Chacin
 *
 */
public class UnderlayModelNode extends BasicNode implements UnderlayNode{

	/**
	 * An address that holds a reference to the node in a simulation model.
	 * 
	 * @author Pablo Chacin
	 *
	 */
	
	 private Map<String,Protocol> protocols;
	 
	 private Map<String,Transport> transports;
	
	private UnderlayModel underlay;
	
	
	public UnderlayModelNode(Identifier id,UnderlayModelNodeAddress address,UnderlayModel underlay) {
		super(id, address);
		this.underlay = underlay;
		this.protocols = new HashMap<String, Protocol>();
		this.transports = new HashMap<String,Transport>();

	}

	public Platform getPlatform() {
		return underlay.getPlatform();
	}

	public Node getRefence() {
		return new BasicNode(id,getAddress());
	}
	
	public Protocol getProtocol(String name) {
		return protocols.get(name);
	}

	public Transport getTransport(String name) {
		return transports.get(name);
	}

	public void registerObserver(UnderlayObserver observer) {
		throw new UnsupportedOperationException();
	}

	public void registerProtocol(String name, Protocol protocol) {
		protocols.put(name, underlay.getProtocol(name,this));	
	}

	public void registerTransport(String name, Transport transport) {
		transports.put(name,underlay.getTransport(name,this));
	}
	

}

package edu.upc.cnds.collectivesim.underlay;

import java.util.List;
import java.util.Map;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.transport.Transport;
import edu.upc.cnds.collectives.transport.TransportException;
import edu.upc.cnds.collectives.transport.TransportObserver;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayAddress;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectives.underlay.base.AbstractUnderlayNode;
import edu.upc.cnds.collectivesim.transport.UnderlayModelTransportDynamicProxy;

/**
 * Simulates an UnderlayNode at network location. Offers mechanisms to send messages to another 
 * node in the model. 
 * 
 * Uses a UnderlayModelTransport to simulate the delivery time for messages.
 * 
 * Delegates topology related functions to the UnderlayModel. 
 * 
 * Registers as a TransportObserver to the UnderlayModelTransport to report the number or messages delivered,
 * received and the failures.
 * 
 * @author Pablo Chacin
 *
 */
public class UnderlayModelNode extends AbstractUnderlayNode implements TransportObserver  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected UnderlayModel underlay;
	
	protected UnderlayModelTransportDynamicProxy transport;
	
	protected Double delivered;
	
	protected Double received;
	
	protected Double undelivered;
	
	protected boolean active = true;
	
	public UnderlayModelNode(UnderlayModel underlay,Identifier id, UnderlayAddress address,UnderlayModelTransportDynamicProxy transport,Map attributes) {
		super(id,address,attributes);
		this.underlay= underlay;
		this.transport = transport;
		this.transport.install(this);
		this.transport.registerObserver(this);
		
		this.delivered = 0.0;
		this.undelivered = 0.0;
		this.received = 0.0;
	}
	


	public List<UnderlayNode> getKnownNodes() {
		return underlay.getKnownNodes(this);
	}
	
	public Underlay getUnderlay(){
		return underlay;
	}
	
	/**
	 * Receives a protocol message from another node and delegates to to the transport
	 * 
	 * @param protocolName
	 * @param methodName
	 * @param args
	 * @throws Exception
	 */
	public void handleTransportMessage(UnderlayModelNode source,String protocolName,String methodName,Object[] args) throws TransportException{
		
		if(!active){
			throw new IllegalStateException("Node is inactive");
		}
		transport.handleMessage(source,protocolName,methodName,args);

	}

	@Override
	public Transport getTransport() {
		return transport;
	}

	/**
	 * Sends a message to another UnderlayNode with a given delay
	 * 
	 * @param targetNode
	 * @param protocol
	 * @param method
	 * @param args
	 */
	public void sendTransportMessage(Node targetNode, long delay,String protocol,
			String method, Object[] args){
	 
		underlay.scheduleEvent(targetNode.getId().toString(),delay,"handleTransportMessage",
							this,protocol,method,args);

	}



	@Override
	public void delivered(Node source, Node target, String protocol, int size) {
		delivered++;
	}


	public Double getDelivered(){
		return delivered;
	}

	@Override
	public void received(Node source, Node target, String protocol, int size) {
		received++;
	}

	public Double getReceived(){
		return received;
	}


	@Override
	public void undeliverable(Node Source, Node target, String protocol,
			Exception cause) {
		undelivered++;
	}


	public Double getUndelivered(){
		return undelivered;
	}
	
	
	public boolean ping(Node node){
		UnderlayNode target = underlay.getNode(node.getId());
		if(target == null)
			return false;
		
		node.setAttributes(target.getAttributes());
		
		return true;
		
	}



	@Override
	public void leave() {
		active = false;
		underlay.removeNode(this);
		
	}




}

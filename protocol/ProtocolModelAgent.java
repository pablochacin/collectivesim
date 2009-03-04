package edu.upc.cnds.collectivesim.protocol;

import java.io.Serializable;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.protocol.Destination;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.protocol.ProtocolException;
import edu.upc.cnds.collectives.protocol.ProtocolObserver;
import edu.upc.cnds.collectivesim.model.imp.ReflexionModelAgent;

/**
 * 
 * Maintains a protocol instance for an overlay node and observes its
 * events, reporting basic metrics.
 * 
 * @author Pablo Chacin
 *
 */
public class ProtocolModelAgent extends ReflexionModelAgent implements ProtocolObserver {
	
	protected Logger log;
	
	protected Protocol protocol;

	protected long undeliverable=0;
	
	protected long delivered = 0;
	
	protected long propagated = 0;
	
	protected long dropped = 0;
	
	protected long unreachable = 0;
	
	protected long received = 0;

	protected ProtocolModel model;
	
	public ProtocolModelAgent(Protocol protocol,ProtocolModel model){
		super();
		log = Logger.getLogger("colectivesim.protocol."+this.getClass().getSimpleName());
		this.protocol = protocol;
		protocol.addObserver(this);
	
	}
	

	/**
	 * Propagate a request to a destination using the agent's protocol.  
	 */
	public void propagate(Destination destination, Serializable ... args) throws ProtocolException{
		protocol.propagate(destination, args);
	}

	@Override
	public void deliver(Protocol protocol, Destination destination,	Node source, Serializable... args) {
		delivered++;
	}


	@Override
	public void propagated(Protocol protocol, Destination destination, Node target, Serializable... args) {
		propagated++;		
	}


	@Override
	public void undeliverable(Protocol protocol, Destination destination, Node node, Exception cause, Serializable... args) {
	
		undeliverable++;
	}
	


	@Override
	public void unreachable(Protocol protocol, Destination destination,Serializable... args) {
		unreachable++;
	}

	@Override
	public void dropped(Protocol protocol, Destination destination, Exception cause, Serializable... args) {
		dropped++;		
	}
	
	
	public void received(Protocol protocol, Destination destination,Node source,Serializable...args){
		received++;
	}


	/**
	 * @return the number of requests delivered to this node
	 */
	public Double getDelivered(){
		return (double)delivered;
	}

	/**
	 * 
	 * @return the number of requests that couldn't be propagated from this node
	 */
	public Double getUndeliverable(){
		return (double)undeliverable;
	}
	
	/**
	 * 
	 * @return the number of requests propagated from this node
	 */
	public Double getPropagated(){
		return (double)propagated;
		
	}
	
	/**
	 * 
	 * @return the number of requests dropped at this node 
	 */
	public Double getDropped(){
		return (double)dropped;
		
	}

	/**
	 * 
	 * @return the number of requests that couldn't be propagated because the destination
	 * was unreachable
	 */
	public Double getUnreachable(){
		return (double)unreachable;
		
	}

	public Double getReceived(){
		return (double)received;
	}


}
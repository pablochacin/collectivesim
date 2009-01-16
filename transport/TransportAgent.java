package edu.upc.cnds.collectivesim.transport;

import java.lang.reflect.Method;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.transport.imp.DynamicProxyTransport;
import edu.upc.cnds.collectives.underlay.UnderlayNode;

/**
 * Implements a transport by simulating the communication with another node.
 * Uses the TransportModel to delegate the invocation.
 *  
 * @author Pablo Chacin
 *
 */
public class TransportAgent extends DynamicProxyTransport {

	private UnderlayNode node;
	
	private TransportModel model;
			
	TransportAgent(TransportModel model,UnderlayNode node) {
		this.node = node;
		this.model = model;
	}


	@Override
	protected void invoke(Node target, Protocol protocol,Method method, Object[] args) throws Throwable {
		
		model.invoke(node,target,protocol.getName(), method, args);
	}


	
}

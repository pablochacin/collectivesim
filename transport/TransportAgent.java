package edu.upc.cnds.collectivesim.transport;

import java.lang.reflect.Method;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.transport.imp.DynamicProxyTransport;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectives.util.ReflectionUtils;

/**
 * Implements a transport by simulating the communication with another node.
 * Delegate the invocation to TransportModel.
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


	/**
	 * Handles the invocation of a protocols's 
	 * @param protocol
	 * @param method
	 * @param args
	 */
	void handleInvocation(String protocolName,String methodName,Object[] args) throws Exception{
		
		Protocol protocol = getProtocol(protocolName);
		if(protocol == null){
			throw new Exception("Protocol not registered: ["+protocol +"]");
		}
		
		ReflectionUtils.invoke(protocol,methodName,args);
		
	}
	
	@Override
	/**
	 * This method is invoked by the protocol proxy to actually invoke a method in a remote
	 * node. Delegates this invocation to the TransportModel.
	 */
	protected void invoke(Node target, Protocol protocol,Method method, Object[] args) throws Throwable {
		
		model.invoke(this,target,protocol.getName(), method.getName(), args);
	}

	
	public UnderlayNode getNode(){
		return node;
	}
	
}

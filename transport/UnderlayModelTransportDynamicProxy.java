package edu.upc.cnds.collectivesim.transport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.transport.TransportException;
import edu.upc.cnds.collectives.transport.base.DynamicProxyTransport;
import edu.upc.cnds.collectives.underlay.UnderlayAddress;
import edu.upc.cnds.collectives.util.ReflectionUtils;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;
import edu.upc.cnds.collectivesim.underlay.UnderlayModelNode;

/**
 * Implements a transport by simulating the communication with another node.
 * Calculates the delay and delegates to the UnderlayModel the delivery of
 * the message as an event.
 *  *  
 * @author Pablo Chacin
 *
 */
public class UnderlayModelTransportDynamicProxy extends DynamicProxyTransport {

	private UnderlayModelNode node;
	
	private UnderlayModel underlay;
			
	public UnderlayModelTransportDynamicProxy(UnderlayModel underlay) {
		this.underlay= underlay;
	}

	
	/**
	 * Install this Transport in an UnderlayNode
	 * @param node
	 */
	public void install(UnderlayModelNode node){
		this.node = node;
	}

	/**
	 * Handles the invocation of a protocols's 
	 * @param protocol
	 * @param method
	 * @param args
	 */
	public Object handleMessage(UnderlayModelNode source,String protocolName,String methodName,Object[] args) throws TransportException{
				
		Protocol protocol = getProtocol(protocolName);
		if(protocol == null){
			throw new TransportException("Protocol not registered: ["+protocolName +"]");
		}
		
		try {
			return ReflectionUtils.invoke(protocol,methodName,args);
		} catch (Exception e) {
			throw new TransportException("Exception invoking method " + methodName + " in protocol " + protocolName,e);
		}
		
	}
	

	/**
	 * This method is invoked by the protocol proxy to actually invoke a method in a remote
	 * node. 
	 */
	protected Object invoke(UnderlayAddress target, Protocol protocol,Method method, Object[] args) throws TransportException {
		
		 // get the destination Node
		 UnderlayModelNode targetNode = (UnderlayModelNode)underlay.getNode(target);
		
		 if(targetNode == null){
			 throw new TransportException("Target Node not found [" + target.getLocation()+"]");
		 }
		 
		 if(deliveryFails(node, targetNode)){
			notifyUndeliverable(node, target, protocol.getName(), new Exception("Delivery failed")); 
			throw new TransportException("Simulation-generated failure to deliver to [" + target.getLocation()+"]");
		 }
		 else{
			Long delay = getDelay(node,target);
	
					 
			 //node.sendTransportMessage(targetNode,delay,protocol.getName(),method.getName(),args);
			 return targetNode.handleTransportMessage(this.node, protocol.getName(), method.getName(),args);

		 }
		 
	}

	
	/**
	 * Calculates the delay when invoking a protocol in another node.
	 * 
	 * Subclasses can override this method to simulate a delivery time using information
	 * like the distance among the nodes 
	 * 
	 * @param source 
	 * @param target  
	 *
	 * @return the delivery time for the invocation
	 */
	protected long getDelay(UnderlayModelNode source,UnderlayAddress target){
		return 1;
	}


	/**
	 * Indicates if the invocation must fail due to a media failure. Default implementation always succeed (is a
	 * reliable protocol).
	 * 
	 * Subclasses can simulate unreliable media by returning true following a certain model (for example, according
	 * to a certain distribution probability)
	 * 
	 * @return a boolean indicating if this invocation will fail (true) or succeed (false)
	 */
	protected boolean deliveryFails(UnderlayModelNode source, UnderlayModelNode target){
		return false;
	}

}

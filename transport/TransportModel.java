package edu.upc.cnds.collectivesim.transport;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.UnderlayAddress;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;

/**
 * Implements the simulation of a transport mechanism.
 * 
 * Uses the Scheduler to delay the invocation of methods in the target node.
 * 
 * TODO:use the 
 * 
 * @author 
 *
 */
public class TransportModel extends Model {

	/**
	 * Mantains the mapping of transport agents to nodes
	 */
	private Map<UnderlayAddress,TransportAgent> transportAgents;
	
	public TransportModel(Scheduler scheduler) {
		super(scheduler);
		transportAgents = new HashMap<UnderlayAddress,TransportAgent>();
	}

	
	/**
	 * Returns a transport agent for the given node. If none exist, creates it.
	 * 
	 * @param node
	 * @return
	 */
	public TransportAgent getTransportAgent(UnderlayNode node){
		
		
		TransportAgent agent = transportAgents.get(node.getAddress());
		
		if(agent == null){
			agent = new TransportAgent(this,node);
			transportAgents.put(node.getAddress(),agent);
		}
		
		return agent;
	}


	/**
	 * Simulates the invocation on a remote node
	 * 
	 * @param node location of the transport agent that makes the invocation
	 * @param target location of the transport that must handle the invocation
	 * @param protocol the name of the protocol
	 * @param method method to invoke
	 * @param args arguments for the invocaton
	 */
	public void invoke(UnderlayNode node, Node target, String protocol,Method method,
			Object[] args) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}

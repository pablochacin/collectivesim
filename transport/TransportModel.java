package edu.upc.cnds.collectivesim.transport;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.UnderlayAddress;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.imp.AbstractModel;
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
public abstract class TransportModel extends AbstractModel {

	private static Logger log = Logger.getLogger("collectivesim.transport");
	
	/**
	 *  Implements the actual invocation of a method of a protocol in a target 
	 *  TransportAgent
	 */
	private class TransportAction implements Runnable {

		private TransportAgent agent;
		
		private String protocol;
		
		private String method;
		
		private Object[] args;
		
		
		
		TransportAction(TransportAgent agent, String protocol, String method,
				Object[] args) {
			super();
			this.agent = agent;
			this.protocol = protocol;
			this.method = method;
			this.args = args;
		}



		@Override
		public void run() {
	
				try {
					agent.handleInvocation(protocol, method, args);
				} catch (Exception e) {
					log.severe("exception invoking method in protocol");
				}
		}
		
	}
	
	
	
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
	 * Simulates the invocation on a remote node by scheduling its execution after
	 * a delay. 
	 * 
	 * @param agent the TransportAgent that makes the invocation
	 * @param target location of the transport that must handle the invocation
	 * @param protocol the name of the protocol
	 * @param method method to invoke
	 * @param args arguments for the invocation
	 */
	public void invoke(TransportAgent agent, Node target, String protocol,String method,
			Object[] args){
	
	 TransportAgent targetAgent = transportAgents.get(target.getAddress());
	 
	 TransportAction action = new TransportAction(targetAgent,protocol,method,args);
	 
	 Long delay = getDelay(agent.getNode(),targetAgent.getNode());
	 
	 scheduler.scheduleAction(action,delay);
	 
	}
	
	/**
	 * Calculates the delay when invoking a protocol in another node.
	 * 
	 * TODO: use UnderlayModel to actually calculate the delay
	 * 
	 * @param source 
	 * @param target  
	 * @return
	 */
	protected long getDelay(UnderlayNode source,UnderlayNode target){
		return 1;
	}
}

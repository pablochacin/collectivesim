package edu.upc.cnds.collectivesim.collective.imp;

import java.util.Map;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeAccessException;
import edu.upc.cnds.collectivesim.agents.Agent;
import edu.upc.cnds.collectivesim.agents.AgentException;
import edu.upc.cnds.collectivesim.collective.Collective;
import edu.upc.cnds.collectivesim.overlay.Overlay;

/**
 * Representes the local interface to the Collective for agents in a Node
 *  
 *  Current implementation only supports one agent per node
 *  
 * @author Pablo Chacin
 *
 */
public class CollectiveAgent implements Collective, Agent {

	private Agent agent;
	
	private Collective collective;
	
	private Node node;
	
	private Overlay overlay;
	
	
	public CollectiveAgent(Collective collective,Agent agent,Node node,Overlay overlay) {
		this.collective = collective;
		this.agent =  agent;
		this.node = node;
		this.overlay = overlay;
		
	}
	
	//current implementation only handles an agent by each node
	
	public void addAgent(Agent agent) {
		this.agent = agent;
	}



	public Object handelInquire(String attribute) {
		throw new UnsupportedOperationException();
	}



	public void visit(String method,Object[] arguments) {
		
		for(Node n: overlay.getNeighbors(node)) {
			try {
				n.visit(method, arguments);
			} catch (NodeAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}


	public Map inquire(String[] attributes) {
		throw new UnsupportedOperationException();
	}

	
	/**
	 * Allows the Collective to visit the agent in this node
	 * 
	 * @param method
	 * @throws AgentException 
	 */
	public void handleVisit(String method,Object[] args) throws AgentException{
		agent.handleVisit(this, method,args);
	}
	
	
	public Object handleInquire(String attribute) throws AgentException {
		return agent.handelInquire(attribute);
	}

	public String getAgentType() {
		return agent.getAgentType();
	}

	public Identifier getId() {
		return agent.getId();
	}

	public Node getNode() {
		return node;
	}

	public void handleVisit(Collective collective, String method) throws AgentException {
		agent.handleVisit(collective, method);
	}

	public void handleVisit(Collective collective, String method, Object[] arguments) throws AgentException {
		agent.handleVisit(collective, method,arguments);
	}

}

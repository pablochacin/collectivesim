package edu.upc.cnds.collectivesim.underlay;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectives.underlay.UnderlayAddress;
import edu.upc.cnds.collectives.underlay.UnderlayEvent;
import edu.upc.cnds.collectives.underlay.UnderlayException;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.CollectiveSim;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.AgentFactory;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.base.BasicModel;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.transport.UnderlayModelTransportDynamicProxy;
import edu.upc.cnds.collectivesim.underlay.Grid2D.UnderlayModelException;

/**
 * Implements a simulated Underlay. Nodes of the underlay are modeled as {@link ModelAgent}s to allow 
 * the implementation of behaviors (Like mobility, for instance) and observers to analyze its
 * behavior (e.g. network traffict).
 * 
 * @author Pablo Chacin
 *
 */
public class UnderlayModel extends BasicModel implements Underlay {

	
	/**
	 * Maintains a mapping of agent names to UndelayModelNodes.
	 */
	protected Map<String,UnderlayModelNode> nodes;
	
	protected NetworkTopology topology;

	public UnderlayModel(String name,Experiment experiment, NetworkTopology topology,AgentFactory factory,int numNodes,Stream...attributeStreams) {
		super(name,experiment,factory, numNodes);
		this.nodes = new HashMap<String,UnderlayModelNode>();
		this.topology = topology;

	}

	/**
	 * Convenience constructor without factory
	 * 
	 * @param name
	 * @param experiment
	 * @param attributeStreams
	 */
	public UnderlayModel(String name,Experiment experiment, NetworkTopology topology,Stream...attributeStreams) {
		this(name,experiment,topology,null,0,attributeStreams);

	}
	
	
	@Override
	public void populate() throws ModelException{
		try {
		
			super.populate();

			List<UnderlayNode> nodeList = new ArrayList<UnderlayNode>(nodes.values());
			
			topology.generateTopology(nodeList);


		} catch (UnderlayModelException e) {
			throw new ModelException("Exception generating underlay",e);
		}
	}


	/**
	 * Removes the node from the topology. Informs the node's neighbors that the node
	 * is leaving the topology.
	 * 
	 */
	public void removeNode(UnderlayModelNode node)  {

		
		//Inform all neighbors of the lost node
		for(UnderlayAddress a : getKnownNodes(node)) {
			UnderlayModelNode neighbor= (UnderlayModelNode) getAgent(a.toString());
			neighbor.notifyNodeLost(node.getAddress());
		}
	

		topology.removeNode(node);

		nodes.remove(node.getAddress().toString());
		
		super.removeAgent(node.getAddress().toString());

	}

	
		
	@Override
	public UnderlayNode createNode() throws UnderlayException {
		
		UnderlayModelTransportDynamicProxy transport = new UnderlayModelTransportDynamicProxy(this);
		
		
		UnderlayModelNode node =new UnderlayModelNode(this,new UnderlayModelNodeAddress(),transport);
		
		addAgent(node.getAddress().toString(),node);
		
		try {

			topology.addNode(node);
			
			nodes.put(node.getAddress().toString(), node);

			return node;
		} catch (UnderlayModelException e) {
			throw new UnderlayException("Exception creating node",e);
		}
		
	
	}


	@Override
	protected void agentAdded(ModelAgent agent){

		UnderlayModelNode node = nodes.get(agent.getName());

		//Inform all neighbors of the new node
		for(UnderlayAddress a : getKnownNodes(node)) {
			UnderlayModelNode neighbor= (UnderlayModelNode) getAgent(a.toString());
			neighbor.notifyNodeDiscovered(node.getAddress());
		}
	
	

		//report the creation of a new node in the underlay

		UnderlayEvent event = new UnderlayEvent(node,UnderlayEvent.NODE_FOUND,scheduler.getTime());
		experiment.reportEvent(event);
	}

	
	@Override
	public Metric[] probe(Node source, Node Target, String[] metrics)
			throws UnderlayException {
		throw new UnderlayException("Unsuported metrics" + FormattingUtils.arrayToString(metrics));
	}


	@Override
	public String[] getSupportedMetrics() {
		return new String[0];
	}
	
	
	/**
	 * Returns the know nodes for a given node
	 * @param topology
	 * @return
	 */
	public List<UnderlayAddress> getKnownNodes(UnderlayNode node){
		List<UnderlayAddress> nodes = new ArrayList<UnderlayAddress>();
		
		for(UnderlayNode n: topology.getKnownNodes(node)){
			nodes.add(n.getAddress());
		}
		
		return nodes;
	}

	@Override
	public List<Node> resolve(InetAddress host) throws UnderlayException {
		throw new UnsupportedOperationException();
	}

	
	public UnderlayNode getNode(String location){
		return nodes.get(location);
	}

	@Override
	public void reset() {
		
		super.reset();
		
		nodes.clear();
		
		topology.reset();
	}


	
	
}

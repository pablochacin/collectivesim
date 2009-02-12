package edu.upc.cnds.collectivesim.topology;

import java.util.ArrayList;
import java.util.List;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.events.EventCollector;
import edu.upc.cnds.collectives.events.EventFilter;
import edu.upc.cnds.collectives.events.EventObserver;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.topology.BasicTopology;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.monitoring.TopologyEvent;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.imp.AbstractModel;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;

/**
 * 
 * Builds and maintains a topology of nodes, generating the local topology view for
 * each node. This view is maintained by a TopologyModelAgent.
 * 
 * @author Pablo Chacin
 *
 */
public abstract class TopologyModel extends AbstractModel implements EventCollector {
	
	protected List<UnderlayNode>nodes; 
	
	protected List<Topology>topologies;
	
	protected List<EventObserver>observers;
		
	public TopologyModel(Scheduler scheduler,List<UnderlayNode>nodes) {
		super(scheduler);
		this.nodes = nodes;
		this.observers = new ArrayList<EventObserver>();
		this.topologies = new ArrayList<Topology>();
		
		addBehavior("Topology Update", "updateTopology",20,10);
	}

	/**
	 * Generates the local view of the topology for a particular UnderlayNode
	 * 
	 * @param node the UnderlayNode to generate the view for.
	 * 
	 * @return the local view of the topology for a node
	 * 
	 */
	public Topology getTopology(UnderlayNode node){
		Topology topology = buildTopology(node);
		TopologyAgent agent = new TopologyAgent(this,topology);
		topologies.add(topology);
		super.addAgent(agent);
		return topology;
	}
	
	
	/**
	 * Generates a topology
	 */
	public abstract void generateTopology();
	
	/**
	 * Adds node to the topology
	 * 
	 * @param node
	 */
	public void addNode(UnderlayNode node){
		nodes.add(node);
	}
	
	/**
	 * Construct a topology agent for the given node
	 * 
	 * @param node
	 * @return
	 */
	protected abstract Topology buildTopology(UnderlayNode node) ;

	/**
	 * 
	 * @param localNode
	 * @param node
	 */
	public void nodeJoin(Node localNode, Node node) {
        //Report that the node is part of the topology
        Event event = new TopologyEvent(localNode,TopologyEvent.TOPOLOGY_JOIN,
        		                        getCurrentTime(),node);
        reportEvent(event);
	}

	/**
	 * 
	 * @param localNode
	 * @param node
	 */
	public void nodeLeave(Node localNode, Node node) {
        //Report that the node is part of the topology
        Event event = new TopologyEvent(localNode,TopologyEvent.TOPOLOGY_LEAVE,
        								getCurrentTime(),node);
        reportEvent(event);
		
	}
	
	private void reportEvent(Event event){
		for(EventObserver o: observers){
			o.notify(event);
		}
	}
    /**
     * Registers an observer to be notified of the events reported to this Collector
     * 
     * @param observer
     */
    public void registerObserver(EventObserver observer){
    	this.observers.add(observer);
    }

    public void registerObserver(EventObserver observer, EventFilter filter){
    	throw new UnsupportedOperationException();
    }
    
    /**
     * Return a list of the nodes that form the global topology
     * @return
     */
    public List<Topology> getTopologies(){
    	return new ArrayList<Topology>(topologies);
    }
}

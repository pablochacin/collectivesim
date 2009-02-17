package edu.upc.cnds.collectivesim.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.events.EventCollector;
import edu.upc.cnds.collectives.events.EventFilter;
import edu.upc.cnds.collectives.events.EventObserver;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.topology.BasicTopology;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.monitoring.TopologyEvent;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.imp.AbstractModel;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * 
 * Builds and maintains a topology of nodes, generating the local topology view for
 * each node. This view is maintained by a TopologyModelAgent.
 * 
 * @author Pablo Chacin
 *
 */
public abstract class TopologyModel extends AbstractModel implements EventCollector {
	

	protected List<EventObserver>observers;
	
	protected UnderlayModel underlay;
	
	protected Map<Identifier,Topology>topologies;
	
		
	public TopologyModel(Scheduler scheduler,UnderlayModel underlay) {
		super(scheduler);
		this.underlay =  underlay;
		this.observers = new ArrayList<EventObserver>();
		this.topologies = new HashMap<Identifier,Topology>();
			
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
		
		Topology topology = topologies.get(node.getId());
		if(topology == null){
			buildTopology(node);
			topologies.put(node.getId(),topology);
		}
		
		TopologyAgent agent = new TopologyAgent(this,topology);
		
		super.addAgent(agent);
		return topology;
	}
	
	public UnderlayModel getUnderlay(){
		return underlay;
	}
	
	/**
	 * Generates a topology
	 */
	public abstract void generateTopology();
	
	
	/**
	 * Construct a topology agent for the given node
	 * 
	 * @param node
	 * @return
	 */
	protected abstract Topology buildTopology(UnderlayNode node) ;

	public List<Topology> getTopologies(){
		return new ArrayList<Topology>(topologies.values());
	}
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
    
}

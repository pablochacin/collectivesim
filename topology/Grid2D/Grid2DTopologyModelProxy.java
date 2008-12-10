package edu.upc.cnds.collectivesim.topology.Grid2D;

import java.util.ArrayList;
import java.util.List;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.topology.NodeFilter;
import edu.upc.cnds.collectives.topology.NodeSampler;
import edu.upc.cnds.collectives.topology.NodeView;
import edu.upc.cnds.collectives.topology.TopologyException;
import edu.upc.cnds.collectives.topology.TopologyObserver;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.imp.BasicView;


/**
 * Implements a node's local topology based on a 2D Grid.
 * 
 * Starting from the node, it returns the node's neighbors up to the maximun 
 * number specified. The scope is set to match that maximun size.
 * 
 * @author Pablo Chacin
 *
 */
public class Grid2DTopologyModelProxy implements Topology {

	
	private List<TopologyObserver> observers;
	
	private Grid2DModel grid;
	
	private Grid2DLocation location;
	
	private Node localNode;
	
	public Grid2DTopologyModelProxy(Grid2DModel grid,Grid2DLocation location,Node localNode) {
		this.grid = grid;
		this.localNode = localNode;
		this.location = location;
		this.observers = new ArrayList<TopologyObserver>();
	}

	/**
	 * Returns the location of this topology node in the Topology
	 * @return
	 */
	public Grid2DLocation getLocation() {
		return location;
	}
	
	public void addObserver(TopologyObserver observer) {
		observers.add(observer);
	}

	public List<Node> getNodes() {
		return grid.getNeighbors(location);
	}

	public NodeView createView(String name, NodeSampler sampler, NodeFilter filter, int size) {

		BasicView view = new BasicView(size,this,sampler,filter);
		addObserver(view);
		return view;
	}

	public void notAlive(List<Node> nodes) {
		for(Node n: nodes) {
			notAlive(n);
		}
	}

	public void notAlive(Node node) {

		for(TopologyObserver o: observers) {
			o.leave(node);
		}
	}

	public void propose(List<Node> candidates) {
		for(Node n: candidates) {
			propose(n);
		}
	}

	public void propose(Node node) {
		for(TopologyObserver o: observers) {
			o.join(node);
		}
	}

	public void removeObserver(TopologyObserver observer) {
		observers.remove(observer);
	}

	public void removeView(NodeView view) {
		removeObserver((TopologyObserver) view);
	}

	public int getSize() {
		return getNodes().size();
	}



	Node getLocalNode() {
		return localNode;
	}

	public NodeView getView(String name) throws TopologyException {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeView(String view) {
		// TODO Auto-generated method stub
		
	}
	


}

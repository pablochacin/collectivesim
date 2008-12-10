package edu.upc.cnds.collectivesim.topology.Grid2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import uchicago.src.sim.space.Object2DGrid;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.topology.NodeView;
import edu.upc.cnds.collectives.topology.TopologyObserver;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.imp.BasicView;
import edu.upc.cnds.collectivesim.topology.TopologyModel;
import edu.upc.cnds.collectivesim.topology.TopologyModelException;

/**
 * Simulates a topology using a 
 * @author pchacin
 *
 */
public class Grid2DModel  implements TopologyModel{

    /**
     * Location strategies
     */
    static public LocationStrategy RANDOM = new Random2DLocationStrategy();
       
    
    private static Logger log = Logger.getLogger("collectivesim.topology.Grid2D");
   
    /**
     * 2D space for agents. Implemented using Repast Object2DGrid
     */
    private Object2DGrid space; 
    
    /**
     * Size of the space
     */
    int sizeX,sizeY;
    
    /**
     * Scope of the neighborhood in number of cells
     */
    int scope;    
    
    /**
     * Location strategy
     */
    
    private LocationStrategy locationStrategy;
    
    private Map<Identifier,Grid2DTopologyModelProxy> topologyNodes; 
    
  
    /**
     * Constructor with initial dimensions
     * 
     * @param model a simulation Model on which this Realm inhabits
     * @param dimensions dimensions of the realm's space
     * 
     */
    public Grid2DModel(int sizeX, int sizeY,int scope,LocationStrategy strategy){

        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.scope = scope;
        this.locationStrategy = strategy;
        this.topologyNodes = new HashMap<Identifier,Grid2DTopologyModelProxy>();       
        
        //initialize the realm's space
        //create the Repast's Object2Dgrid that supports this space
        space = new Object2DGrid(sizeX,sizeY);
                      
     }
        
    
    public boolean isEmpty(int x, int y) {

        //check if the location is empty
        return (space.getObjectAt(x, y)== null);
    }

    
    public int getSizeX() {
        return space.getSizeX();
    }
    public int getSizeY() {
        return space.getSizeY();
    }




    public List<Node> getNeighbors(Grid2DLocation location) {
    	Vector<Node> nodes = space.getMooreNeighbors(location.getCoordX(),location.getCoordY(), false);
    	 
    	return nodes;

    }

    public Vector getNeighbors(Grid2DLocation location,int scope) {
        return space.getMooreNeighbors(location.getCoordX(),location.getCoordY(), scope, scope,false);
    }


	public void addNode(Node node) throws TopologyModelException {
    	
		try {
			Grid2DLocation location = locationStrategy.getLocation(this);

			Grid2DTopologyModelProxy topology = new Grid2DTopologyModelProxy(this,location,node);
			
    		space.putObjectAt(location.getCoordX(),location.getCoordY(), topology);
    		
    		topologyNodes.put(node.getId(),topology);
    		
    		Vector neighbors = space.getMooreNeighbors(location.getCoordX(), location.getCoordY(), false);
    		
    		//Iform all neighbors of the new node
    		for(Object t : neighbors) {
    			((Grid2DTopologyModelProxy)t).propose(node);
    		}
    	        
		} catch (Grid2DException e) {
			throw new TopologyModelException("Unable to locate node "+node.toString());
		}
		
	}


	/**
	 * Returns all the nodes in the topology
	 */
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		
		for(Grid2DTopologyModelProxy t : topologyNodes.values()) {
			nodes.add(t.getLocalNode());
		}
	
		return nodes;
	}


	/**
	 * Removes the node from the topology. Informs the node's neighbors that the node
	 * is leaving the topology.
	 * 
	 */
	public void removeNode(Node node) throws TopologyModelException {
		Grid2DTopologyModelProxy topology = topologyNodes.remove(node.getId());
		if(topology == null) {
			throw new TopologyModelException("node "+node.toString() + " not in the topology");

		}
		
		
		Vector neighbors = space.getMooreNeighbors(topology.getLocation().getCoordX(), 
												   topology.getLocation().getCoordY(), false);
		
		//Iform all neighbors of the new node
		for(Object t : neighbors) {
			((Grid2DTopologyModelProxy)t).notAlive(node);
		}
	}


	public Topology getTopologyModelProxy(Node node) {
		Grid2DTopologyModelProxy topology = topologyNodes.get(node.getId());
		if(topologyNodes == null) {
			throw new IllegalArgumentException("node "+node.toString() + " not in the topology");
		}
		return topology;
	}

}

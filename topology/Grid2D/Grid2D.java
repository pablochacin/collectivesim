package edu.upc.cnds.collectivesim.topology.Grid2D;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import uchicago.src.sim.space.Object2DGrid;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeAddress;
import edu.upc.cnds.collectives.node.imp.BasicNode;
import edu.upc.cnds.collectives.topology.NodeView;
import edu.upc.cnds.collectivesim.topology.TopologyException;

public class Grid2D  {

    /**
     * Location strategies
     */
    static public LocationStrategy RANDOM = new Random2DLocation();
       
    
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
    
  
    /**
     * Constructor with initial dimensions
     * 
     * @param model a simulation Model on which this Realm inhabits
     * @param dimensions dimensions of the realm's space
     * 
     */
    public Grid2D(int sizeX, int sizeY,int scope,LocationStrategy strategy){

        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.scope = scope;
        this.locationStrategy = strategy;
               
        //initialize the realm's space
        //create the Repast's Object2Dgrid that supports this space
        space = new Object2DGrid(sizeX,sizeY);
                      
     }
        

    /**
     * Adds a node to the topology
     * 
     * @param agent
     * @return
     * @throws TopologyException 
     * 
     */
    public Node getNode(Identifier id) throws TopologyException {

        //create a location for the agent
    	
    	Node node = null;
		try {
			Grid2DAddress address = locationStrategy.getLocation(this);

			 node = new BasicNode(id,address);

    		space.putObjectAt(address.getX(),address.getY(), node);
    	        
        	return node;
		} catch (Grid2DException e) {
			throw new TopologyException("Unable to locate node for id "+id.toString());
		}
		
    }
    
    

    public Node getNode(NodeAddress address) throws TopologyException{
 
    	Grid2DAddress address2D = (Grid2DAddress)address;
    	
    	Node node = (Node)space.getObjectAt(address2D.getX(),address2D.getY());
    	
    	if(node == null) {
    		throw new TopologyException("Node not found");
    	}
    	
       	return node;
        
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

	public NodeView getView(Grid2DAddress address,int maxSize,int scope) {
			return new Grid2DView(address,maxSize,scope,this);
	}


    public List<Node> getNeighbors(Grid2DAddress address) {
    	Vector<Node> nodes = space.getMooreNeighbors(address.getX(),address.getY(), false);
    	 
    	return nodes;

    }

    public Vector getNeighbors(Grid2DAddress address,int scope) {
        return space.getMooreNeighbors(address.getX(),address.getY(), scope, scope,false);
    }

}

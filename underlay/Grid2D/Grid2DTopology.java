package edu.upc.cnds.collectivesim.underlay.Grid2D;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import uchicago.src.sim.space.Object2DGrid;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.UnderlayException;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.underlay.NetworkTopology;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;


/**
 * Simulates an Underlay using a 2D grid on which nodes are placed. 
 * A node's neighborhood is defined by the located in adjacent locations
 * within a radius. 
 * 
 * @author Pablo Chacin
 *
 */
public class Grid2DTopology implements NetworkTopology{


       
    
    private static Logger log = Logger.getLogger("collectivesim.underlay.Grid2D");
   
    /**
     * 2D space for agents. Implemented using Repast Object2DGrid
     */
    private Object2DGrid grid; 
    
    
    private Map<UnderlayNode,Grid2DLocation> locations;

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
 * Constructor with full parameters
 * @param scheduler simulation Scheduler
 * @param ids a Stream of identifiers to create nodes
 * @param numNodes number of nodes in the model
 * @param sizeX size of X coordinate space for model
 * @param sizeY size of Y coordinate space for model
 * @param scope diameter of a location's neighborhood 
 * @param strategy LocationStrategy used to place nodes
 */
    public Grid2DTopology(int sizeX, int sizeY,int scope,LocationStrategy strategy){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.scope = scope;
        this.locationStrategy = strategy;      
        
        //initialize the realm's space
        //create the Repast's Object2Dgrid that supports this space
        grid = new Object2DGrid(sizeX,sizeY);
                      
        locations = new HashMap<UnderlayNode,Grid2DLocation>();
     }
        
    /**
     * Convenience constructor. Calculates the size of the grid to accomodate the nodes
     * @param name
     * @param experiment
     * @param ids
     * @param numNodes
     * @param scope
     * @param strategy
     * @param attributes
     */
    public Grid2DTopology(int numNodes,int scope,LocationStrategy strategy){
    	 this((int)Math.ceil(Math.sqrt(numNodes)),(int)Math.ceil(Math.sqrt(numNodes)),scope,strategy);
    } 	
    public boolean isFree(int x, int y) {

        //check if the location is empty
        return (grid.getObjectAt(x, y)== null);
    }

    
    public int getSizeX() {
        return grid.getSizeX();
    }
    
    public int getSizeY() {
        return grid.getSizeY();
    }
   
    public  int getNumLocations(){
    	return locations.size();
    }

	@Override
	public List<UnderlayNode> getKnownNodes(UnderlayNode node) {
		
		
		Grid2DLocation location = locations.get(node);
		
		try{
    	Vector<UnderlayNode> nodes = grid.getMooreNeighbors(location.getCoordX(),location.getCoordY(),false);
   	 
    	return (new ArrayList<UnderlayNode>(nodes));
		}catch(NullPointerException e){
			throw e;
		}
}



	@Override
	public void generateTopology(List<? extends UnderlayNode> nodes)
			throws UnderlayModelException {
		
		for(UnderlayNode n: nodes){
			addNode(n);
		}
	}

	@Override
	public void addNode(UnderlayNode node) throws UnderlayModelException {
		Grid2DLocation location;
		
			location = locationStrategy.getLocation(this);

		
			double positionX = (double)location.getCoordX()/(double)sizeX;
			double positionY = (double)location.getCoordY()/(double)sizeY;
		
			node.getAttributes().put("positionX", positionX);
			node.getAttributes().put("positionY", positionY);
		
			grid.putObjectAt(location.getCoordX(),location.getCoordY(), node);
			locations.put(node,location);
	
		
	}

	@Override
	public void removeNode(UnderlayNode node) {
		
		Grid2DLocation location = locations.remove(node);
		
		grid.putObjectAt(location.getCoordX(), location.getCoordY(), null);
	}

}

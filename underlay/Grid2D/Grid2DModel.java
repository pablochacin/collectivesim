package edu.upc.cnds.collectivesim.underlay.Grid2D;

import java.net.InetAddress;
import java.util.ArrayList;
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
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;


/**
 * Simulates an Underlay using a 2D grid on which nodes are placed. 
 * A node's neighborhood is defined by the located in adjacent locations
 * within a radius. 
 * 
 * @author Pablo Chacin
 *
 */
public class Grid2DModel extends UnderlayModel{


       
    
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
    public Grid2DModel(String name,Experiment experiment,Stream<Identifier>ids, int numNodes,int sizeX, int sizeY,int scope,LocationStrategy strategy,Stream ...attributes){
    	super(name,experiment,ids,numNodes,attributes);
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
    public Grid2DModel(String name,Experiment experiment,Stream<Identifier>ids, int numNodes,int scope,LocationStrategy strategy,Stream ...attributes){
    	 this(name,experiment,ids,numNodes,(int)Math.ceil(Math.sqrt(numNodes)),(int)Math.ceil(Math.sqrt(numNodes)),scope,strategy,attributes);
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
   

	@Override
	public List<Node> getKnownNodes(UnderlayNode node) {
		
		Grid2DLocation location = locations.get(node);
		
    	Vector nodes = grid.getMooreNeighbors(location.getCoordX(),location.getCoordY(),false);
   	 
    	return (new ArrayList<Node>(nodes));
	}





	@Override
	public List<Node> resolve(InetAddress host) throws UnderlayException {
		throw new UnsupportedOperationException();
	}


	@Override
	public String[] getSupportedMetrics() {
		throw new UnsupportedOperationException();
	}


	@Override
	protected void generateNetworkTopology(List<? extends UnderlayNode> nodes)
			throws UnderlayModelException {
		
		for(UnderlayNode n: nodes){
			Grid2DLocation location = locationStrategy.getLocation(this);
			
			double positionX = (double)location.getCoordX()/(double)sizeX;
			double positionY = (double)location.getCoordY()/(double)sizeY;
		
			n.getAttributes().put("positionX", positionX);
			n.getAttributes().put("positionY", positionY);
		
			grid.putObjectAt(location.getCoordX(),location.getCoordY(), n);
			locations.put(n,location);
	    		   		
		}
	}


	@Override
	protected void terminate() {
		grid = new Object2DGrid(sizeX,sizeY);
		
	}
}

package edu.upc.cnds.collectivesim.underlay.Grid2D;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import uchicago.src.sim.space.Object2DGrid;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.UnderlayException;
import edu.upc.cnds.collectives.underlay.UnderlayMetricType;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.Stream;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;
import edu.upc.cnds.collectivesim.underlay.UnderlayModelNode;


/**
 * Simulates an Underlay using a 2D grid on which nodes are placed. 
 * A node's neighborhood is defined by the located in adjacent locations
 * within a radius. 
 * 
 * @author Pablo Chacin
 *
 */
public class Grid2DModel extends UnderlayModel{

    /**
     * Location strategies
     */
    static public LocationStrategy RANDOM = new Random2DLocationStrategy();
       
    
    private static Logger log = Logger.getLogger("collectivesim.underlay.Grid2D");
   
    /**
     * 2D space for agents. Implemented using Repast Object2DGrid
     */
    private Object2DGrid grid; 
    
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
    public Grid2DModel(Scheduler scheduler,Stream<Identifier>ids, int numNodes,int sizeX, int sizeY,int scope,LocationStrategy strategy){
    	super(scheduler,ids,numNodes);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.scope = scope;
        this.locationStrategy = strategy;      
        
        //initialize the realm's space
        //create the Repast's Object2Dgrid that supports this space
        grid = new Object2DGrid(sizeX,sizeY);
                      
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
    	Vector nodes = grid.getMooreNeighbors(((Grid2DUnderlayNode)node).getLocation().getCoordX(),
    															   ((Grid2DUnderlayNode)node).getLocation().getCoordY(), false);
   	 
    	return (new ArrayList<Node>(nodes));
	}


	@Override
	//TODO maybe makes more sense to populate the grid in the creteNetworkTopology method
	//     but the node's constructor requires the location. Maybe put this  info in a 
	//     Map<Identifier,Location> and populate it in that method (no need to extend UnderlayNode)
	public UnderlayModelNode buildNode(Identifier id) throws UnderlayModelException {
	    	
		Grid2DLocation location;

			location = locationStrategy.getLocation(this);
			Grid2DUnderlayNode node = new Grid2DUnderlayNode(id,null,location,this);
			
	   		grid.putObjectAt(location.getCoordX(),location.getCoordY(), node);
		    		
	   		super.addNode(node);
	   		
	   		return node;	  	

	}


	@Override
	public List<Node> resolve(InetAddress host) throws UnderlayException {
		throw new UnsupportedOperationException();
	}


	@Override
	public Metric[] probe(UnderlayNode source, UnderlayNode target,	UnderlayMetricType[] metrics) {
		throw new UnsupportedOperationException();
	}


	@Override
	public UnderlayMetricType[] getSupportedMetrics() {
		throw new UnsupportedOperationException();
	}


	@Override
	protected void generateNetworkTopology(List<? extends UnderlayNode> nodes)
			throws UnderlayModelException {
	
		//There is no need to do anything, as the topology is created as the nodes are added
	}
}

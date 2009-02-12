package edu.upc.cnds.collectivesim.underlay.Grid2D;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import uchicago.src.sim.space.Object2DGrid;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.UnderlayException;
import edu.upc.cnds.collectives.underlay.UnderlayMetricType;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.topology.TopologyAgent;
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
    public Grid2DModel(Scheduler scheduler,int sizeX, int sizeY,int scope,LocationStrategy strategy){
    	super(scheduler);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.scope = scope;
        this.locationStrategy = strategy;      
        
        //initialize the realm's space
        //create the Repast's Object2Dgrid that supports this space
        space = new Object2DGrid(sizeX,sizeY);
                      
     }
        
    
    public boolean isFree(int x, int y) {

        //check if the location is empty
        return (space.getObjectAt(x, y)== null);
    }

    
    public int getSizeX() {
        return space.getSizeX();
    }
    
    public int getSizeY() {
        return space.getSizeY();
    }


    public Vector getNeighbors(Grid2DLocation location,int scope) {
        return space.getMooreNeighbors(location.getCoordX(),location.getCoordY(), scope, scope,false);
    }

    


	@Override
	public UnderlayNode[] getKnownNodes(UnderlayNode node) {
    	Vector nodes = space.getMooreNeighbors(((Grid2DUnderlayNode)node).getLocation().getCoordX(),
    															   ((Grid2DUnderlayNode)node).getLocation().getCoordY(), false);
   	 
    	return (UnderlayNode[]) nodes.toArray(new UnderlayModelNode[0]);
	}


	@Override
	public UnderlayNode getNode(Identifier id) throws UnderlayException{
	    	
		Grid2DLocation location;
		try {
			location = locationStrategy.getLocation(this);
			Grid2DUnderlayNode node = new Grid2DUnderlayNode(id,null,location,this);
			
	   		space.putObjectAt(location.getCoordX(),location.getCoordY(), node);
		    		
	   		super.addNode(node);
	   		
	   		return node;
	   		
		} catch (UnderlayModelException e) {
			throw new UnderlayException(e);
		}


	}


	@Override
	public Node[] resolve(InetAddress host) throws UnderlayException {
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
}

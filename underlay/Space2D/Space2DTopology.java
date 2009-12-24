package edu.upc.cnds.collectivesim.underlay.Space2D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.underlay.NetworkTopology;
import edu.upc.cnds.collectivesim.underlay.Grid2D.UnderlayModelException;


/**
 * Simulates a 2D space on which the nodes are located. Nodes are considered neighbors if the distance
 * among them is less than a given range. 
 *  
 * The space is divided in cell, to allow efficient searching of neighbors
 * 
 * 
 * 
 * @author Pablo Chacin
 *
 */
public class Space2DTopology implements NetworkTopology{
       
    
    private static Logger log = Logger.getLogger("collectivesim.underlay.topology.Space2D");
   
    /**
     * a 2D grid with a vector of locations on each position
     */
    private Vector<Space2DLocation>[][] grid; 
    
    
    private Double sizeX,sizeY;
    
    private int gridSizeX,gridSizeY;
    
    Double scope;
    
    private Map<UnderlayNode,Space2DLocation> locations;

    
    /**
     * Scope of the neighborhood in distance
     */
    Double range;    
    
    /**
     * Location strategy
     */
    
    private SpaceLocationStrategy locationStrategy;
     
  
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
    public Space2DTopology(Double sizeX, Double sizeY,Double scope,SpaceLocationStrategy strategy){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.scope = scope;
        this.locationStrategy = strategy;      
        
        //initialize the space's grid 
        gridSizeX = (int)(sizeX/scope);
        gridSizeY = (int)(sizeY/scope);
        
        initGrid();
     }
        
	

    
    public Double getSizeX() {
        return sizeX;
    }
    
    public Double getSizeY() {
        return sizeY;
    }
   
    
    

	public int getGridSizeX() {
		return gridSizeX;
	}




	public int getGridSizeY() {
		return gridSizeY;
	}




	@Override
	public List<UnderlayNode> getKnownNodes(UnderlayNode node) {
		
		Vector<UnderlayNode> neighbors = new Vector<UnderlayNode>();
		
		Space2DLocation location = locations.get(node);
		 if(location == null){			 
			 throw new IllegalArgumentException("The node[" + node.getId().toString() + "] is not in the topology");			 
		 }
		
		int startX = (int)Math.max(0,location.getGridX()-1);
		int endX =   (int)Math.min(gridSizeX, location.getGridX()+1);
		int startY = (int)Math.max(0,location.getGridY()-1);
		int endY =   (int)Math.min(gridSizeY, location.getGridY()+1);
		
		for(int i = startX;i < endX;i++)
			for(int j = startY;j < endY;j++){
				Vector<Space2DLocation> locations = grid[i][j];
				
				for(Space2DLocation l: locations){
					if(distance(location,l) < scope){
						neighbors.add(l.getNode());
					}
				}
			}
		
		//remove itself
		neighbors.remove(node);
		
		
		return neighbors;
	}



	private Double distance(Space2DLocation l1,Space2DLocation l2){
		return Math.sqrt(Math.pow(l1.getCoordX() - l2.getCoordX(),2) + 
				         Math.pow(l1.getCoordY() - l2.getCoordY(),2));
	}
	
	@Override
	public void generateTopology(List<? extends UnderlayNode> nodes) throws UnderlayModelException   {
		
		for(UnderlayNode n: nodes){
			addNode(n);
		}
	}

	@Override
	public void addNode(UnderlayNode node) throws UnderlayModelException {
		
			
			Space2DLocation location  = locationStrategy.getLocation(this,node);
			
			node.getAttributes().put("positionX", location.getCoordX());
			node.getAttributes().put("positionY", location.getCoordY());
			
			grid[location.getGridX()][location.getGridY()].add(location);
			
			locations.put(node,location);
	
		
	}

	@Override
	public void removeNode(UnderlayNode node) {
		
		Space2DLocation location = locations.remove(node);
		
		int gridX = (int)(location.getCoordX()/sizeX);
		int gridY = (int)(location.getCoordY()/sizeY);
		
		grid[gridX][gridY].remove(location);
		
	}




	@Override
	public void reset() {
		
		initGrid();
	}

	@SuppressWarnings("unchecked")
	private void initGrid(){
		
        grid = (Vector<Space2DLocation>[][]) new Vector[gridSizeX][gridSizeY];
        
        for(int i = 0; i< gridSizeX;i++)
        	for(int j=0; j < gridSizeY;j++){
        		grid[i][j] = new Vector<Space2DLocation>();
        	}
                      
        locations = new HashMap<UnderlayNode,Space2DLocation>();
	}
	
	
}

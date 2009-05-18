package edu.upc.cnds.collectivesim.underlay.Grid2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A LocationStrategy that selects free Locations in a 2D randomly
 *   
 * @author Pablo Chacin
 *
 */
public class Random2DLocationStrategy implements LocationStrategy{


    
    /**
     * Random number generator used to generate random
     * positions
     */
    private Random rand;

    private List<Grid2DLocation>locations;
    
    private int sizeX,sizeY;
    
    /**
     * Default Constructor
     */
    public Random2DLocationStrategy(int sizeX,int sizeY){
    	this.sizeX = sizeX;
    	this.sizeY = sizeY;
        rand = new Random();        
        locations = new ArrayList<Grid2DLocation>(sizeX*sizeY);
        for(int x = 0;x<sizeX;x++){
        	for(int y =0;y<sizeY;y++){
        		locations.add(new Grid2DLocation(x,y));
        	}
        }
    }
    
    /**
     * Convenience constructor. Calculates the size of the grid to accomodate the nodes.
     * 
     * @param numNodes
     */
    public Random2DLocationStrategy(int numNodes){
    	this((int)Math.ceil(Math.sqrt(numNodes)),(int)Math.ceil(Math.sqrt(numNodes)));
    }

	public Grid2DLocation getLocation(Grid2DModel grid) throws UnderlayModelException{
		
		if(locations.isEmpty()){
			throw new UnderlayModelException("Not available cell found");
		}
		
		Grid2DLocation location = locations.remove(rand.nextInt(locations.size()));
		
		return location;
	}


    
}

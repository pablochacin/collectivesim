package edu.upc.cnds.collectivesim.topology.Grid2D;

import java.util.Random;


/**
 * Locates the nodes randomly in a 2D topology
 * 
 * @author pchacin
 *
 */
public class Random2DLocation implements LocationStrategy{


    
    /**
     * Random number generator used to generate random
     * positions
     */
    private Random rand;

    /**
     * Default Constructor
     */
    public Random2DLocation(){
        rand = new Random();
    }
    

	public Grid2DAddress getLocation(Grid2D grid) throws Grid2DException {
		
		//attemps for each possition.
		int attempts  = grid.getSizeX()*grid.getSizeY();
		
        for(int i =0;i<attempts;i++){
            int x = rand.nextInt(grid.getSizeX()-1);
            int y = rand.nextInt(grid.getSizeY()-1);
            
            if(grid.isEmpty(x, y)){
                return new Grid2DAddress(x,y,grid);
            }
        }
        
        throw new Grid2DException("Not available cell found");

	}


    
}

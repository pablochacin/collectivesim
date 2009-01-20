package edu.upc.cnds.collectivesim.underlay.Grid2D;

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

    /**
     * Default Constructor
     */
    public Random2DLocationStrategy(){
        rand = new Random();
    }
    

	public Grid2DLocation getLocation(Grid2DModel grid) throws UnderlayModelException{
		
		//Attempts to retry if selected location is not free.
		
		int attempts  = grid.getSizeX()*grid.getSizeY();
		
        for(int i =0;i<attempts;i++){
            int x = rand.nextInt(grid.getSizeX()-1);
            int y = rand.nextInt(grid.getSizeY()-1);
            
            if(grid.isFree(x, y)){
                return new Grid2DLocation(grid,x,y);
            }
        }
        
        throw new UnderlayModelException("Not available cell found");

	}


    
}

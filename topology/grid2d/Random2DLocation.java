package edu.upc.cnds.collectivesim.topology.grid2d;

import java.util.Random;

import edu.upc.cnds.collectivesim.agents.Agent;
import edu.upc.cnds.collectivesim.topology.Topology;
import edu.upc.cnds.collectivesim.topology.discrete2D.Discrete2DLocation;

/**
 * Locates the nodes randomly in a 2D topology
 * 
 * @author pchacin
 *
 */
public class Random2DLocation implements LocationStrategy{

    /**
     * Default number of relocation attempts 
     */
    private static int DEFAULT_RELOCATION_ATTEMPTS = 10;
    
    /**
     * Topology on which the nodes must be located
     */
    private Grid2D topology;
    
    /**
     * Numbre of attempts to loate an location 
     */
    private int relocationAttemps;
    
    /**
     * Random number generator used to generate random
     * positions
     */
    private Random rand;

    /**
     * Size of 
     */
    /**
     * Default Constructor
     */
    public Random2DLocation(){
        rand = new Random();
        relocationAttemps = DEFAULT_RELOCATION_ATTEMPTS;
    }
    
    public boolean locate(Cell cell) {
    
        for(int i =0;i<relocationAttemps;i++){
            int x = rand.nextInt(topology.getSizeX()-1);
            int y = rand.nextInt(topology.getSizeY()-1);
            
            if(topology.isEmpty(x, y)){
                cell.setCoordinates(x,y);
                return true;
            }
        }
        return false;

    }


	public void setTopology(Topology topology) {
		this.topology = this.topology;
		
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}
    
}

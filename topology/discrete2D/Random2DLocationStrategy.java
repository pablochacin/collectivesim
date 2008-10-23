package edu.upc.cnds.collectivesim.topology.discrete2D;

import java.util.Random;

import edu.upc.cnds.collectivesim.agents.Agent;
import edu.upc.cnds.collectivesim.topology.discrete2D.Discrete2DLocation;


public class Random2DLocationStrategy implements Discrete2DLocationStrategy{

    /**
     * Default number of relocation attempts 
     */
    private static int DEFAULT_RELOCATION_ATTEMPTS = 10;
    
    /**
     * Realm to which the agents must be located
     */
    private Discrete2DRealm realm;
    
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
    public Random2DLocationStrategy(){
        rand = new Random();
        relocationAttemps = DEFAULT_RELOCATION_ATTEMPTS;
    }
    
    public boolean locate(Discrete2DLocation location) {
    
        for(int i =0;i<relocationAttemps;i++){
            int x = rand.nextInt(realm.getSizeX()-1);
            int y = rand.nextInt(realm.getSizeY()-1);
            
            if(realm.isEmpty(x, y)){
                realm.addLocation(location, x, y);
                location.setCoordinates(x,y);
                return true;
            }
        }
        return false;

    }

    public void setRealm(Discrete2DRealm realm) {
        this.realm = realm;
        
    }

    public void reset() {
                
    }
    
}

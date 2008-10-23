package edu.upc.cnds.collectivesim.topology.discrete2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import edu.upc.cnds.collectivesim.agents.Agent;
import edu.upc.cnds.collectivesim.collective.BasicRealm;
import edu.upc.cnds.collectivesim.models.Model;
import edu.upc.cnds.collectivesim.topology.Node;
import edu.upc.cnds.collectivesim.topology.grid2d.Cell;
import edu.upc.cnds.collectivesim.topology.grid2d.Grid2D;
import edu.upc.cnds.collectivesim.topology.grid2d.LocationStrategy;
import edu.upc.cnds.collectivesim.topology.grid2d.Random2DLocation;

import uchicago.src.sim.space.Object2DGrid;
import uchicago.src.sim.util.SimUtilities;

public class Discrete2DRealm extends CollectiveManager implements Grid2D {

    /**
     * Location strategies
     */
    static private LocationStrategy RANDOM = new Random2DLocation();
    
    /**
     * Default location strategy
     */
    private static LocationStrategy DEFAULT_LOCATION_STRATEGY = RANDOM;
    
   
    /**
     * 2D space for agents. Implemented using Repast Object2DGrid
     */
    private Object2DGrid space; 
    
    /**
     * Size of the space
     */
    int sizeX,sizeY;
    
    /**
     * Location strategy
     */
    
    private LocationStrategy locationStrategy;
    
   
    /**
     * Random number generator
     */
    private Random rand;
    
   
    /**
     * Constructor with initial dimensions
     * 
     * @param model a simulation Model on which this Realm inhabits
     * @param dimensions dimensions of the realm's space
     * 
     */
    public Discrete2DRealm(Model model,int sizeX, int sizeY){
        super(model);
 
        this.model = model;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        
       
        //initialize the realm's space
        //create the Repast's Object2Dgrid that supports this space
        space = new Object2DGrid(sizeX,sizeY);
        
        //set the default location strategy and initialize it
        try {
            this.locationStrategy = DEFAULT_LOCATION_STRATEGY.getClass().newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
        locationStrategy.setSpace(this);
        locationStrategy.reset();
        
        rand = new Random();
        
     }
    
    /**
     * 
     */
    public void setLocationStrategy(LocationStrategy locationStrategy){
        
        //Sets and initialize location strategy
        this.locationStrategy = locationStrategy;
        this.locationStrategy.setSpace(this);
        this.locationStrategy.reset();
    }
    
    

    /**
     * Adds an agent in the Realms and locates it the realm's space 
     * @param agent
     * @return
     * 
     */
    public boolean addAgent(Agent agent) {

        //create a location for the agent
        Discrete2DLocation location = new Discrete2DLocation(model,this);
        location.setAgent(agent);
        addLocation(location);
    
        
            
        //locate agent in space using a location strategy
        boolean located = locationStrategy.locate(location);

        //if sucessfully located insert agent in the realm's list
        if(located){
            super.addAgent(agent,location);
            space.putObjectAt(location.getX(),location.getY(),location);
        }
        
        return located;
    }
    
    


    /* *****************************************************************
     * 
     * Methods from AgentSpace2D inteface
     * 
     * ***************************************************************** */


   public Discrete2DLocation getLocation(int x, int y) {
        return (Discrete2DLocation)space.getObjectAt(x, y);
    }


    public boolean isEmpty(int x, int y) {

        //check if the location is empty
        return (space.getObjectAt(x, y)== null);
    }


    public boolean isAvailable(int x,int y){
        return isEmpty(x,y);
    }

   
    
    public int getSizeX() {
        return space.getSizeX();
    }
    public int getSizeY() {
        return space.getSizeY();
    }


    public Vector getNeighbors(int x, int y) {
        return space.getMooreNeighbors(x,y, false);
    }


    public Vector getNeighbors(int x,int y, int scope) {
        return space.getMooreNeighbors(x, y, scope, scope, false);
    }


    public Discrete2DLocation getRandomNeighbor(int x,int y) {
        Vector neighborgs = getNeighbors(x, y);
        
        if(neighborgs.size() != 0){
            int neighbor = rand.nextInt(neighborgs.size());
        
            return (Discrete2DLocation)neighborgs.get(neighbor); 
        }
        else{
            return null;
        }
        
    }

    public boolean addLocation(Discrete2DLocation location, int x, int y) {
      space.putObjectAt(x, y, location);
     
      //TODO: check why it is needed to return a boolean?
      return super.addLocation(location);
    }



    public Cell getCell(int x, int y) {
        return (Cell)getLocation(x, y);
    }

    public ArrayList getCells() {
        return getLocations();
    }



}

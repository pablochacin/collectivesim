package edu.upc.cnds.collectivesim.topology.discrete2D;

import java.util.Comparator;
import java.util.Vector;

import edu.upc.cnds.collectivesim.collective.BasicNode;
import edu.upc.cnds.collectivesim.models.Model;
import edu.upc.cnds.collectivesim.topology.grid2d.Cell;


public class Discrete2DLocation extends BasicNode implements Cell  {

   
    
    private int x,y;

    
    public Discrete2DLocation(Model model, Discrete2DRealm realm) {
       super(model,realm);
    }
      
  
    public void setCoordinates(int x,int y){
        this.x = x;
        this.y = y;
    }




    protected Vector getNeighbors() {
        return ((Discrete2DRealm)this.realm).getNeighbors(x,y);
    }

     
    
    protected Discrete2DLocation getRandomNeighbor() {
      return ((Discrete2DRealm)this.realm).getRandomNeighbor(x, y);   
    }
    
     
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }







}

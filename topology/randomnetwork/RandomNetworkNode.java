package edu.upc.cnds.collectivesim.topology.randomnetwork;

import java.util.Vector;

import edu.upc.cnds.collectivesim.agents.Agent;
import edu.upc.cnds.collectivesim.collective.BasicNode;
import edu.upc.cnds.collectivesim.models.Model;


public class RandomNetworkNode extends BasicNode {

    
    private int position;
    private RandomNetworkRealm realm;
    private Model model;
    
    
    public RandomNetworkNode(Model model, RandomNetworkRealm realm) {
        super(model,realm);
        this.realm = realm;
        this.model = model;
    }
    
  
 
    @Override
    protected Vector getNeighbors() {
        return realm.getNeighbors(position);
    }


    @Override
    protected BasicNode getRandomNeighbor() {
       return realm.getRandomNeighbor(position);
    }



	public Object inquire(Integer id, String attribute) {
		// TODO Auto-generated method stub
		return null;
	}

}

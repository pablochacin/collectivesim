package edu.upc.cnds.collectivesim.topology.randomnetwork;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import edu.upc.cnds.collectivesim.agents.Agent;
import edu.upc.cnds.collectivesim.collective.BasicRealm;
import edu.upc.cnds.collectivesim.models.BasicModel;
import edu.upc.cnds.collectivesim.topology.Node;
import edu.upc.cnds.collectivesim.topology.grid2d.Cell;
import edu.upc.cnds.collectivesim.topology.grid2d.Grid2D;


/**
 * This Topology models a network on which neiborghs are selected randomly
 * among the whole network agents. The network is created with a diven connectivity
 * which defines how many nodes compose the neiborhood. By defaul, this value is
 * DEFAULT_CONNECTIVIY
 * 
 * @author pchacin
 *
 */
public class RandomNetworkRealm extends CollectiveManager implements Grid2D{

	private static int DEFAULT_CONNECTIVITY = 2;
	
	/**
	 * Vector that contains the locations of the network.
	 */
	private Vector network;
	
	/**
	 * number of "neigborgs" per node
	 */
	private int connectivity;
	
    /**
     * Random number generator used to generate random
     * positions
     */
    private Random rand = new Random();
	
	public RandomNetworkRealm(BasicModel model) {
		this(model,DEFAULT_CONNECTIVITY);
	}
	
	public RandomNetworkRealm(BasicModel model, int connectivity) {
		super(model);
		network = new Vector();
		this.connectivity = connectivity;
	}


	/* (non-Javadoc)
	 * @see simrealms.realms.BasicRealm#addAgent(simrealms.agents.Agent)
	 */
    public boolean addAgent(Agent agent) {
		//create a location to hold the agent
		RandomNetworkNode node = new RandomNetworkNode(this.model,this);
        node.setAgent(agent);
        addLocation((Node)node);
        
        //add the location to the network
		network.add(node);
		
		//set the location's position, assumming it is at the end of the vector
		node.setPosition(network.size()-1);
		
		return super.addAgent(agent,node);
	}

	/**
	 * Returns the neighbors of a position by loking for as many
	 * random locations as specified in the connectivity field.
	 * 
	 * @param position
	 * @return
	 */
	public Vector getNeighbors(int position) {
		return getRandomNeighborhood(position,this.connectivity);
	}
	
	/**
	 * 
	 */
	public RandomNetworkNode getRandomNeighbor(int position){
		//look for one single neigborg and return it
		return (RandomNetworkNode)getRandomNeighborhood(position,1).get(0);
	}
	
	/**
	 * returns a random subset of the network, excepting the given position
	 * @param position
	 * @param number
	 * @return
	 */
    private Vector getRandomNeighborhood(int position,int number) {
		Vector neighborhood = new Vector(number);
		int i = 0;
		while(i < number){
			//select a random location
			int loc = rand.nextInt(network.size()-1);
			RandomNetworkNode neigborg = (RandomNetworkNode)network.get(loc);
			
			//if not already selected, select
			if(!neighborhood.contains(neigborg) &&(loc != position)){
				neighborhood.add(neigborg);
				i++;
			}
		}
		 
		return neighborhood;
		
	}

	public Cell getCell(int x, int y) {
		return (Cell)network.get(x);
				
	}

	public List getCells() {
		return network.subList(0, network.size()-1);
	}

	public int getSizeX() {
		return network.size();
	}

	public int getSizeY() {

		return 1;
	}

	public boolean isAvailable(int x, int y) {
		throw new UnsupportedOperationException();       
	}

	public boolean isEmpty(int x, int y) {
		throw new UnsupportedOperationException();
	}



	
}

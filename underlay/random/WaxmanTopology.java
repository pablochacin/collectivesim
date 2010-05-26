package edu.upc.cnds.collectivesim.underlay.random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import Model.RouterWaxman;
import Topology.Topology;
import edu.upc.cnds.collectives.Collectives;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.random.MersenneRandom;
import edu.upc.cnds.collectivesim.underlay.NetworkTopology;
import edu.upc.cnds.collectivesim.underlay.Grid2D.UnderlayModelException;
import graph.Graph;

/**
 * Generates an random topology based on the Waxman (1) algorithm, using the
 * implementation provided by Brite (2)
 * 
 * The main parameters for this generator are:
 * <ul>
 * <li> Number of nodes
 * <li> out-degree (outgoing links) of nodes. An out-degree of 1 (default) creates 
 *      a tree
 * <li> alpha, beta: parameters for the edge distribution probability function. Larger values of P result
 * in graphs with higher edge densities, while small values of a increase the density of short edges relative to longer
 * ones.
 * </ul> 
 * 
 * Even when Brite offers many other parameters, current implementation does not 
 * make them available for adjusting and fix them to some predefined values:
 * <ul>
 * <li> HS = 1000: Size of main plane (number of squares)
 * <li>	LS = 100: Size of inner planes (number of squares)
 * <li> NodePlacement = 1: node placement strategy (Random = 1, HeavyTailed = 2)
 * <li>	BWDist = 1: bandwidth distribution (Constant = 1, Uniform =2, HeavyTailed = 3, 
 *      Exponential =4)
 * <li>	BWMin = 1.0, BWMax = 1.0: min and max bandwidth (not considered)
 * </ul>.
 * 
 * 
 * Due to limitations in Brite's implementation, it is not possible to extend neither Underlay
 * node to also be a Graph node, nor the other way around. More over, it is not currently possible
 * to re-implement Graph to use UnderlayNode instead of Node, as Graph is not an interface but a Class
 * and instantiation of objects is hard-coded and not easily changed. 
 * 
 * Therefore, we need to keep a bidirectional mapping between them.  
 * 
 * 
 * 
 * (1) B. Waxman. Routing of Multipoint Connections. IEEE J. Select. Areas Commun., 
 *     December 1988.
 * (2)BRITE: An Approach to Universal Topology Generation. In Proceedings of the 
 *    International Workshop on Modeling, Analysis and Simulation of Computer and 
 *    Telecommunications Systems- MASCOTS'01, Cincinnati, 
 *    http://www.cs.bu.edu/brite/publications/BriteMascots.pdf
 *    
 * @author Pablo Chacin
 *
 */
public class WaxmanTopology implements NetworkTopology {

	private class TopologyLocation{
		
		/**
		 * Index of the location in the topology's graph
		 */
		private graph.Node node;
		
		/**
		 * Nodes located in this locaiton
		 */
		private Vector<UnderlayNode> nodes;

		TopologyLocation(graph.Node node){
			this.node = node;
			this.nodes = new Vector<UnderlayNode>();
		}
		
		
		public graph.Node getNode() {
			return node;
		}

		public Vector<UnderlayNode> getNodes() {
			return nodes;
		}
		
		
	}
	
	/**
	 * Brite's topology representation
	 */
	protected Topology networkTopology;
	
	/**
	 * Representation of the topology as a set of nodes and directed edges connecting them
	 */
	protected Graph graph;
	
	/**
	 * Maintains a mapping from Graph node's id to Underlay node's identifier
	 */
	protected List<TopologyLocation> locations;

	/**
	 * Maintains a mapping from Underlay node's identifier (an integer) to Graph node's id
	 */
	protected Map<UnderlayNode,TopologyLocation> nodes;

	
	/**
	 * Statistical model for generating the underlay topology
	 */	
	
	protected static int HS = 1000;
	
	protected static int LS = 100;
	
	protected static int STATIC_NODE_PLACEMENT = 1;
	
	protected static int INCREMENTAL_GROWTH = 1;
	
	protected static int FIXED_BANDWIDTH_DIST = 1;
	
	protected static double MIN_BANDWIDTH = 1.0;
	
	protected static double MAX_BANDWIDTH = 1.0;

	protected static double ALPHA = 0.4;
	
	protected static double BETA = 0.6;
	
	protected int nodePlacement = STATIC_NODE_PLACEMENT;
	
	protected int growth = INCREMENTAL_GROWTH;

	protected int bandwithDistribution = FIXED_BANDWIDTH_DIST;
	
	protected double minBandwidth = MIN_BANDWIDTH;
	
	protected double maxBandwidth = MAX_BANDWIDTH;
	
	protected int outDegree = 1;
	
	protected double alpha = ALPHA;
	
	protected double beta = BETA;
	
	protected int ls = LS;
	
	protected int hs = HS;
	
	protected  RouterWaxman model;
	
	protected int numLocations;
	
	protected Random rand;
	
	public WaxmanTopology(int numLocations,int outDegree) {
	
		 this.numLocations = numLocations;
		 
		 this.outDegree = outDegree;
		 
		 this.rand = Collectives.getExperiment().getRandomGenerator();
		 
		 this.locations = new ArrayList<TopologyLocation>(numLocations);
		 //locations.add(0,null);
		 
		 this.nodes = new HashMap<UnderlayNode,TopologyLocation>();
		 
	   	 this.model = new RouterWaxman(numLocations,HS,LS,nodePlacement,outDegree,alpha,beta,
	   			 growth,bandwithDistribution,minBandwidth,maxBandwidth);
	      	 
	     this.networkTopology = new Topology(model);
	          
	     this.graph = networkTopology.getGraph();
	}




	@Override
	public void generateTopology(List<? extends UnderlayNode> nodes) throws UnderlayModelException{
	
      
        //assign underlay nodes to graph nodes
        graph.Node[] graphNodes = graph.getNodesArray();
        
        for(int i=0;i<numLocations;i++){
        	TopologyLocation location = new TopologyLocation(graph.getNodeFromID(i));        	
        	locations.add(location);
        }
        
	}


	@Override
	public List<UnderlayNode> getKnownNodes(UnderlayNode node) {
		List<UnderlayNode> neighbors = new ArrayList<UnderlayNode>();
		
		//add other nodes in the same location
		TopologyLocation location = nodes.get(node);		
		neighbors.addAll(location.getNodes());
		neighbors.remove(node);
		
		//add nodes from neighbor locations
		for(graph.Node n: graph.getNeighborsOf(location.getNode())){
			neighbors.addAll(locations.get(n.getID()).getNodes());
		}
		
		
		return neighbors;
	}




	@Override
	public void addNode(UnderlayNode node) {
		
		TopologyLocation location = locations.get(rand.nextInt(numLocations));
		location.getNodes().add(node);		
		nodes.put(node, location);
	}





	@Override
	public void removeNode(UnderlayNode node) {
	
		TopologyLocation location = nodes.remove(node);
		
		if(location != null){
			location.getNodes().remove(node);
		}
	}




	@Override
	public void reset() {
		
		for(TopologyLocation l: locations){
			l.getNodes().clear();
		}
		
		nodes.clear();
	}


	
	
}

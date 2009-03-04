package edu.upc.cnds.collectivesim.underlay.random;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import Model.RouterWaxman;
import Topology.Topology;
import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.underlay.UnderlayException;
import edu.upc.cnds.collectives.underlay.UnderlayMetricType;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.Stream;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;
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
 *    Telecommunications Systems- MASCOTS�'01, Cincinnati, 
 *    http://www.cs.bu.edu/brite/publications/BriteMascots.pdf
 *    
 * @author Pablo Chacin
 *
 */
public class RandomUnderlayModel extends UnderlayModel {

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
	protected Map<UnderlayNode,graph.Node> underlayToGrap;

	/**
	 * Maintains a mapping from Underlay node's identifier (an integer) to Graph node's id
	 */
	protected Vector<UnderlayNode> graphToUnderlay;

	
	/**
	 * Statistical model for generating the underlay topologt
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
	
	/**
	 * Maintains a mapping of identifiers to Nodes
	 */
	protected Map<Identifier,graph.Node> nodeMap;
	
	public RandomUnderlayModel(Scheduler scheduler,Stream<Identifier> idStream,int numNodes) {
		super(scheduler,idStream,numNodes);
		
    	 model = new RouterWaxman(numNodes,HS,LS,nodePlacement,outDegree,alpha,beta,
    			 growth,bandwithDistribution,minBandwidth,maxBandwidth);
    	 

    	 graphToUnderlay = new Vector<UnderlayNode>(numNodes);
    	 
    	 underlayToGrap = new HashMap<UnderlayNode,graph.Node>(numNodes);
	}


	@Override
	public Metric[] probe(UnderlayNode source, UnderlayNode target,
			UnderlayMetricType[] metrics) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public UnderlayMetricType[] getSupportedMetrics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Node> resolve(InetAddress host) throws UnderlayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void generateNetworkTopology(List<? extends UnderlayNode> nodes) throws UnderlayModelException{
		
		networkTopology = new Topology(model);
       
        graph = networkTopology.getGraph();
        
        
        //assign underlay nodes to graph nodes
        graph.Node[] graphNodes = graph.getNodesArray();
        
        for(int i=0;i<numNodes;i++){
        	UnderlayNode node = nodes.get(i);
        	graphToUnderlay.add(i, node);
        	underlayToGrap.put(node,graphNodes[i]);        	
        }
        
	}


	@Override
	public List<Node> getKnownNodes(UnderlayNode node) {
		List<Node> neighbors = new ArrayList<Node>();
		
		graph.Node n = underlayToGrap.get(node);
		for(graph.Node neighbor : graph.getNeighborsOf(n)){
			neighbors.add(graphToUnderlay.get(neighbor.getID()));
		}
		
		return neighbors;
	}

}

package edu.upc.cnds.collectivesim.overlay.gradient;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingException;
import edu.upc.cnds.collectives.routing.RoutingHandler;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectives.util.ReflectionUtils;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.routing.kbr.RoutingModelEvent;

/**
 * Maintains a gradient topology. 
 * 
 * The updateTopology method contacts a randomly selected peer from the gradient topology and
 * gets its peer list, considering as candidates for the gradient topology.
 * 
 * The randomWalk method contacts a randomly selected peer from the random topology and gets its
 * list of peers, considering as candidates for the random topology and the gradient topology.
 * 
 * @author Pablo Chacin
 *
 */
public class GradientOverlayAgent extends OverlayAgent  implements RoutingHandler {

	protected Vector<Long>requests;
	
	protected String role; 
	
	
	protected Double tolerance;
	
	protected Integer ttl;
	
	public GradientOverlayAgent(OverlayModel model, Overlay overlay,
			Routing router,Topology randomTopology,String role,
			Double tolerance,Integer ttl) {
			
			super(model, overlay);
			
			if(overlay.getLocalNode().getId().toString().equals("0000000000000000")){
				this.role = "consumer";
			}
			else{
				this.role = role;
			}
			
			this.tolerance = tolerance;
			this.ttl = ttl;
			this.requests = new Vector<Long>();
		
	}
	
	
	
	
	public String getRole(){
		return role;
	}
	
	
	public void route(Double duration){
		route(getUtility(),tolerance,ttl,duration);
	}
	
	public void route(Double target, Double tolerance,
			Integer ttl,Object...args) {

		Map attributes = new HashMap();
		attributes.put("utility", target);
		Destination destination = new Destination(attributes,tolerance);
		try {
			overlay.route(destination, ttl);
		} catch (RoutingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void updateUtility(){
		Double utility = calculateUtility();
		overlay.getLocalNode().getAttributes().put("utility", utility);

	}
	
	/**
	 * Updates the node's utility
	 * @param variation
	 */
	public void updateUtility(Double variation){
		
		
		Double utility = (Double)overlay.getLocalNode().getAttributes().get("utility");
				
		if((utility == 1) && (variation > 0))
			variation = -1.0*variation;

		if((utility == 0) && (variation < 0))
			variation = -1.0*variation;

		//ensure that 0 <= utility+variation `<= 1
		if(variation < 0)
			utility = Math.max(0,utility+variation);
		else
			utility = Math.min(1, utility+variation);
		
				
		overlay.getLocalNode().getAttributes().put("utility", utility);

	}

	
	public Double getUtility(){
		return (Double)overlay.getLocalNode().getAttributes().get("utility");
	}

	/**
	 * 
	 * @return the average of the (absolute) difference between the node's utility 
	 *         and its peer's utility
	 */
	public Double getGradient(){

		
		Double utility = (Double)overlay.getLocalNode().getAttributes().get("utility");
		Double gradient = 0.0;
		for(Node n: overlay.getNodes()){
			//get the actual utility of the node,not the local value 
			GradientOverlayAgent agent = (GradientOverlayAgent)model.getAgent(n.getId().toString());
			
			Double otherUtility = (Double)agent.getUtility();
			Double difference = + Math.abs(utility-otherUtility);
			gradient += difference;
		}
		
		
		return gradient/(double)overlay.getSize();

	}


	@Override
	public void routed(Routing router, Node node, Destination destination,
			Route route, Serializable... args) {
		
		super.routed(router, node, destination, route, args);

		reportDelivered(overlay.getLocalNode(), this.getName(), destination, route);
		
		System.out.println("-------\nRequest received at " + overlay.getLocalNode().toString() + 
		                   " node attributes " + FormattingUtils.mapToString(overlay.getLocalNode().getAttributes()) +
		                   " destination " + FormattingUtils.mapToString(destination.getAttributes()) +
		                   " route " + route.toString() + 
		                   " num hops " + route.getNumHops());

	}

	
	/**
	 * Reports the delivery of a request on a node. The route includes the origin and 
	 * final target.
	 * 
	 * @param node
	 * @param route
	 * @param destination
	 */
	void reportDelivered(Node node, String protocol,Destination destination,Route route){

		Map attributes = new HashMap();
		attributes.put("source", route.getHops().get(0).toString());
		attributes.put("target",node.getId().toString());
		attributes.put("hops",route.getNumHops());
		attributes.put("protocol",protocol);
		for(Map.Entry< String, Object> e: destination.getAttributes().entrySet()){
			attributes.put("destination."+e.getKey().toString(), 
					     e.getValue().toString());
		}
		
		Event event = new RoutingModelEvent(node,model.getCurrentTime(),
											attributes,protocol,destination,route);
		
		model.getExperiment().reportEvent(event);
		
	}


	@Override
	public void deliver(Routing router, Destination destination,
			Serializable... args) {
		
		try {
			ReflectionUtils.invoke(this, "deliver", args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	protected void deliver(Routing router,Destination destination, Double utility, Long duration){
		requests.add(duration);
		
	}
	
	
	protected Double calculateUtility(){
		
		Collections.sort(requests);
		Iterator<Long> iter = requests.iterator();
		
		//remove all the expired requests
		while(iter.hasNext() && (iter.next() <= model.getCurrentTime())){
			iter.remove();
		}
		
		Double n = (double)requests.size();
		Double utility = 1.0/((n+1.0)*(n+1.0));
		return utility;
	}

}

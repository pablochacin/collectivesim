package edu.upc.cnds.collectivesim.overlay;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;


import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayHandler;
import edu.upc.cnds.collectives.overlay.epidemic.EpidemicOverlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.RouteObserver;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingEvent;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectives.topology.TopologyObserver;
import edu.upc.cnds.collectives.util.ReflectionUtils;
import edu.upc.cnds.collectives.util.UniqueArrayList;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.base.CompositeReflexionModelAgent;
import edu.upc.cnds.collectivesim.model.base.ReflexionModelAgent;
import edu.upc.cnds.collectivesim.state.Counter;

/**
 * Offers a view of the topology for a particular underlay Node.
 * 
 * @author Pablo Chacin
 *
 */
public class OverlayAgent extends ReflexionModelAgent implements TopologyObserver, RouteObserver,OverlayHandler {
	
	protected static Logger log = Logger.getLogger("colectivesim.overlay");
		
	protected OverlayModel model;
	
	protected Overlay overlay;
	
	protected Counter dropped;
	
	protected Counter routed;
	
	protected Counter delivered;
	
	protected Counter unreachable;
	
	protected Counter received;
	
	protected Counter undeliverable;
		
	protected Counter forwarded;
	
	protected boolean active = false;
	
	
	/**
	 * 
	 * @param model
	 * @param overlay
	 */
	public OverlayAgent(OverlayModel model,Overlay overlay,Identifier id,String[] attributes){
		
		super(id.toString(),attributes);
		
		this.model = model;
		this.overlay = overlay;
		this.overlay.addViewObserver(this);
		this.overlay.addObserver(this);
		
		this.dropped = model.getExperiment().getCounter("routing.dropped").getChild();
		this.routed = model.getExperiment().getCounter("routing.routed").getChild();
		this.delivered = model.getExperiment().getCounter("routing.delivered").getChild();
		this.forwarded= model.getExperiment().getCounter("routing.forwarded").getChild();
		this.unreachable= model.getExperiment().getCounter("routing.unreachable").getChild();
		this.undeliverable= model.getExperiment().getCounter("routing.undeliverable").getChild();		
		this.received= model.getExperiment().getCounter("routing.received").getChild();
		
	}
	
	
	public void init() {
		join();
	}
	
	/**
	 * Makes this agent join the overlay
	 */
	public void join(){
		active = true;
		overlay.join();
		model.nodeJoin(this);
	}
	
	
	/**
	 * Simulates the failure of the node
	 */
	public void leave(){
	
		overlay.getUnderlayNode().leave();
		model.nodeLeave(this);
		active = false;
	}
	
	@Override
	public void nodeAdded(Node node) {
		model.nodeLink(overlay.getLocalNode(),node);
		
	}

	@Override
	public void nodeRemoved(Node node) {
		model.nodeUnlink(overlay.getLocalNode(),node);; 
		
	}
	
	
	

	@Override
	public void forwarded(Routing router, Destination destination, Route route, Serializable message) {
		
		forwarded.increment();
		
		//TODO: why this is done?
		//route.getLastHop().touch(model.getCurrentTime());
	}

	@Override
	public void routed(Routing router, Destination destination,Route route, Serializable message) {
		
		routed.increment();
		
		
	}


	

	@Override
	public void dropped(Routing router, Destination destination,Route route,Exception cause, Serializable message) {
		
		dropped.increment();
				
	}



	@Override
	public void received(Routing router, Destination destination,Route route, Serializable message) {
		
		received.increment();
		
	}


	@Override
	public void undeliverable(Routing router, Destination destination, Route route, Exception cause, Serializable message) {
		
		undeliverable.increment();
	}


	@Override
	public void unreachable(Routing router, Destination destination, Route route,Serializable message) {
		unreachable.increment();
	}

	
	public Double getRouted(){
		return routed.getValue();
	}
	

	
	public Double getDropped(){
		return dropped.getValue();
	}
	
	public Double getDelivered() {
		return delivered.getValue();
	}

	
	public Double getReceived() {	
		return received.getValue();
	}

	
	public Double getForwarded(){
		return forwarded.getValue();
	}
	
	public Double getUndeliverable() {
		return undeliverable.getValue();

	}

	
	public Double getUnreachable(){
		return unreachable.getValue();
	}

	
	/**
	 * 
	 * @return the age of the elder node in the node's local overlay view.
	 */
	public Double getAge(){
	
		try{
			Vector<Long> ages = new Vector<Long>();
			for	(Node n: overlay.getNodes()){
				ages.add(model.getCurrentTime()-n.getKnowSince());
			}
		
		
			if(ages.isEmpty()){
				return Double.NaN;
			}
		
			Collections.sort(ages);
	
		
        	double pos = 0.5 * (ages.size() + 1);
        	double fpos = Math.floor(pos);
        	int intPos = (int) fpos;
        	double dif = pos - fpos;
        
        	double lower = ages.get(intPos - 1);
        	double upper = ages.get(intPos);
        	Double age = lower + dif* (upper - lower);
        
        	return age;
		}catch (Exception e){
        	return Double.NaN;
        }
	}


	@Override
	public boolean delivered(Routing router, Destination destination,Route route, Serializable message) {
				
		delivered.increment();
		
		Map attributes = new HashMap();

		for(Map.Entry<String,Object> e: route.getSource().getAttributes().entrySet()){
			attributes.put("source."+e.getKey().toString(), 
					     e.getValue().toString());
		}

		for(Map.Entry<String,Object> e: route.getLastHop().getAttributes().entrySet()){
			attributes.put("target."+e.getKey().toString(), 
					     e.getValue().toString());
		}

		attributes.put("route.hops",route.getNumHops());
		
		for(Map.Entry< String, Object> e: destination.getAttributes().entrySet()){
			attributes.put("destination."+e.getKey().toString(), 
					     e.getValue().toString());
		}
		
		
		attributes.put("agent",getName());
		
		attributes.put("timestamp",model.getCurrentTime());
		
		Event event = new RoutingEvent(overlay.getLocalNode(),model.getCurrentTime(),
											attributes);
		
		model.getExperiment().reportEvent(event);


		return true;
	}
	
	/**
	 * Time the current reference locality window begins.
	 */
	protected long localityWindowBegin = 0;
	
	protected List<Node> pastReferenced = new ArrayList<Node>();
	
	protected Double lastLocality = 0.0;
	
	/**
	 * Calculates the locality of the references to the nodes of the overlay,
	 * measured as the ratio of nodes referenced across consecutive windows
	 * with respect of the total number of nodes references in the last window.
	 * 
	 * @return
	 */
	public Double getLocality(){
		
		//size of the working set
		Double locality = 0.0;
		
		//list of nodes referenced in this window
		List<Node> referenced = new ArrayList<Node>();
		
		for(Node n: overlay.getNodes()){
			if(n.getLastTouch() > localityWindowBegin){
				referenced.add(n);
			}
		}

		localityWindowBegin = model.getCurrentTime();
 
		if(referenced.isEmpty()){
			return lastLocality;
		}
		
		List<Node> workingset = new ArrayList<Node>(referenced);
		workingset.retainAll(pastReferenced);
		
		locality = (double)workingset.size()/(double)referenced.size();
		
		lastLocality = locality;
		pastReferenced = referenced;
		
		return locality;
	}
	
	
	public Double getClusteringCoeficient(){
	
		Double numInterLinks = 0.0;
		
		List<OverlayAgent> neighbors = getActiveNeighbors();
		
		for(OverlayAgent n: neighbors){
						
			List<Node> interLinks = n.getOverlay().getNodes();
			interLinks.retainAll(overlay.getNodes());
			
			numInterLinks += (double)interLinks.size();
			
		}
		
		Double coeficient = numInterLinks/((double)(overlay.getMaxSize()*(overlay.getMaxSize()-1)));
		
		return coeficient;
	}
	
	
	protected Double indegree = 0.0;
	
	public Double getInDegree(){
		
		return indegree;
	}
	
	


		
	void incIndegree(){
		indegree++;
	}
	
	void decIndegree(){
		indegree--;
	}
	
	protected Overlay getOverlay(){
		return overlay;
	}
	
	
	List<Node> acquiances = new UniqueArrayList<Node>();
	
	public void update(){
		((EpidemicOverlay)overlay).update();
		acquiances.addAll(overlay.getNodes());
	}
	
	public Double getLocallity(){

		return (double)overlay.getTopology().getMaxSize()/(double)acquiances.size();
	}
	
	
	Double intervalCenter = new Random().nextDouble();
	Double intervalSize = 0.03125;
	
	public Double getNetworkSize(){
		Double count = 0.0;
		
		for(Node n: acquiances){
			Double id = n.getId().toDouble();
			if( (Math.abs(id -intervalCenter) <= intervalSize/2.0) ){
				count++;
			}
		}

		return count/intervalSize;
	}
	
	Vector<Double> pastNetworkSizeStimates = new Vector<Double>();
	Vector<Double> pastIntervalCenters = new Vector<Double>();
	
	public void updateNetworkSizeStimate(){
		pastNetworkSizeStimates.add(getNetworkSize());
		if(pastNetworkSizeStimates.size() > 8){
			pastNetworkSizeStimates.remove(0);
		}
		
		pastIntervalCenters.add(intervalCenter);
		if(pastIntervalCenters.size() > 8){
			pastIntervalCenters.remove(0);
		}
		
		Double avgSize = 0.0;
		for(Double s: pastNetworkSizeStimates){
			avgSize += s;
		}
		avgSize = avgSize/pastNetworkSizeStimates.size();
		
		Double minError = Double.MAX_VALUE;
		int minErrorCenter = 0;
		
		for(int i=0; i < pastNetworkSizeStimates.size();i++){
			if(Math.abs(pastNetworkSizeStimates.get(i)-avgSize) < minError){
				minErrorCenter = i;
			}
		}
		Double sign = new Random().nextGaussian()< 0.0?-1.0:1.0;
		
		intervalCenter = pastIntervalCenters.get(minErrorCenter) + sign*1.0/Math.pow(2, 64);
	}
	
	
	/**
	 * Returns the list of active neighbors, excluding from the overlay those that are not
	 * active.
	 * 
	 * @return a List of active OverlayNodes in the neighborhood
	 */
	protected List<OverlayAgent> getActiveNeighbors(){
		
		List<OverlayAgent>activeNeighbors = new ArrayList<OverlayAgent>();
		
		for(Node n: overlay.getNodes()){
			OverlayAgent agent = model.getAgent(n.getId().toString()); 
			if(agent != null){
				activeNeighbors.add(agent);
			}
			
		}
		
		return activeNeighbors;
	}
	
	public Double getOutDegree(){
		return (double)getActiveNeighbors().size();
	}

	
	@Override
	public String[] getAttributeNames(){
		return attributeNames;		
	}

	@Override
	public Map<String, Object> getAttributes() {
	 
		return inquire(getAttributeNames());
	}


	public Map<String, Object> getAttributes(String[] attributes) {
		 
		return inquire(attributes);
	}
	
	@Override
	public Object getAttribute(String attribute) {
		try {
			return inquire(attribute);
		} catch (ModelException e) {
			return null;
		}
	}
	
	
	public List<Node> getNeighbors(){
		return getOverlay().getNodes();
	}
	
	public Node getLocalNode(){
		return getOverlay().getLocalNode().getReference();
	}
}



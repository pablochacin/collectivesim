package edu.upc.cnds.collectivesim.overlay;

import java.io.Serializable;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.routing.Destination;
import edu.upc.cnds.collectives.routing.RouteObserver;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.routing.RoutingException;
import edu.upc.cnds.collectives.routing.RoutingHandler;
import edu.upc.cnds.collectives.routing.base.Route;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.ViewObserver;
import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.model.base.CompositeReflexionModelAgent;
import edu.upc.cnds.collectivesim.state.Counter;

/**
 * Offers a view of the topology for a particular underlay Node.
 * 
 * @author Pablo Chacin
 *
 */
public class OverlayAgent extends CompositeReflexionModelAgent implements ViewObserver, RouteObserver {
	
	protected static Logger log = Logger.getLogger("colectivesim.topology");
		
	protected OverlayModel model;
	
	protected Overlay overlay;
		
	
	/**
	 * 
	 * @param model
	 * @param overlay
	 */
	public OverlayAgent(OverlayModel model,Overlay overlay){
		
		super(overlay.getLocalNode().getId().toString(),overlay);
		
		this.model = model;
		this.overlay = overlay;
		this.overlay.addViewObserver(this);
		this.overlay.addRouteObserver(this);

		
	}
	
		
	@Override
	public void join(Node node) {
		model.nodeJoin(overlay.getLocalNode(),node);
		
	}

	@Override
	public void leave(Node node) {
		model.nodeLeave(overlay.getLocalNode(),node);
		
	}
	

	@Override
	public void forwarded(Routing router, Node node,
			Destination destination, Route route, Serializable... args) {
		
	}

	@Override
	public void routed(Routing router, Node node, Destination destination,
			Route route, Serializable... args) {
		
		routed++;
		
	}


	@Override
	public void dropped(Routing router, Destination destination,
			Exception cause, Serializable... args) {
		
		dropped++;
		
	}




	@Override
	public void received(Routing router, Destination destination,
			Node source, Serializable... args) {
		
		received++;
		
	}


	@Override
	public void undeliverable(Routing router, Destination destination,
			Node node, Exception cause, Serializable... args) {
		
		undeliverable++;
	}


	@Override
	public void unreachable(Routing router, Destination destination,
			Serializable... args) {
		unreachable++;
	}


	protected Double routed = 0.0;
	
	public Double getRouted(){
		return routed;
	}
	
	protected Double dropped = 0.0;
	
	public Double getDropped(){
		return dropped;
	}
	
	protected Double delivered =0.0;

	public Double getDelivered() {
		return delivered;
	}

	protected Double propagated = 0.0;

	public Double getPropagated() {
		return propagated;
	}

	protected Double received = 0.0;
	
	public Double getReceived() {	
		return received;
	}

	protected Double undeliverable;
	
	public Double getUndeliverable() {
		return undeliverable;

	}

	protected Double unreachable;
	
	public Double getUnreachable(){
		return unreachable;
	}

	
}

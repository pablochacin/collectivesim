package edu.upc.cnds.collectivesim.overlay.gradient;

import java.util.List;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.gradient.GradientOverlay;
import edu.upc.cnds.collectives.routing.Routing;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;


/**
 * Maintains a gradient topology. 
 * 
 * @author Pablo Chacin
 *
 */
public class GradientOverlayAgent extends OverlayAgent  {

	
	protected Double utility;
	
	public GradientOverlayAgent(OverlayModel model, Overlay overlay,
			Routing router,Topology randomTopology,Double utility) {
			
			super(model, overlay);
			
			setUtility(utility);
			
	}
	
	
	/**
	 * Sets the utility of this node to a new value.
	 * Updates the Overlay node's "utility" attribute.
	 * 
	 * @param utility Double with the new utility value
	 */
	public void setUtility(Double utility){
		
		
		this.utility = utility;
		
		overlay.getLocalNode().getAttributes().put("utility", utility);
		overlay.getLocalNode().touch(model.getCurrentTime());

	}
	
	/**
	 * Updates the node's utility with a variation (negative or positive) from 
	 * the current utility value.
	 * 
	 * Ensures that the resulting value is in the [0.0,1.0] range.
	 * 
	 * 
	 * @param variation a Double to be added to the current utility. 
	 */
	public void updateUtility(Double variation){
					
		Double newUtility;
		
		if((utility == 1) && (variation > 0))
			variation = -1.0*variation;

		if((utility == 0) && (variation < 0))
			variation = -1.0*variation;

		//ensure that 0 <= utility+variation `<= 1
		if(variation < 0)
			newUtility = Math.max(0,utility+variation);
		else
			newUtility = Math.min(1, utility+variation);
		
				
		setUtility(newUtility);
	}

	
	
	/**
	 * 
	 * @return the average of the (absolute) difference between the node's utility 
	 *         and its peer's utility
	 */
	public Double getGradient(List<Node>neighbors){

		
		Double gradient = 0.0;
		for(Node n: neighbors){
			//get the actual utility of the node,not the local value 
			GradientOverlayAgent neighbor = (GradientOverlayAgent)model.getAgent(n.getId().toString());
			
			Double difference = + Math.abs(getUtility()-neighbor.getUtility());
			gradient += difference;
		}
				
		return gradient/(double)neighbors.size();

	}

	public Double getGradient(){
		return getGradient(overlay.getTopology().getNodes());
	}

	
	public Double getRandomGradient(){
		List<Node> randomNeighbors = ((GradientOverlay)overlay).getRandomTopology().getNodes();
		return  getGradient(randomNeighbors);
	}
	
	public Double getUtility(){
		return utility;
	}

	



	
	
	
}

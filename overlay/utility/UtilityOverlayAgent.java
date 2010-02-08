package edu.upc.cnds.collectivesim.overlay.utility;

import java.util.List;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;


/**
 * Maintains a gradient topology. 
 * 
 * @author Pablo Chacin
 *
 */
public class UtilityOverlayAgent extends OverlayAgent  {

	
	protected Double utility;
	
	public UtilityOverlayAgent(OverlayModel model, Overlay overlay,Identifier id,Double utility) {
			
			super(model, overlay,id);
			
			this.utility = utility;
			
			if(utility == null){
				throw new IllegalArgumentException("Utility attribute not specified");
			}
					
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
		//overlay.getLocalNode().touch(model.getCurrentTime());

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

	Double direction = 1.0;
	public void updateUtility(Double variation,Double direction){
		this.direction = this.direction*direction;
		updateUtility(variation*this.direction);
	}
	
	
	
	public void setDirection(Double direction){
		this.direction = direction;
	}
	
	/**
	 * 
	 * @return the average of the (absolute) difference between the node's utility 
	 *         and its peer's utility
	 */
	public Double getGradient(List<OverlayAgent>neighbors){

				Double gradient = 0.0;
		for(OverlayAgent n: neighbors){
			
			Double difference = + Math.abs(getUtility()-((UtilityOverlayAgent)n).getUtility());
			gradient += difference;
		}
				
		return gradient/(double)neighbors.size();

	}

	public Double getGradient(){
		return getGradient(getActiveNeighbors());
	}


	
	/**
	 * 
	 * @return the average of the (absolute) difference between the node's utility 
	 *         and its peer's utility
	 */
	public Double getGradientError(){

		
		Double error = 0.0;
		for(Node n: overlay.getNodes()){

			//get the actual utility of the node,not the local value 
			UtilityOverlayAgent neighbor = (UtilityOverlayAgent)model.getAgent(n.getId().toString());
			if(neighbor != null){
				Double neighborUtility = (Double)n.getAttributes().get("utility");
				Double difference = + Math.abs(neighborUtility-neighbor.getUtility());
				error+= difference;
			}
		}
				
		return error/(double)overlay.getTopology().getSize();

	}

	


	
	public Double getUtility(){

		return utility;
		
	}

	



	
	
	
}

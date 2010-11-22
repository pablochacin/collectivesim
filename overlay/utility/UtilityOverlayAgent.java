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

	
	protected UtilityFunction function;
	
	protected Double utility;
	
	public UtilityOverlayAgent(Overlay overlay,UtilityFunction function) {
			
			super(overlay);
			
			this.function = function;
						
			setUtility(function.getUtility(overlay.getLocalNode()));
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
	public void updateUtility(){
		setUtility(function.getUtility(overlay.getLocalNode()));
	}
		
	
	protected void setUtility(Double utility) {
		this.utility = utility;
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
				Double neighborUtility = (Double)n.getAttributes().get("Utility");
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

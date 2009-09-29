package edu.upc.cnds.collectivesim.overlay.utility;

import java.util.Map;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.underlay.UnderlayModel;

/**
 * 
 * Factory for GradientTopologyAgents that maintains a gradient topology.
 * 
 * @author Pablo Chacin
 *
 */
public class UtilityOverlayModel extends OverlayModel{

			

	/**
	 * Constructor
	 * @param name name of the model
	 * @param experiment Experiment on which this model resides
	 * @param underlay UnderlayModel that gives access to underlay nodes
	 * @param factory OverlayFactory used to create overlay nodes 
	 */
	public UtilityOverlayModel(String name,Experiment experiment,UnderlayModel underlay, 
			                    OverlayFactory factory,Stream ... attributes) {
		
		super(name,experiment,underlay,factory,attributes);
	}

	
	/**
	 * Creates an OverlayAgent from the overlay components
	 * 
	 * @param overlay
	 * @param router
	 * @param randomTopology
	 * @return
	 * @throws ModelException 
	 */
	@Override
	protected OverlayAgent createOverlayAgent(Overlay overlay,Map attributes) throws ModelException{
		
		
		return new UtilityOverlayAgent(this,overlay,attributes);

	}
	





}

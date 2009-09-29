package edu.upc.cnds.collectivesim.overlay;

import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.underlay.UnderlayNode;

/**
 * Provides the logic to create and initialize an Overlay node for a
 * UnderlayNode.  
 * 
 * @author Pablo Chacin
 *
 */
public interface OverlayFactory {

	/**
	 * Returns an overlay for the given UnderlayNode
	 * 
	 * @param node
	 * @return
	 */
	public Overlay getOverlay(UnderlayNode node);
	
}

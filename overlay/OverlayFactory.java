package edu.upc.cnds.collectivesim.overlay;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.overlay.OverlayException;
import edu.upc.cnds.collectives.overlay.OverlayHandler;


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
	 * @throws OverlayException 
	 */
	public Overlay getOverlay(Identifier id,OverlayHandler handler) throws OverlayException;
	
	/**
	 * Convenience method, without OverlayHandler, which may be set later directly on the OverlayNode
	 * @param id
	 * @return
	 * @throws OverlayException
	 */
	public Overlay getOverlay(Identifier id) throws OverlayException;
}

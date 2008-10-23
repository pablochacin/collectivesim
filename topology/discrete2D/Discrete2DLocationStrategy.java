package edu.upc.cnds.collectivesim.topology.discrete2D;

import edu.upc.cnds.collectivesim.agents.Agent;
import uchicago.src.sim.space.Discrete2DSpace;

public interface Discrete2DLocationStrategy {
	
	/**
	 * 
	 * @return 
	 */
	public boolean locate(Discrete2DLocation location);

	/**
	 * Sets the realm to which the location must be accomplished
	 */
	public void setRealm(Discrete2DRealm realm);
    
	/**
     * Resets the location strategy's state, if any 
	 */
    public void reset();
}

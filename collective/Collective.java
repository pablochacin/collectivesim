package edu.upc.cnds.collectivesim.collective;



import java.util.Map;

import edu.upc.cnds.collectivesim.agents.Agent;

/**
 * Representes a group of agents that interact to achieve an objective
 * 
 * @author Pablo Chacin
 *
 */
public interface Collective {

	/**
	 * Adds an agent to the Collective.
	 * 
	 * @return a boolean indicating if the agent could be
	 *         added to the Collective
	 */
	public void addAgent(Agent agent);
    
    /**
     * Visit all agents on the Realm, executing a given method
     */
    public void visit(String method,Object[] arguments);
        
    
    /**
     * Inquires an attribute from all agents in the realm
     * 
     * @param attribute a String with the name of the attribute to inquire
     * 
     * @return an object with the result of the inquire 
     */
    public Object handelInquire(String attribute);
    
    
    /**
     * Inquires an attribute from all agents in the realm
     * 
     * @param attributes an array of Strings with the name of the attributes 
     *        to inquire
     * 
     * @return a Map with the result of the inquire 
     */
    public Map inquire(String[] attributes);
}

package edu.upc.cnds.collectivesim.collective;



import edu.upc.cnds.collectivesim.agents.Agent;

/**
 * Representes a group of agents that interact to achieve an objective
 * 
 * @author Pablo Chacin
 *
 */
public interface Collective {

	/**
	 * Adds an agent to the Realm.
	 * 
	 * @return a boolean indicating if the agent could be
	 *         added to the realm
	 */
	public boolean addAgent(Agent agent);

	
	/**
     * Ads an observer to the ralm
     * 
     * @param obsever a RealmObserver which must observe the realm
     * @param frequency a double that specifies the frequency of 
     *        observation 
	 */
    void addObserver(CollectiveObserver observer, double frequency);
    
        
   
   /**
     * Inquires an attribute from all agents in the realm
     * 
     * @param attribute a String with the name of the attribute to inquire
     * 
     * @return an object with the result of the inquire 
     */
    public Object inquire(String attribute);
    
    
    /**
     * Visit all agents on the Realm, executing a given method
     */
    public void visit(String method);
    
    
    /**
     * Visit all agents on the Realm, executin for each of them
     * a series of methods.
     */
    public void visit(String[] methods);
    
    
}

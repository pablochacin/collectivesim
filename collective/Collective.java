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
     * Visit all agents on the Realm, executing a given method
     */
    public void visit(String method,Object[] arguments);
            
    
    /**
     * Inquires an attribute from all agents in the collective
     * 
     * @param attribute a Strings with the name of the attribute
     *        to inquire
     * 
     * @return a Map with the result of the inquire 
     */
    public Object inquire(String attributes);
    
    
    /**
     * Registers an object as handler of an action. The object must implement a method
     * with the same name of the action
     * 
     * @param action name of the action
     * @param target object tha will handle the request for the action

     */
    public void registerAction(String action, Object target);
    
    /**
     * Registers an object as handler of an specific attribute
     * 
     * @param attribute
     */
    public void registerAttribute(String attribute,Object target);
}

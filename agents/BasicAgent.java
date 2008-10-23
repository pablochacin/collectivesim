package edu.upc.cnds.collectivesim.agents;

import java.lang.reflect.Method;

import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.collective.Collective;
import edu.upc.cnds.collectivesim.models.Model;

public class BasicAgent implements Agent {

	protected Identifier id;
	
	protected Model model;
	
	protected Node node;
	
	/**
	 * Default constructor withour arguments
	 *
	 */
	public BasicAgent() {
		
	}
	
	/**
	 * Constructor with full arguments
	 * @param id Identifier of the agent
	 * @param model the Model from which the agent can retrieve simulation information
	 * @param node the Node on which the agent resides
	 */
	public BasicAgent(Identifier id, Model model, Node node) {
		this.id = id;
		this.model = model;
		this.node = node;
	}
	
    public String getAgentType(){
        return this.getClass().getName();
    }

    public Object getAttribute(String attribute) throws AgentException{
               Method method;
            Object result = null;
            try {
                method = this.getClass().getMethod("get"+attribute, null);
                result = method.invoke(this, null);
            } catch (Exception e) {
            	throw new AgentException("Exception accessing attribute "+attribute,e);
            } 
            
            return result;
        }


    public Object execute(Collective collective, String methodName) throws AgentException {
    	return execute(collective,methodName,null);
    }
    
    
    public Object execute(Collective collective, String methodName,Object[] arguments) throws AgentException {
        
        try {
            Object[] arg = {collective};
            
            Method method = this.getClass().getMethod(methodName, Collective.class);
            
            return method.invoke(this, arg);
            
        } catch (Exception e) {
        	throw new AgentException("Exception accessing executing method " + methodName,e);
        } 
       
    }

    /**
     * Returns the agent's hashCode as an Integer object to be used for
     * identification
     */
    
	public Identifier getId() {
		 return id;
	}

	public Model getModel() {
		return model;
	}

	public Node getNode() {
		return node;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void setNode(Node node) {
	
		this.node = node;
	}
}

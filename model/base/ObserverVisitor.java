package edu.upc.cnds.collectivesim.model.base;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * Observes the Collective and calculates an attribute from applying an operator
 * to the attribute on all agents (e.g. MAX, MIN)
 *  
 * @author Pablo Chacin
 *
 */
public class ObserverVisitor extends AgentVisitor{

	private static Logger logger = Logger.getLogger("collectivesim.model");


	/**
	 * Agent attribute being observed
	 */
	String[] attributes;

	protected DataSeries values;

	private boolean reset;
	
	Double max = Double.MIN_VALUE;

	/**
	 * Default constructor
	 */
	public ObserverVisitor(Model model,String name,AgentSampler sampler,String[] attributes,DataSeries values,boolean reset,int iterations,Stream<Long> frequency, long delay, long endTime,int priority){
		super(model,name,sampler,true,iterations,frequency,delay,endTime,priority);
		this.attributes = attributes;
		this.values = values;
		this.reset= reset;
	}


	
	protected void startVisit(){
		if(reset){
			values.reset();
		}
		
		
		
	}

	/**
	 * 
	 * Generates a DataItem in the values DataSeries from the attributes of an Agent.
	 * 
	 * Returns true if the visit must continue, false otherwise (e.g. an error)
	 */
	@Override
	protected boolean visit(ModelAgent agent) throws ModelException{


		Map<String,Object>agentAttributes = new HashMap<String,Object>();
		for(String attribute: attributes){
			try {
				agentAttributes.put(attribute,agent.getAttribute(attribute));
				
			} catch (ModelException e) {
				log.warning("Exception accessing attribute [" + attribute + "] from observer" + name + 
						    "\n" + FormattingUtils.getStackTrace(e));
				this.pause();
				return false;
			}
		}
		
				
		return processAttributes(agentAttributes);
		
		

	}

	/**
	 * 
	 * @param attributes
	 */
	protected boolean processAttributes(Map agentAttributes){

		values.addItem(agentAttributes);

		return true;
	}
}

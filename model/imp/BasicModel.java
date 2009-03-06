package edu.upc.cnds.collectivesim.model.imp;

import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;

/**
 * A basic model that does not support any modeling functionality
 * and allows any class to be added as an agent. 
 * 
 * @author Pablo Chacin
 *
 */
public class BasicModel extends AbstractModel {

	public BasicModel(String name,Experiment experiment) {
		super(name,experiment);

	}

	
	/**
	 * Adds an object by wrapping it as an ModelAgent.
	 * @param type
	 * @param target
	 */
	public void addAgent(String type,Object target){
		super.addAgent(new ReflexionModelAgent(type,target));
	}	
	
	public void addAgent(ModelAgent agent){
		super.addAgent(agent);
	}
}

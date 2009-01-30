package edu.upc.cnds.collectivesim.model.imp;

import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;

/**
 * Executes an method on an specific agent
 * 
 * @author Pablo Chacin
 *
 */
public class EventAction implements Runnable {

	private static Logger log = Logger.getLogger("collectivesim.model");
	private ModelAgent agent;
		
	private String method;
	
	private Object[] args;
			
	EventAction(ModelAgent agent, String method, Object[] args) {
		this.agent = agent;
		this.method = method;
		this.args = args;
	}


	public void run() {
		try {
			agent.execute(method, args);
		} catch (ModelException e) {

		}
	}
		
}

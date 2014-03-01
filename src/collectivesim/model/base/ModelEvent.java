package collectivesim.model.base;

import java.util.logging.Logger;

import collectivesim.model.ModelAgent;
import collectivesim.model.ModelException;

/**
 * Executes an method on an specific agent
 * 
 * @author Pablo Chacin
 *
 */
public class ModelEvent implements Runnable {

	private static Logger log = Logger.getLogger("collectivesim.model");
	private ModelAgent agent;
		
	private String method;
	
	private Object[] args;
			
	public ModelEvent(ModelAgent agent, String method, Object[] args) {
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

package edu.upc.cnds.collectivesim.model.base;

import java.util.logging.Logger;

import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.model.AgentFactory;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.stream.Stream;

public class AgentStream extends ModelAction {


	private static final String MODEL_ACTION_TYPE = "Agent Stream";

	protected AgentFactory factory;

	protected Stream<Integer>rate;

	public AgentStream(BasicModel model,String name,AgentFactory factory, Stream<Integer>rate, 
			boolean active, Stream<Long> frequency, long delay, long endTime) {
		super(model, name, active, 0, frequency, delay, endTime,0);
		this.model = model;
		this.factory=factory;
		this.rate = rate;


	}

	@Override
	protected void execute() {

		int numAgents = rate.nextElement();


		for(int i=0;i< numAgents;i++){
			try {
				ModelAgent agent;

				agent = model.createAgent(factory);

			} catch (ModelException e) {
				Logger.getLogger("collectivesim.model").warning("Exception creating agent " + 
						FormattingUtils.getStackTrace(e));
			}
		}

	}

	protected String getType(){
		return MODEL_ACTION_TYPE;
		
	}
}

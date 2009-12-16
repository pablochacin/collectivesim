package edu.upc.cnds.collectivesim.model.base;

import java.util.logging.Logger;

import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectivesim.model.AgentFactory;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.stream.Stream;

public class AgentStream extends ModelAction {

	protected BasicModel model;

	protected AgentFactory factory;

	protected Stream[] argStreams;

	protected Stream<Integer>rate;

	public AgentStream(BasicModel model,AgentFactory factory, Stream<Integer>rate,Stream[] argStreams, 
			boolean active, Stream<Long> frequency, long delay, long endTime) {
		super(active, 0, frequency, delay, endTime);
		this.model = model;
		this.factory=factory;
		this.argStreams = argStreams;
		this.rate = rate;


	}

	@Override
	protected void execute() {

		int numAgents = rate.nextElement();


		for(int i=0;i< numAgents;i++){
			try {
				ModelAgent agent;

				agent = model.createAgent(factory, argStreams);

			} catch (ModelException e) {
				Logger.getLogger("collectivesim.model").warning("Exception creating agent " + 
						FormattingUtils.getStackTrace(e));
			}
		}

	}

}

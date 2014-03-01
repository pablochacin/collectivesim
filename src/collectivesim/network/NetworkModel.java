package collectivesim.network;

import java.util.List;

import collectivesim.model.Model;
import collectivesim.model.ModelAgent;

/**
 * A model that maintains a series of network between its agents
 * 
 * @author Pablo Chacin
 *
 */
public interface NetworkModel extends Model {

	/**
	 * Returns a list of agents that are related to the given agent 
	 * @param agent
	 * @return
	 */
	List<ModelAgent> getNeighbors(String network,ModelAgent agent);
}

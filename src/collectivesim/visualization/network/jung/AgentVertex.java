package collectivesim.visualization.network.jung;

import edu.uci.ics.jung.graph.impl.SparseVertex;
import collectivesim.model.ModelAgent;
import collectivesim.model.ModelException;

/**
 * Extends the SparseVertex class to give it access to node's attributes.
 * 
 * For compatibility with Jung framework and to prevent node's attributes to be
 * changed from the graph, this class doesn't implement the 
 * {@link #setUserDatum(Object, Object, edu.uci.ics.jung.utils.UserDataContainer.CopyAction)} method
 * and the {@link #getUserDatum(Object)} method returns a node attribute only if any matches the key, 
 * otherwise delegates to its super class. 
 * 
 * @author Pablo Chacin
 *
 */
public class AgentVertex extends SparseVertex {

	private ModelAgent agent;
	
	public AgentVertex(ModelAgent node){
		this.agent = node;
	}

	@Override
	public Object getUserDatum(Object key) {
		
		try {
			return agent.inquire((String)key);
		} catch (ModelException e) {
			return super.getUserDatum(key);
		}
	}
	
	
	
}

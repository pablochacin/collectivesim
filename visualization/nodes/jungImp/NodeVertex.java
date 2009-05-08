package edu.upc.cnds.collectivesim.visualization.nodes.jungImp;

import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.upc.cnds.collectives.node.Node;

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
public class NodeVertex extends SparseVertex {

	private Node node;
	
	public NodeVertex(Node node){
		this.node = node;
	}

	@Override
	public Object getUserDatum(Object key) {
		Object datum = node.getAttributes().get(key) ;
		if(datum != null)
			return datum;
		else
			return super.getUserDatum(key);
	}
	
	
	
}

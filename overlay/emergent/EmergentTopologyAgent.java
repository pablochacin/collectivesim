package edu.upc.cnds.collectivesim.overlay.emergent;


import java.util.List;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.node.NodeSelector;
import edu.upc.cnds.collectives.node.base.NodeAttributeComparator;
import edu.upc.cnds.collectives.node.base.OrderedSelector;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.topology.base.BasicTopology;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.ordered.OrderedTopologyAgent;

/**
 * 
 * Created and Emergent Topology in two steps: first, creates a ring of nodes based on the
 * id distance. Then, adapt the topology using an adaptation sampler.
 * 
 * @author Pablo Chacin
 *
 */
public class EmergentTopologyAgent extends OrderedTopologyAgent {

	
	/**
	 * Size of the topology's view in the adaptation phase
	 */
	protected int adaptationViewSize;
	
	/**
	 * Node attribute used to adapt the topology
	 */
	protected String adaptationAttribute;
			
	public EmergentTopologyAgent(OverlayModel model, Topology topology,
								 NodeSelector selector,int viewSize,
								 String adaptationAttribute,int adaptationViewSize) {
		super(model, topology,selector,viewSize);
		this.adaptationAttribute = adaptationAttribute;
		this.adaptationViewSize = adaptationViewSize;

	}
	
	
	/**
	 * Initiates the adaptation phase of the topology
	 * 
	 * TODO: this code need to downcast several times. This reflects a design problem as the
	 *       code depends of some particular implementation of the Topology (BasicTopology) 
	 *       and the NodeSelector it uses
	 *       Maybe a better approach could be to just create a new topology node altogether!
	 */
	public void initAdaptation(){
		topology.setSize(this.adaptationViewSize);
		OrderedSelector selector = (OrderedSelector) ((BasicTopology)topology).getSelector();
		NodeAttributeComparator comparator = (NodeAttributeComparator)selector.getComparator();
		selector.setCompatator(new NodeAttributeComparator(adaptationAttribute));
	}
	
	/**
	 * Updates the topology to adapt to the application requirements.
	 * Nodes are already selected by the routing algorithm. Call update
	 * to filter them.
	 */
	public void adaptTopology(){
		topology.update();
	}
	
}

package edu.upc.cnds.collectivesim.protocol;

import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.protocol.Protocol;
import edu.upc.cnds.collectives.routing.kbr.KbrProtocolImp;
import edu.upc.cnds.collectives.routing.kbr.KeyDistanceMatchFunction;
import edu.upc.cnds.collectives.topology.Topology;
import edu.upc.cnds.collectives.transport.Transport;
import edu.upc.cnds.collectivesim.experiment.Experiment;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.imp.AbstractModel;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.topology.TopologyModel;
import edu.upc.cnds.collectivesim.transport.TransportModel;

/**
 * A simulation model of a Protocol. It handles the request from all nodes
 * and simulates the protocol's propagation rules using a simulated transport.
 * 
 * The ProtocolModel is called from the UnderlayNodeModel passing the node that
 * made the request. 
 * 
 * Tipically, the ProtocolModel will use the TopologyModel to obtain information about the
 * topology that communicates the nodes (for example, to know a node's neighbors). 
 * 
 * @author Pablo Chacin
 *
 */
public abstract class ProtocolModel  extends AbstractModel{
	
	private TransportModel transport;
	
	private TopologyModel topology;
	;
	
	public ProtocolModel(String name,Experiment experiment,TopologyModel topology, TransportModel transport) {
		super(name,experiment);
		this.topology = topology;
		this.transport = transport;
	}

	

	
	@Override
	protected void populate() throws ModelException {
		installProtocol();	
	}




	/**
	 * Installs the protocol on each node
	 * @param protocol
	 */
	public void installProtocol(){
		
		for(Topology t: topology.getTopologies()){
			ProtocolModelAgent agent = installProtocol(name,t,transport.getTransport(t.getLocalNode()));
			addAgent(agent);
		}
	}
	


	/**
	 * 
	 * @param node
	 * 
	 * @return a ProtocolAgent for the given overlay node
	 */
	public abstract ProtocolModelAgent installProtocol(String name,Topology topology,Transport transport);
	

}

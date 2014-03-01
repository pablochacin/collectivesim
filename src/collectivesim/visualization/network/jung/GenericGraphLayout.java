package collectivesim.visualization.network.jung;

import java.awt.Dimension;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.visualization.AbstractLayout;
import edu.uci.ics.jung.visualization.Coordinates;
import edu.uci.ics.jung.visualization.LayoutMutable;
import edu.uci.ics.jung.visualization.VertexLocationFunction;

public class GenericGraphLayout extends AbstractLayout implements LayoutMutable {

	

	public GenericGraphLayout(VertexLocationFunction locationFunction, Graph g) {
		super(g);
		super.vertex_locations =locationFunction;
	}



	public boolean isIncremental() {
		return false;
	}



	/**
	 * Initializer, calls <tt>intialize_local</tt> and <tt>initializeLocations</tt>
	 * to start construction process.
	 */
	public void initialize(Dimension size) 
	{
		initialize(size, super.vertex_locations);
	}

	/*
	 * new function for handling updates and changes to the graph
	 */
	@SuppressWarnings("unchecked")
	public synchronized void update() {
		try {
			for (Iterator iter = getGraph().getVertices().iterator(); iter.hasNext();) {
				Vertex v = (Vertex) iter.next();
				Coordinates coord = getCoordinates(v);
				if (coord == null) {
					coord = new Coordinates();
					v.addUserDatum(getBaseKey(), coord, UserData.REMOVE);
				}
				initializeLocation(v, coord, getCurrentSize());
				initialize_local_vertex(v);

			} 
		} catch(ConcurrentModificationException cme) {
			update();
		}
		initialize_local();
	}

	protected void initialize_local() {}



	@Override
	public void advancePositions() {
		// For incremental layouts, implements each incremental update

	}



	@Override
	protected void initialize_local_vertex(Vertex v) {
		if (v.getUserDatum(getBaseKey()) == null) {
			v.addUserDatum(getBaseKey(), new Coordinates(), UserData.REMOVE);
		}
	}


	public boolean incrementsAreDone() {
		return true;
	}

}

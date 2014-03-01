package collectivesim.visualization.network.jung;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.FRLayout;

public class ForceGradientLayout extends FRLayout {

	String attribute;
	
	public ForceGradientLayout(Graph g) {
		super(g);

	}

}

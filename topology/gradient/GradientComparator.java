package edu.upc.cnds.collectivesim.topology.gradient;

import java.util.Comparator;

import edu.upc.cnds.collectives.node.Node;

/**
 * Compares to nodes with respect of the utility of a base node;
 * 
 * @author Pablo Chacin
 *
 */
public class GradientComparator implements Comparator {

	Node node;
	
	public GradientComparator(Node node){
		this.node = node;
	}
	
	@Override
	public int compare(Object o1, Object o2) {
		Double utility = (Double)node.getAttributes().get("utility");
		
		Double u1 =  (Double)(o1);
		Double u2 =  (Double)(o2);

		Double dist1 = u1 >= utility? u1-utility: 1-(utility-u1);
		Double dist2 = u2 >= utility? u2-utility: 1-(utility-u2);
		
		
		return dist1.compareTo(dist2);
	}

}

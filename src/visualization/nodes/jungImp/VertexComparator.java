package collectivesim.visualization.nodes.jungImp;

import java.util.Comparator;

import edu.uci.ics.jung.graph.Vertex;
import collectives.identifier.Identifier;
/**
 * Alloes the comparation of vertex according to a given comparator
 * that compares an specific user datum.
 * 
 * @author Pablo Chacin
 *
 */
public class VertexComparator implements Comparator{
	
	   private String datum;
	   private Comparator comparator;
	   
	  public VertexComparator(String datum, Comparator comparator){
		this.datum = datum;
		this.comparator = comparator;
		
	   }

	public int compare(Object o1, Object o2) {
		Vertex thisVertex = (Vertex)o1;
		Vertex otherVertex = (Vertex)o2;
		Identifier thisId = (Identifier)thisVertex.getUserDatum(datum);
		Identifier otherId = (Identifier)otherVertex.getUserDatum(datum);
		
		return comparator.compare(thisId,otherId);
	}
	   
	   
}

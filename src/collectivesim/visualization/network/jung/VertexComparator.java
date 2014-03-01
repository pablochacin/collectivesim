package collectivesim.visualization.network.jung;

import java.util.Comparator;

import edu.uci.ics.jung.graph.Vertex;
/**
 * Allows comparing vertex according to a given comparator
 * that compares an specific user datum.
 * 
 * @author Pablo Chacin
 *
 */
public class VertexComparator<T> implements Comparator{
	
	   private String datum;
	   private Comparator<T> comparator;
	   
	  public VertexComparator(String datum, Comparator<T> comparator){
		this.datum = datum;
		this.comparator = comparator;
		
	   }

	public int compare(Object o1, Object o2) {
		Vertex thisVertex = (Vertex)o1;
		Vertex otherVertex = (Vertex)o2;
		T thisDatum = (T)thisVertex.getUserDatum(datum);
		T otherDatum  = (T)otherVertex.getUserDatum(datum);
		
		return comparator.compare(thisDatum,otherDatum);
	}
	   
	   
}

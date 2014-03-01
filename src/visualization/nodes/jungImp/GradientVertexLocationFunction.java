package collectivesim.visualization.nodes.jungImp;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.visualization.VertexLocationFunction;
import collectives.identifier.Identifier;


/**
 * Places a Vertex based on its initial location and the intensity of the "attraction" of its neighbors. 
 * This attraction is measured as the different of the vertex's attribute value and those of its neighbors.
 * 
 *  X = SUM[(X-Xi)*(Ai-A)]
 *  Y = SUM[(Y-Yi)*(Ai-A)]
 *  For neighbors Ni so that Ai >= A
 *  Where A is the vertex's attribute value and Ai is the attribute value for node Ni.
 *
 * @author Pablo Chacin
 *
 */
public class GradientVertexLocationFunction implements VertexLocationFunction {

	private Map v_locations;
	private Dimension size;
	private String attribute;

	public GradientVertexLocationFunction(String attribute,int sizeX, int sizeY) {
		this.attribute = attribute;
		this.v_locations = new HashMap();
		this.size = new Dimension(sizeX,sizeY);
	}

	public void resize(Dimension size) {
		this.size = size;
	}

	public Point2D getLocation(ArchetypeVertex v) {

		@SuppressWarnings("unused")
		Identifier id = (Identifier)v.getUserDatum("id");
		
		Double originX = (Double)v.getUserDatum("positionX");
		Double originY = (Double)v.getUserDatum("positionY");

		Double newX = originX;
		Double newY = originY;

		double attr = (Double)v.getUserDatum(attribute);

		for(Object o: v.getNeighbors()){
			ArchetypeVertex n = (ArchetypeVertex)o;

			double neighborAttr = (Double)n.getUserDatum(attribute);
			if(neighborAttr > attr){
				Double neighborX = (Double)n.getUserDatum("positionX");
				Double neighborY= (Double)n.getUserDatum("positionY");

				newX += (neighborX-originX)*(neighborAttr-attr);
				newY += (neighborY-originY)*(neighborAttr-attr);

			}
		}

		Point2D location = new Point2D.Double(newX*size.width,newY*size.height);
		v_locations.put(v, location);
		return location;

	}


	public void reset()
	{
		v_locations.clear();
	}

	public Iterator getVertexIterator()
	{
		return v_locations.keySet().iterator();
	}
}

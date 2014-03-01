package collectivesim.visualization.nodes.jungImp;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.visualization.Coordinates;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.VertexLocationFunction;
import edu.uci.ics.jung.visualization.contrib.CircleLayout.CircleVertexData;
import collectives.identifier.IdSpace;
import collectives.identifier.Identifier;


/**
 * Places a Vertex based on its "mass" and Id, so that vetex with more mass are put to the center
 * and nodes with less mass to the edge. 
 * 
 * For doing so, the function calculates the actual radius, with respect of the center of the layout, on which the 
 * on which the vertex must be placed. Additionally, the vertex is located in a circle of the calculated
 * radius, in a possition that depends of the distance of its id with respect of the id "0", as
 * calculated by the Idspace.
 * 
 * @author Pablo Chacin
 *
 */
public class MassVertexLocationFunction implements VertexLocationFunction {

	private IdSpace space;
    private Map v_locations;
    private Dimension size;
    private String attribute;

	public MassVertexLocationFunction(IdSpace space,String attribute,int sizeX, int sizeY) {
		this.space = space;
		this.attribute = attribute;
		this.v_locations = new HashMap();
		this.size = new Dimension(sizeX,sizeY);
	}

	public void resize(Dimension size) {
		this.size = size;
	}
	
	public Point2D getLocation(ArchetypeVertex v) {

		
        Point2D location = (Point2D)v_locations.get(v);
        if (location == null) {
        	
        	
        	double weight = (Double)v.getUserDatum(attribute);
    		double height = size.getHeight();
    		double width =  size.getWidth();

    		Double radius = (0.45 * (height < width ? height : width))*(1-weight);

    		Identifier id = (Identifier)v.getUserDatum("id");
    		Double distance = space.getDistance(space.getMaxIdentifier(),id);


    		double angle = 2 * Math.PI  * distance;
    		
    		double x = Math.cos(angle) * radius + width / 2;
    		double y = Math.sin(angle) * radius + height / 2;
  
            location = new Point2D.Double(x,y);
            v_locations.put(v, location);
        }
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

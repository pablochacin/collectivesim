package collectivesim.visualization.network.jung;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import collectivesim.util.IdUtils;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.visualization.VertexLocationFunction;


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

	private BigInteger maxId;
	private Map v_locations;
    private Dimension size;
    private String attribute;

	public MassVertexLocationFunction(String maxId,String attribute,int sizeX, int sizeY) {
		this.maxId = new BigInteger(maxId);
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

    		String id = (String)v.getUserDatum("id");
    		Double distance = IdUtils.getDistance(new BigInteger(id), maxId);


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

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

public class CircleVertexLocationFunction implements VertexLocationFunction {


    private Map<ArchetypeVertex,Point2D> v_locations;
    private Dimension size;
    BigInteger maxId;

	public CircleVertexLocationFunction(String maxId,int sizeX,int sizeY) {
		this.maxId = new BigInteger(maxId);
		this.v_locations = new HashMap<ArchetypeVertex,Point2D>();
		this.size = new Dimension(sizeX,sizeY);
	}

	public void resize(Dimension size) {
		this.size = size;
	}
	
	public Point2D getLocation(ArchetypeVertex v) {

		
        Point2D location = v_locations.get(v);
        if (location == null) {
        
    		double height = size.getHeight();
    		double width =  size.getWidth();

    		Double radius = 0.45 * (height < width ? height : width);

    		String id = (String)v.getUserDatum("id");
    		Double distance = IdUtils.getDistance(new BigInteger(id),maxId);


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

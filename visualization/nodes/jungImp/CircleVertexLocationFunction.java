package edu.upc.cnds.collectivesim.visualization.nodes.jungImp;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.visualization.VertexLocationFunction;
import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.identifier.Identifier;

public class CircleVertexLocationFunction implements VertexLocationFunction {


	private IdSpace space;
    private Map<ArchetypeVertex,Point2D> v_locations;
    private Dimension size;

	public CircleVertexLocationFunction(IdSpace space,int sizeX,int sizeY) {
		this.space = space;
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

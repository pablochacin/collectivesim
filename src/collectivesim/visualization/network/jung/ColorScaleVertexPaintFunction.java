package collectivesim.visualization.network.jung;

import java.awt.Color;
import java.awt.Paint;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;

public class ColorScaleVertexPaintFunction implements VertexPaintFunction {

	private String attribute;
	
	private double minValue,maxValue;
	
	private Paint drawPaint;
	
	
	
	public ColorScaleVertexPaintFunction(Paint drawPaint,String attribute, double minValue,
			double maxValue) {
		
		this.drawPaint = drawPaint;
		this.attribute = attribute;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	public Paint getDrawPaint(Vertex v) {
		return drawPaint;	
	}

	@Override
	public Paint getFillPaint(Vertex v) {
		Double value = (Double)v.getUserDatum(attribute);
		
		float scale = (float)( value/(maxValue-minValue));
		
		return new Color(scale,scale,scale);
	}

}

package collectivesim.visualization.nodes.jungImp;

import java.awt.Color;
import java.awt.Paint;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;

public class DiscreteColorScaleVertexPaintFunction implements VertexPaintFunction {

	private String attribute;
	
	/*
	 * Valid range of values
	 */
	private double range;
	
	private Paint drawPaint;
	
	/**
	 * span of each interval
	 */
	private double step;
	
	public DiscreteColorScaleVertexPaintFunction(Paint drawPaint,String attribute, double minValue,
			double maxValue,int intervals) {
		
		this.drawPaint = drawPaint;
		this.attribute = attribute;
		this.range = maxValue-minValue;
		this.step = 1.0/(double)intervals;
	}

	@Override
	public Paint getDrawPaint(Vertex v) {
		return drawPaint;	
	}

	@Override
	public Paint getFillPaint(Vertex v) {
		Double value = (Double)v.getUserDatum(attribute);
				
		//map the value to an interval
		double interval = Math.floor((value/range)/step);
		
		//set the scale to the corresponding interval
		float scale = (float)( interval*step);
		
		return new Color(scale,scale,scale);
	}

}

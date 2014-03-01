package collectivesim.visualization.charts;

import java.util.Map;

import collectivesim.dataseries.DataSeries;
import collectivesim.visualization.View;

/**
 * A graphical representation of one or more series of values. 
 * 
 * The plot can be configured by a Map of attributes,which depend largely of the type of
 * plot and the actual implementation. However, the following attributes are expected to be supported:
 * <ul>
 * <li>title: title of the plot
 * <li>height: vertical size in points
 * <li>width: horizontal size in points
 * <li>background: of the backgroud of the plot 
 * <li>grid.visible indicates if the grid is visible or not
 * <li>grid.color: the of the plot's grid, if visible.
 * <li>axisX.min, axisX.max: min and max values for axis X
 * <li>axisX.label label of axis X.
 * <li>axisY.min, axisY.max: min and max values for axis Y
 * <li>axisY.label label of axis Y.
 * <li>defaultcolor: default color for series
 * </ul>
 * 
 * @author Pablo Chacin
 *
 */
public interface Chart extends View{

	
	/**
	 * Adds a sequence to to the Plot specifying the plotting properties. These properties are
	 * mostly implementation dependent but the following are expected to be supported:
	 * <ul>
	 * <li> color
	 * <li> label: label of the series
	 * </ul>
	 * 
	 * 
	 * @param name of the sequence
	 * @param series DataSeries of values to plot
	 * @param attribute a String with the name of the attribute
	 * @param properties map of properties
	 * 
	 */
	public void addSequence(String name,DataSeries series,String sequenceAttribute,String valueAttribute,Map<String,String> properties) throws UnsoportedChartProperty;

	/**
	 * Adds a sequence to to the Plot using plot's default properties
	 * @param name of the sequence
	 * @param series DataSeries of values to plot
	 * @param attribute a String with the name of the attribute
	 */
	public void addSequence(String name,DataSeries series,String sequenceAttribute,String valueAttribute) throws UnsoportedChartProperty;

	
	/**
	 * Sets the properties of the plot
	 * @param properties
	 * @throws UnsoportedChartProperty
	 */
	public void setProperties(Map<String,String> properties) throws UnsoportedChartProperty;
	
	
	/**
	 * Generates a sample chart. Used for testing the visualization of a chart.
	 * @throws UnsoportedChartProperty 
	 */
	public void sampleChart() throws UnsoportedChartProperty;
}

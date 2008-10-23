package edu.upc.cnds.collectivesim.views.histograms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import uchicago.src.sim.analysis.BinDataSource;

/**
 * Provides the information of an agent
 * 
 * @author Pablo Chacin
 *
 */
 public class DataSeriesIteratorWithReflection implements BinDataSource {

	/**
	 * attribute to be probed in the agent.
	 */
	private String attribute; 
	
	/**
	 * getter method to obtain the attribute
	 */
	Method getter;
	
	public DataSeriesIterator(String attribute){
	   this.attribute = attribute;
	}
	
	/**
	 * @param agent and Object reference to the agent whose
	 *        value must be calculated
	 */
	public double getBinValue(Object agent) {
	   
		Double value = new Double(0);
		try {
		//obtain a reference to the getter method
		getter = agent.getClass().getMethod("get"+attribute, new Class[0]);
		
		//use getter to obtain the value of the attribute
		value = (Double)(getter.invoke(agent, (Object[])null));   
		
	   } catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   } catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   } catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
	   finally{
		   return value.doubleValue();
	   }
	}

}

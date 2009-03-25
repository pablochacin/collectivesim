package edu.upc.cnds.collectivesim.experiment.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.upc.cnds.collectives.identifier.BasicIdentifier;
import edu.upc.cnds.collectives.util.ReflectionUtils;
import edu.upc.cnds.collectivesim.experiment.ExperimentException;
import edu.upc.cnds.collectivesim.experiment.Table;

public class MemoryTable<T> implements Table<T> {

	protected List<T>values;

	protected String name;
	
	public MemoryTable(String name){
		this(name,new ArrayList<T>());
	}
	
	public MemoryTable(String name,List<T>values){
		this.name = name;
		this.values = new ArrayList<T>(values);
	}
	
	@Override
	public T getElement(int i) {
		return values.get(i);
	}

	@Override
	public List<T> getElements() {
		return new ArrayList(values);		
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getNumValues() {
		return values.size();
	}

	/**
	 * Method used by subclasses to add values once the table is created
	 * 
	 * @param values a List of values to add
	 */
	protected void setValues(List<T> values){
		this.values.addAll(values);
	}
	
	protected void addValue(T value){
		values.add(value);
	}

	@Override
	public void load() throws ExperimentException {	
		
	}
	/**
	 * Loads values from a list of Strings. Each string is converted to the
	 * Table's base type by either creating a new instance with the values as
	 * a sole argument, or by invoking the valueOf(String) method (this is 
	 * useful for Double, Long, Boolean, String, BigIngeger)
	 * 
	 * @param textValues a List of Strings
	 * @param type the Class of the elements to be returned. This argument is necessary because
	 *        in java, generic's  parameterized types are not accessible at runtime 
	 *        (it is very tricky to know at run time the type of a parameterized collection)
	 */
	
	protected void parseValues(List<String> textValues,Class type) throws IllegalArgumentException {
				
		for(String s: textValues){
			addValue(s,type);
		}
		
	}
	
	
	/**
	 * Adds a value from a String, parsing it as a value of the type
	 * @param value
	 * @param type
	 */
	protected void addValue(String srtValue,Class type){
		T value = (T)ReflectionUtils.parseValue(srtValue,type);
		values.add(value);

	}
	
	
	/**
	 * Convenience method, parse String values in an array 
	 * Convenient to handle comma separated values in String
	 * ("value1,value2, ...,valueN") which can easely splited
	 * as an array of Strings {@see java.lang.String#split(String)}
	 * 
	 * @param values
	 * @param type
	 */
	protected void parseValues(String[] values,Class type){
	
		parseValues(Arrays.asList(values),type);
	}
	
	
	public static void main(String[] args){
		
		MemoryTable<Long> table = new MemoryTable<Long>("table");
	
		List<String> textValues = new ArrayList<String>();
		textValues.add("0032");
		textValues.add("0320");
		
		table.parseValues(textValues,BasicIdentifier.class);
		for(int i = 0;i<table.getNumValues();i++){
			System.out.println(table.getElement(i));
		}
	}
}

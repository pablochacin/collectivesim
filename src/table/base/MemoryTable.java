package collectivesim.table.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import collectives.util.ReflectionUtils;
import collectivesim.table.Table;
import collectivesim.table.TableException;

/**
 * Implements a table in memory, loading values from text
 * 
 * @author Pablo Chacin
 *
 * @param <T>
 */
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
	
	public MemoryTable(String name,String[] values,Class type){
		this.name = name;
		this.values = new ArrayList<T>();
		parseValues(values,type);
	}
	
	public MemoryTable(String name,String values,String delimiter,Class type){
		this.name = name;
		this.values = new ArrayList<T>();
		parseValues(values,delimiter,type);		
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
	public void load() throws TableException {	
		
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
	
	protected void parseValues(String values,String delimiter,Class type){
		String[] valueArray = values.split(delimiter);
		parseValues(valueArray,type);
		
	}
	
	
	public String toString(){
	
		if(getNumValues() == 0){
			return "{}";
		}
		
		StringBuffer buffer = new StringBuffer();		
		buffer.append("{");
		buffer.append(getElement(0).toString());
		for(int i=1;i<getNumValues();i++){
			buffer.append(",");
			buffer.append(getElement(i));
		}
		buffer.append("}");
		
		return buffer.toString();
	}
	
	public static void main(String[] args){
		
	
		List<Long> valueList = new ArrayList<Long>();
		valueList.add(new Long(0032));
		valueList.add(new Long(0320));
		
		MemoryTable<Long> table1 = new MemoryTable<Long>("table",valueList);
		MemoryTable<Long> table2 = new MemoryTable<Long>("table 2","0032;0320",";",Long.class);
		String[] valueArray = {"0032","0320"};
		MemoryTable<Long> table3 = new MemoryTable<Long>("table 3",valueArray,Long.class);
		
		
		System.out.println("table1 \n" + table1.toString());
		System.out.println("table2 \n" + table2.toString());
		System.out.println("table3 \n" + table3.toString());
	}
}

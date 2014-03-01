package collectivesim.parameters;

import collectivesim.util.ReflectionUtils;

/**
 * A parameter that can be read from an external textual source, like a command line
 * or configuration file.
 * 
 * @author 
 *
 * @param <T>
 */
public class Parameter<T>{

	protected boolean required;
	
	protected String name;
		
	protected T value;
	
	protected Class type;
	
	protected T defaultValue;
	
	/**
	 * Constructor that copies information from a template argument
	 * is useful to create arguments from static definitions
	 * 
	 * @param template
	 */
	public Parameter(Parameter template){
		this(template.getName(),template.isRequired(),
			 template.getType(),(T) template.getDefaultValue());
	}
	
	
	
	public Parameter(String name,boolean required,Class type,T defaultValue) {
		this.required = required;
		this.name = name;
		this.defaultValue = defaultValue;
		this.type = type;
	}

	public Parameter(String name,boolean required,Class type,String defaultValue) {
		this.name = name;
		this.required = required;
		this.type = type;
		this.defaultValue = (T)ReflectionUtils.parseValue(defaultValue, type);
	}

	public Parameter(String name,boolean required,Class type) {
		this(name,required,type,(T)null);
	}

	public Parameter(String name,boolean required,T defaultValue){
		this(name,required,defaultValue.getClass(),defaultValue);
	}
	
	public String getName() {
		return name;
	}

	public void setValue(String value) {
		
		if((required) && (value == null)){
			throw new IllegalArgumentException("Argument "+name + " is required");
		}
		
		if(value == null){
			this.value = defaultValue;
		}
		else{
			this.value = (T)ReflectionUtils.parseValue(value,type);
		}
	}

	
	public boolean isRequired(){
		return required;
	}
	
	public T getValue(){
		return value;
	}

	public Class getType(){
		return type;
	}
	
	public T getDefaultValue(){
		return defaultValue;
	}
}


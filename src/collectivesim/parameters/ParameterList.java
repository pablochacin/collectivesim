package collectivesim.parameters;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * A List of parameters that can be read form an external source, like a Map, 
 * a configuration file or a command line argument list
 * 
 * @author Pablo Chacin
 *
 */
public class ParameterList {

	private Map<String,Parameter>parameters;
	
	public ParameterList(List<Parameter>parameters){
		this.parameters = new HashMap<String,Parameter>();
		addParameters(parameters);
	}
	
	public ParameterList(){
		this(new ArrayList<Parameter>());
	}
	
	public ParameterList(Parameter[] parameters){
		this(Arrays.asList(parameters));
	}
	
	
	public void addParameter(Parameter parameter){
		parameters.put(parameter.getName(),parameter);
	}
	
	public void addParameters(List<Parameter>parameters) {
		for(Parameter p: parameters){
			addParameter(p);
		}
	}
	
	public void addParameter(Parameter[] parameters){
		addParameters(Arrays.asList(parameters));
	}
	
	/**
	 * Validates the arguments in a Map
	 * 
	 * @param arguments a Map with the arguments and their values (as Strings)
	 * 
	 * @throws ParameterException if any argument is valid
	 */
	public void load(Map arguments) {
	
		for(Parameter p: parameters.values()){
			String value = (String)arguments.get(p.getName());
			p.setValue(value);
		}
	}
	
	
	public Parameter getParameter(String name){
		Parameter parameter = parameters.get(name);
		if(parameter == null){
			throw new IllegalArgumentException("Unknown Parameter "+name);
		}
		return parameter;
	}

}

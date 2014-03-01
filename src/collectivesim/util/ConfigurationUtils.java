package collectivesim.util;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Tools for configuring applications
 * 
 * @author Pablo Chacin
 *
 */
public class ConfigurationUtils {

	
	/**
	 * Transform an array of strings of the form -key value and creates a Map 
	 * of <key,value> pairs. 
	 * 
	 * @param args
	 * @return
	 */
	public static Map<String,String> getArguments(String[] args) {
		//load the parametes passed to in the args into a map
		Map<String,String> argsMap = new HashMap<String,String>();

		//check an even number of elements
		if(args.length % 2 != 0) {
			throw new InvalidParameterException("invalid syntax of argument lists");
		}

		for(int i = 0;i<args.length;i+=2){

			if(!args[i].startsWith("-")) {
				throw new InvalidParameterException("invalid syntax of argument lists");
			}

			//Add to map removing leading "-"
			argsMap.put(args[i].substring(1),args[i+1]);
		}

		return argsMap;
	}
}

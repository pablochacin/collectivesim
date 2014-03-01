package collectivesim.util;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Creates a Map from a String with a sequence of key=value elements
 * delimited by a delimieter charater (by default an space"
 * 
 * "key1=value1 key2=value2 ... keyn=value2"
 * 
 * @author Pablo Chacin
 *
 */
public class MapFromString extends HashMap<String,String> {

	private static String DEFAULT_DELIMITER = ";";
	
	public MapFromString(String keyvaluepairs){
		super();
		parseValues(keyvaluepairs,DEFAULT_DELIMITER);
	}
	
	public MapFromString(String keyvaluepairs,String separator){
		super();
		parseValues(keyvaluepairs,separator);
	}
	
	
	protected void parseValues(String keyvaluepairs,String separator){
		
		StringTokenizer st= new StringTokenizer(keyvaluepairs, separator);
		while (st.hasMoreTokens()) {
			String token= st.nextToken();
			int i= token.indexOf('=');
			if (i < 1)
			    throw new IllegalArgumentException("kev/value pair '" + token + "' is illformed"); //$NON-NLS-1$ //$NON-NLS-2$
			String value= token.substring(i+1);
			token= token.substring(0, i);
			put(token, value);
		}
	}
}

package collectivesim.configuration;


import java.io.InputStream;
import java.util.Map;


/**
 * 
 * Defines a generic interface to manage the process of loading a configuration 
 * and retrieving its components. A Configuration is treated as a set of loadable 
 * ConfigurationElements, which have a name, a type, a base class (used to create 
 * instances) and some parameters.
 * <p>
 * Elements are referenced by a combination of the following attributes:
 * <UL>
 * <LI> its type (mandatory)
 * <LI> its name (optional, as might be unnamed elements)
 * </UL>
 * 
 * @author Pablo Chacin
 *
 * @version 1.0
 */
public interface Configuration extends ConfigurationElement {
	
	/**
	 * Loads and validate the configuration from an external format. 
	 * Each implementing class is intended to handle one or more specific formats, 
	 * being these persisntent (e.g. xml files, properties files) or volatile
	 * (e.g. a hashMap).
	 * <p>
	 * Classes implementing this interface must check the validity of the config according to the
	 * format specific rules and the minimun consitency required by the configuration.
	 * Some basic consistency rules are:
	 * <ul>
	 * <LI> The reference to the external format is missing or invalid (this reference
	 *      is usually passed to the subclass in its constructor method or a setter method)
	 * <LI> All required parameters must be defined or a default value provided.
	 * 	    TODO: finish this list of validations
	 * </ul>
	 * <p>
	 * The procedure for loading the configuration should also assure that all 
	 * required parameter for any configuration item (hosting, service, protocol, agent) that 
	 * is not specified receives an approapiated default. How (and where those defaults
	 * are defined depends on the implementation of the subclass and the specific 
	 * external format being used.
	 *  
	 * @throws ConfigurationException if there is an error when initalizing 
	 *                                the configuration  
	 */
	 public void load(String configFile) throws ConfigurationException;
	
	
	 /**
	  * Same as {@link #load(String)} but receives an input stream to read from 
	  * 
	  * @param inStream
	  * @throws ConfigurationException
	  */
	 public void load(InputStream inStream) throws ConfigurationException;
	 
	/**
	 * Same as <code>load()</code> but additionaly adds a set of properties defined in a Map.
	 * If a property with the same name already exists, it is overwritten.
	 * 
	 * @param propertiesMap a Map with configuration parameters to load into the configuration
	 * 
	 * @throws ConfigurationException if there is an erros when initalizing 
	 *                                the configuration
	 */
	public void load(Map<String,String> propertiesMap);
	
	/**
	 * Returns an specific configuration element.
	 * 
	 * @param type a String with the type of element
	 * @param id a String with the identification of the element
	 * 
	 * @return the configuration element,or null if not found
	 * @throws ConfigurationException 
	 * 
	 */
	
	
	/**
	 * Builds the configuration elements from the configuration. Must be called before any Configuration element
	 * is retrieved.
	 */
	public void build();

	public ConfigurationElement getElement(String type,String id) throws ConfigurationException;
	
	/**
	 * Returns all the configuration elements of a given type. Finds all the elements of a given 
	 * type defined in a configuration and calls the getElement for each of them.
	 * 
	 * @param type a String with the type of element
	 * @throws ConfigurationException 
	 *    
	 * @see #getElement(String, String, String)
	 *  
	 * @returms a Map with the elements indexed by its id. If none is found, the map is empty.
	 *  
	 */	
	public Map<String, ConfigurationElement> getElements(String type) throws ConfigurationException;
	
	/**
	 * Convenience method that returns a map with all the elConfigurationElementements contained in the Configuration
	 * 
	 * @return a Map with the elements indexed by its string "type.id"
	 * @throws ConfigurationException 
	 */
	public Map<String,ConfigurationElement> getElements() throws ConfigurationException;
}

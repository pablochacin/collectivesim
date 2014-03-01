/*
 * ConfigurationElement.java
 */
package collectivesim.configuration;
/*
 * Grid Market Middleware (GMM)
 *
 * Copyright (C) 2004-2009  Computer Networks and Distributed
 * Systems Lab, Technical University of Catalonia, Spain
 * 
 * Grid Market Middleware is free software: you can redistribute it 
 * and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 *
 * Grid Market Middleware is distributed in the hope that it will be 
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */




import java.util.Map;

import collectivesim.util.TypedMap;

/**
 * Defines an element defined in a configuration, which has:
 * <UL>
 * <LI> a type
 * <LI> an id
 * <LI> a set of parameters that the element can use to configure the element
 * <LI> a set of configuration elements (subconfiguration) which define the 
 *      elements on which this one relies
 * </UL>
 * 
 * @author Pablo Chacin
 * 
 * @version 1.0
 *
 */
public interface ConfigurationElement {

	/**
	 * @return Returns the id of this element
	 */
	public String getId();
	
	/**
	 * @return Returns the type.
	 */
	public String getType();

	/**
	 * 
	 * @return Returns the name of this element
	 */
	public String getName();
	
	/**
	 * 
	 * @return Returns the name of the class of this element. 
	 *         It can be null if a builder is specified in the configuration file.
	 */
	public String getClassName();
	
	/**
	 * @return Returns the parameters.
	 * @throws ConfigurationException 
	 */
	public TypedMap getParameters();

	/**
	 * Retrieves a configuration created with the attributes of an configuration element.
	 * 
	 * @param element a ConfigurationElement whose attributes will be used to create a
	 *                 configuration
	 *                 
	 * @return a Configuration
	 * 
	 * @throws ConfigurationException if the attributes
	 *         of the ConfigurationElement do not conform a valid configuration. 
	 */
	public Configuration getSubConfiguration() throws ConfigurationException;
	
	/**
	 * Instantiates, builds and configure the object defined by this configuration element. 
	 * 
	 * @return An instance builded and configured of the element defined by this configuration
	 *         element
	 * @throws ConfigurationException
	 */
	
	public Object instantiate() throws ConfigurationException;
	
}
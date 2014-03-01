package collectivesim.configuration;

import java.io.InputStream;
import java.util.Map;

import collectivesim.util.TypedMap;

public class PropertiesConfiguration implements Configuration {

	@Override
	public void load(String configFile) throws ConfigurationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(InputStream inStream) throws ConfigurationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(Map<String,String> propertiesMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void build() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ConfigurationElement getElement(String type, String id) throws ConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String,ConfigurationElement> getElements(String type) throws ConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String,ConfigurationElement> getElements() throws ConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypedMap getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Configuration getSubConfiguration() throws ConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object instantiate() throws ConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

}

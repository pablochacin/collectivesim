package collectivesim.configuration;


public class ConfigurationException extends Exception {
	
	public ConfigurationException() {
		super();
	}
	
	public ConfigurationException(Exception e) {
		super(e);
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

}

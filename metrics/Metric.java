package edu.upc.cnds.collectivesim.metrics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Metric implements Serializable{
	
    private double timestamp;
    private String name;
    private String value;
	private Map attributes;
	
    
	public Metric(String name, String value ,double timestamp,Map attributes) {
		super();
		this.name = name;
		this.value = value;
		this.attributes= attributes;
        this.timestamp = timestamp;
	}
	
		
	public String getName() {
		return this.name;
	}

	
	public Map getAttributes() {
		return attributes;
	}

	public String getValue(){
		return this.value;
	}


    public double getTimestamp() {
       return this.timestamp;
    }
}
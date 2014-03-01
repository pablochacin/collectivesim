package collectivesim.util;

import java.io.Serializable;

/**
 * Mantains a value associated with a name and
 * tries to mantain an unique value and name
 * accross subclasses. 
 * 
 * @author Pablo Chacin
 *
 */
public abstract class EnumeratedValue implements Serializable, Comparable{

	private String shortName;
	
	private String fullName;
	
	private int hash;
	
	
	public EnumeratedValue(String name) {
		this.shortName = name;
		
		fullName = buildName(name);
		this.hash = fullName.hashCode();	
	}	
	
	public final String getName() {
		return shortName;
		
	}
	
	public final String getFullName() {
		return fullName;
	}
	

	private final String buildName(String name) {
		
		name = getClass().getSimpleName()+"."+name;
		
		//track all the hierarchy
		Class parent = this.getClass().getSuperclass();
		do{
			
			name = parent.getSimpleName()+"."+name;
			parent = parent.getSuperclass();
		}while(parent == EnumeratedValue.class);

		return name;
		
	}
	
	
	/**
	 * 
	 */
    public int compareTo(Object other) {
    	EnumeratedValue otherValue = (EnumeratedValue)other;
    	
    	return this.fullName.compareTo(otherValue.fullName);
    	
    }
	
    
    public boolean equals(Object other) {
    	EnumeratedValue otherValue = (EnumeratedValue)other;
    	
    	return this.fullName.equals(otherValue.fullName);
    
    
    }
    
    public String toString() {
    	return fullName;
    }
    
    public int hashCode() {
    	return hash;
    }
}

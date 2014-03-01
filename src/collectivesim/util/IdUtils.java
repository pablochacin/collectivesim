package collectivesim.util;

import java.math.BigInteger;

/**
 * Utility functions to handle Identifiers
 * 
 * @author pablo
 *
 */
public class IdUtils {

    /**
	 * Calculates the distance between the ids considering them in a
	 * circular disposition
	 * 
	 */
	public static Double getDistance(BigInteger id, BigInteger maxId) {
				
				
		BigInteger difference = id.subtract(maxId).abs();	
		
		BigInteger distance = difference.min(maxId.subtract(difference));
	
		return distance.doubleValue()/ maxId.doubleValue();
	}
}

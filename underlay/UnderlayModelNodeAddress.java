package edu.upc.cnds.collectivesim.underlay;


import edu.upc.cnds.collectives.underlay.UnderlayAddress;
import edu.upc.cnds.collectivesim.CollectiveSim;

public class UnderlayModelNodeAddress implements UnderlayAddress{

	
	private String location;

	public UnderlayModelNodeAddress(String location) {
		if(location == null){
			throw new IllegalArgumentException("Location cannot be null");
		}
		
		this.location = location;
	}
	
	
	/**
	 * Create an address with a random location
	 */
	public UnderlayModelNodeAddress() {
		this(generateLocation());
	}
	
	private static String generateLocation(){	
		byte[] address = new byte[4];		
		CollectiveSim.getExperiment().getRandomGenerator().nextBytes(address);
		int port = CollectiveSim.getExperiment().getRandomGenerator().nextInt();
		
		String location = address[0] + "." +
						address[1] + "." +
						address[2] + "." +
						address[3] + ":" +
						port;
		
		
		return location;
	}
	
	public String getLocation() {
		return location;
	}
}